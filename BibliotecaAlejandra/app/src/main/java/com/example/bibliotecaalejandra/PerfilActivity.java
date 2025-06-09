package com.example.bibliotecaalejandra;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.NotificationChannel;
import android.app.NotificationManager;


public class PerfilActivity extends AppCompatActivity {
    private ApiService apiService;
    private MoreUserData moreUserData;
    private AppDatabase db;
    private NotificacionDao notificacionDao;

    private CalendarView calendarView;
    private TextView fechaTextView, descripcionTextView, estadoPenalizacionTextView;
    private ImageView estadoPenalizacionImageView, logoutImageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        crearCanalDeNotificaciones();

        db = AppDatabase.getInstance(this);
        notificacionDao = db.notificacionDao();

        TextView bienvenida = findViewById(R.id.bienvenida);
        String texto = bienvenida.getText().toString();
        bienvenida.setText(texto + " " + User.getNombre());

        //menu de navegacion
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_perfil);
        bottomNavigationView.setItemActiveIndicatorColor(ContextCompat.getColorStateList(this, R.color.chamoiseTransparent));
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.nav_libros) {
                    startActivity(new Intent(PerfilActivity.this, CatalogoActivity.class));
                    return true;
                }
                if (item.getItemId() == R.id.nav_eventos) {
                    startActivity(new Intent(PerfilActivity.this, EventosActivity.class));
                }
                return false;
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://biblioteca-alejandria.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        calendarView = findViewById(R.id.calendarView);
        fechaTextView = findViewById(R.id.textFechaSeleccionada);
        descripcionTextView = findViewById(R.id.textDescripcionEvento);
        estadoPenalizacionTextView = findViewById(R.id.penalizado_text);
        estadoPenalizacionImageView = findViewById(R.id.penalizado_icon);
        logoutImageView = findViewById(R.id.logout_button);

        logoutImageView.setOnClickListener(v -> {
            User.logout();
            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedCal = Calendar.getInstance();
            selectedCal.set(year, month, dayOfMonth, 0, 0, 0);
            selectedCal.set(Calendar.MILLISECOND, 0);

            Date selectedDate = selectedCal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String selectedStr = sdf.format(selectedDate);

            if (!fechasConEventos.contains(selectedStr)) {
                Toast.makeText(this, "No hay actividad este día", Toast.LENGTH_SHORT).show();
                return;
            }

            manejarClickDia(selectedDate);
        });

        User.recargarUsuarioDesdeAPI(User.getId(), apiService);
        cargarRegistrosUsuario();
    }

    private void cargarRegistrosUsuario() {
        int userId = User.getId();

        apiService.getMoreUserData(userId).enqueue(new Callback<MoreUserData>() {
            @Override
            public void onResponse(Call<MoreUserData> call, Response<MoreUserData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    moreUserData = response.body();
                    programarNotificacionesDevoluciones(moreUserData);
                    calcularFechasConEventos();
                    mostrarEstadoPenalizacion();
                    mostrarDiasConEventosDelMesActual();
                    manejarClickDia(new Date(calendarView.getDate()));
                } else {
                    Toast.makeText(PerfilActivity.this, "Error al cargar datos de usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MoreUserData> call, Throwable t) {
                Toast.makeText(PerfilActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearCanalDeNotificaciones() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                    String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence nombre = "Recordatorios";
            String descripcion = "Canal para recordatorios de libros";
            int importancia = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel canal = new NotificationChannel("canal_recordatorios", nombre, importancia);
            canal.setDescription(descripcion);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(canal);
        }
    }

    private void programarNotificacionesDevoluciones(MoreUserData data) {
        List<LibroUsuario> libros = data.getLibrosEnPosesion();

        for (LibroUsuario libro : libros) {
            Calendar fechaDevolucion = getFechaDevolucionComoCalendar(libro);
            if (fechaDevolucion != null) {
                apiService.getLibroById(libro.getLibro()).enqueue(new Callback<Libro>() {
                   @Override
                   public void onResponse(Call<Libro> call, Response<Libro> response) {
                       if (response.isSuccessful() && response.body() != null) {
                           Libro libro = response.body();
                           String tituloLibro = libro.getTitulo();
                           programarNotificacion(fechaDevolucion, tituloLibro);
                       }
                   }
                  @Override
                  public void onFailure(Call<Libro> call, Throwable t) {
                  }

               });
            }
        }
    }

    private Calendar getFechaDevolucionComoCalendar(LibroUsuario libro) {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date fecha = formato.parse(libro.getFecha_devolucion());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fecha);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void programarNotificacion(Calendar fechaDevolucion, String tituloLibro) {
        Calendar recordatorio = (Calendar) fechaDevolucion.clone();
        recordatorio.add(Calendar.DAY_OF_YEAR, -1);

        Date fechaRecordatorio = recordatorio.getTime();

        new Thread(() -> {
            NotificacionEntity existente = notificacionDao.obtenerNotificacion(tituloLibro, fechaRecordatorio.toString());

            if (existente == null) {
                // No existe, programar y guardar
                Intent intent = new Intent(PerfilActivity.this, RecordatorioReceiver.class);
                intent.putExtra("mensaje", tituloLibro);

                int requestCode = (tituloLibro + recordatorio.getTimeInMillis()).hashCode();

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        PerfilActivity.this,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (!alarmManager.canScheduleExactAlarms()) {
                        Intent intent2 = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                        startActivity(intent2);
                        return;
                    }
                }

                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        recordatorio.getTimeInMillis(),
                        pendingIntent
                );

                notificacionDao.insertarNotificacion(new NotificacionEntity(tituloLibro, fechaRecordatorio.toString()));

                runOnUiThread(() -> Log.d("NotificacionTest", "Programada notificación para: " + tituloLibro + " el " + fechaRecordatorio));
            } else {
                runOnUiThread(() -> Log.d("NotificacionTest", "Ya existe notificación programada para: " + tituloLibro));
            }
        }).start();
    }

    private void mostrarEstadoPenalizacion() {
        try {
            String fechaStr = User.getFechaPenalizacion();
            if (fechaStr != null && !fechaStr.isEmpty()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date fechaPenalizacion = simpleDateFormat.parse(fechaStr);
                Date hoy = new Date();

                if (fechaPenalizacion != null && (hoy.before(fechaPenalizacion) || simpleDateFormat.format(hoy).equals(fechaStr))) {
                    estadoPenalizacionImageView.setImageResource(R.drawable.circulo_rojo);
                    SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    estadoPenalizacionTextView.setText("Penalizado hasta " + displayFormat.format(fechaPenalizacion));
                } else {
                    estadoPenalizacionImageView.setImageResource(R.drawable.circulo_verde);
                    estadoPenalizacionTextView.setText("No penalizado");
                }
            } else {
                estadoPenalizacionImageView.setImageResource(R.drawable.circulo_verde);
                estadoPenalizacionTextView.setText("No penalizado");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            estadoPenalizacionImageView.setImageResource(R.drawable.circulo_verde);
            estadoPenalizacionTextView.setText("No penalizado");
        }
    }

    private void mostrarDiasConEventosDelMesActual() {
        Calendar actual = Calendar.getInstance();
        int mes = actual.get(Calendar.MONTH);
        int anio = actual.get(Calendar.YEAR);

        Set<Integer> diasConEvento = new TreeSet<>();

        for (String fechaStr : fechasConEventos) {
            try {
                Date fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fechaStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(fecha);
                if (cal.get(Calendar.MONTH) == mes && cal.get(Calendar.YEAR) == anio) {
                    diasConEvento.add(cal.get(Calendar.DAY_OF_MONTH));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        TextView textDiasEventos = findViewById(R.id.textDiasConEventos);
        if (diasConEvento.isEmpty()) {
            textDiasEventos.setText("No tienes ningún evento este mes.");
        } else {
            StringBuilder sb = new StringBuilder("Días con actividad este mes: ");
            for (int dia : diasConEvento) {
                sb.append(String.format("%02d", dia)).append(", ");
            }
            sb.setLength(sb.length() - 2);
            textDiasEventos.setText(sb.toString());
        }
    }

    private void manejarClickDia(Date clickedDate) {
        SimpleDateFormat sdfISO = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String clickedStr = sdfISO.format(clickedDate);

        List<LibroUsuario> encontrados = new ArrayList<>();

        Predicate<String> mismaFecha = fecha -> fecha != null && fecha.startsWith(clickedStr);

        for (LibroUsuario l : moreUserData.getHistorial()) {
            if (mismaFecha.test(l.getFecha_adquisicion()) || mismaFecha.test(l.getFecha_devolucion())) {
                encontrados.add(l);
            }
        }
        for (LibroUsuario l : moreUserData.getLibrosEnPosesion()) {
            if (mismaFecha.test(l.getFecha_adquisicion()) || mismaFecha.test(l.getFecha_devolucion())) {
                encontrados.add(l);
            }
        }
        for (LibroUsuario l : moreUserData.getLibrosReservados()) {
            if (mismaFecha.test(l.getFecha_adquisicion()) || mismaFecha.test(l.getFecha_devolucion())) {
                encontrados.add(l);
            }
        }

        if (encontrados.isEmpty()) {
            fechaTextView.setText("Selecciona un día marcado");
            descripcionTextView.setText("Para ver el evento relacionado");
            return;
        }

        LibroUsuario evento = encontrados.get(0);

        SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fechaTextView.setText(sdfDisplay.format(clickedDate));

        apiService.getLibroById(evento.getLibro()).enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Libro libro = response.body();

                    String descripcion = "";

                    if (evento.getFecha_adquisicion().startsWith(clickedStr)) {
                        descripcion = "Adquisición: " + libro.getTitulo();
                    } else if (evento.getFecha_devolucion() != null && evento.getFecha_devolucion().startsWith(clickedStr)) {
                        descripcion = "Devolución: " + libro.getTitulo();
                    } else {
                        descripcion = "Evento relacionado con: " + libro.getTitulo();
                    }

                    descripcionTextView.setText(descripcion);
                } else {
                    descripcionTextView.setText("Error al cargar libro");
                }
            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                descripcionTextView.setText("Error de conexión");
            }
        });
    }

    private Set<String> fechasConEventos = new HashSet<>();

    private void calcularFechasConEventos() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (LibroUsuario l : moreUserData.getHistorial()) {
            if (l.getFecha_adquisicion() != null) fechasConEventos.add(l.getFecha_adquisicion().substring(0, 10));
            if (l.getFecha_devolucion() != null) fechasConEventos.add(l.getFecha_devolucion().substring(0, 10));
        }
        for (LibroUsuario l : moreUserData.getLibrosEnPosesion()) {
            if (l.getFecha_adquisicion() != null) fechasConEventos.add(l.getFecha_adquisicion().substring(0, 10));
            if (l.getFecha_devolucion() != null) fechasConEventos.add(l.getFecha_devolucion().substring(0, 10));
        }
        for (LibroUsuario l : moreUserData.getLibrosReservados()) {
            if (l.getFecha_adquisicion() != null) fechasConEventos.add(l.getFecha_adquisicion().substring(0, 10));
            if (l.getFecha_devolucion() != null) fechasConEventos.add(l.getFecha_devolucion().substring(0, 10));
        }
    }

}
