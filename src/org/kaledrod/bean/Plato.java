/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaledrod.bean;

import javafx.scene.control.Button;

public class Plato {

    private int codigoPlato;
    private int cantidad;
    private String nombrePlato;
    private String descripcion;
    private Double precioPlato;
    private int codigoTipoPlato;
    private Button eliminar;

    public Plato() {
    }

    public Plato(int codigoPlato, int cantidad, String nombrePlato, String descripcion, Double precioPlato, int codigoTipoPlato) {
        this.codigoPlato = codigoPlato;
        this.cantidad = cantidad;
        this.nombrePlato = nombrePlato;
        this.descripcion = descripcion;
        this.precioPlato = precioPlato;
        this.codigoTipoPlato = codigoTipoPlato;
        this.eliminar = new Button("Borrar");
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecioPlato() {
        return precioPlato;
    }

    public void setPrecioPlato(Double precioPlato) {
        this.precioPlato = precioPlato;
    }

    public int getCodigoTipoPlato() {
        return codigoTipoPlato;
    }

    public void setCodigoTipoPlato(int codigoTipoPlato) {
        this.codigoTipoPlato = codigoTipoPlato;
    }

    public Button getEliminar() {
        return eliminar;
    }

    public void setEliminar(Button eliminar) {
        this.eliminar = eliminar;
    }

}
