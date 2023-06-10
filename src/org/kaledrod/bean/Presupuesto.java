/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaledrod.bean;

import java.time.LocalDate;
import javafx.scene.control.Button;

/**
 *
 * @author Kaled Rodriguez
 */
public class Presupuesto {

    private int codigoPresupuesto;
    private LocalDate fechaSolicitud;
    private double cantidadPresupuesto;
    private int codigoEmpresa;
    private Button eliminar;

    public Presupuesto() {
    }

    public Presupuesto(int codigoPresupuesto, LocalDate fechaSolicitud, double cantidadPresupuesto, int codigoEmpresa) {
        this.codigoPresupuesto = codigoPresupuesto;
        this.fechaSolicitud = fechaSolicitud;
        this.cantidadPresupuesto = cantidadPresupuesto;
        this.codigoEmpresa = codigoEmpresa;
        this.eliminar = new Button("Borrar");
    }

    public int getCodigoPresupuesto() {
        return codigoPresupuesto;
    }

    public void setCodigoPresupuesto(int codigoPresupuesto) {
        this.codigoPresupuesto = codigoPresupuesto;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public double getCantidadPresupuesto() {
        return cantidadPresupuesto;
    }

    public void setCantidadPresupuesto(double cantidadPresupuesto) {
        this.cantidadPresupuesto = cantidadPresupuesto;
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
