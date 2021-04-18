package com.bitgymup.gymup.users;

public class Salud {
    private String id_salud, titulo, contenido, fechaCreacion, gymName;

    public Salud() {
    }

    public Salud(String id_salud, String titulo, String contenido, String fechaCreacion, String gymName) {
        this.id_salud      = id_salud;
        this.titulo        = titulo;
        this.contenido     = contenido;
        this.fechaCreacion = fechaCreacion;
        this.gymName       = gymName;
    }

    public String getId_salud() {
        return id_salud;
    }

    public void setId_salud(String id_salud) {
        this.id_salud = id_salud;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getGymName() { return gymName; }

    public void setGymName(String gymName) { this.gymName = gymName;  }

    public String getFecha() {return fechaCreacion; }

    public void setFecha(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
