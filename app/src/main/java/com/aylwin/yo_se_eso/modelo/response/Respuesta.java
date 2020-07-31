package com.aylwin.yo_se_eso.modelo.response;

public class Respuesta {
    private int mensajeCodigo;
    private String mensajeResultado;

    private int idRespuesta;
    private String nombre;
    private String fecha;
    private int idUsuario;
    private String nombreUsuario;
    private String rutaImagen;
    private int idPregunta;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(int idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public int getMensajeCodigo() {
        return mensajeCodigo;
    }

    public void setMensajeCodigo(int mensajeCodigo) {
        this.mensajeCodigo = mensajeCodigo;
    }

    public String getMensajeResultado() {
        return mensajeResultado;
    }

    public void setMensajeResultado(String mensajeResultado) {
        this.mensajeResultado = mensajeResultado;
    }
}
