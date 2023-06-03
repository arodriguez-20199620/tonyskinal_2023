/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaledrod.bean;

import javafx.scene.control.Button;

public class TipoEmpleado {

    private int codigoTipoEmpleado;
    private String descripcion;
    private Button eliminar;

    public TipoEmpleado() {
    }

    public TipoEmpleado(int codigoTipoEmpleado, String descripcion) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
        this.descripcion = descripcion;
        this.eliminar = new Button("Borrar");
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCodigoTipoEmpleado() {
        return codigoTipoEmpleado;
    }

    public void setCodigoTipoEmpleado(int codigoTipoEmpleado) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
    }

    public Button getEliminar() {
        return eliminar;
    }

    public void setEliminar(Button eliminar) {
        this.eliminar = eliminar;
    }

    @Override
    public String toString() {
        return codigoTipoEmpleado + " | " + descripcion;
    }

}
