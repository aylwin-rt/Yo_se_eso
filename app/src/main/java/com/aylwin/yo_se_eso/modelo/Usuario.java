package com.aylwin.yo_se_eso.modelo;

public class Usuario {

    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String email;
    private String contrasenya;
    //private String idUniversidad;
    //public String role;

    private String token;


    //RESPUESTA
    private int mensajeCodigo;
    private String mensajeResultado;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
