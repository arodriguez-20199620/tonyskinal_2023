/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaledrod.bean;

import java.time.LocalDate;
import javafx.scene.control.Button;
import java.sql.Time;

/**
 *
 * @author Kaled Rodriguez
 */
public class Servicios {

    private int codigoServicio;
    private LocalDate fechaServicio;
    private String tipoServicio;
    private Time horaServicio;
    private String lugarServicio;
    private String telefonoContacto;
    private int codigoEmpresa;
    private Button eliminar;

    public Servicios() {
    }

    public Servicios(int codigoServicio, LocalDate fechaServicio, String tipoServicio, Time horaServicio, String lugarServicio, String telefonoContacto, int codigoEmpresa) {
        this.codigoServicio = codigoServicio;
        this.fechaServicio = fechaServicio;
        this.tipoServicio = tipoServicio;
        this.horaServicio = horaServicio;
        this.lugarServicio = lugarServicio;
        this.telefonoContacto = telefonoContacto;
        this.codigoEmpresa = codigoEmpresa;
        this.eliminar = new Button("Borrar");
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public LocalDate getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(LocalDate fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Time getHoraServicio() {
        return horaServicio;
    }

    public void setHoraServicio(Time horaServicio) {
        this.horaServicio = horaServicio;
    }

    public String getLugarServicio() {
        return lugarServicio;
    }

    public void setLugarServicio(String lugarServicio) {
        this.lugarServicio = lugarServicio;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public Button getEliminar() {
        return eliminar;
    }

    public void setEliminar(Button eliminar) {
        this.eliminar = eliminar;
    }
    
}
