package com.bitgymup.gymup.users;

public class Salud {
    private String id_salud, titulo, contenido, fechaCreacion;

    public Salud() {
    }

    public Salud(String id_salud, String titulo, String contenido, String fechaCreacion) {
        this.id_salud = id_salud;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaCreacion = fechaCreacion;
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

    public String getFecha() {return fechaCreacion; }

    public void setFecha(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
