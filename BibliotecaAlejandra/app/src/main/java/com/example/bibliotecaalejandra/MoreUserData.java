package com.example.bibliotecaalejandra;

import java.util.List;

public class MoreUserData {
    private List<LibroUsuario> librosEnPosesion;
    private List<LibroUsuario> librosReservados;
    private int maxLibrosReservar;
    private int maxLibrosPrestar;
    private List<LibroUsuario> historial;

    public List<LibroUsuario> getLibrosEnPosesion() {
        return librosEnPosesion;
    }

    public void setLibrosEnPosesion(List<LibroUsuario> librosEnPosesion) {
        this.librosEnPosesion = librosEnPosesion;
    }

    public List<LibroUsuario> getLibrosReservados() {
        return librosReservados;
    }

    public void setLibrosReservados(List<LibroUsuario> librosReservados) {
        this.librosReservados = librosReservados;
    }

    public int getMaxLibrosReservar() {
        return maxLibrosReservar;
    }

    public void setMaxLibrosReservar(int maxLibrosReservar) {
        this.maxLibrosReservar = maxLibrosReservar;
    }

    public int getMaxLibrosPrestar() {
        return maxLibrosPrestar;
    }

    public void setMaxLibrosPrestar(int maxLibrosPrestar) {
        this.maxLibrosPrestar = maxLibrosPrestar;
    }

    public List<LibroUsuario> getHistorial() {
        return historial;
    }

    public void setHistorial(List<LibroUsuario> historial) {
        this.historial = historial;
    }
}
