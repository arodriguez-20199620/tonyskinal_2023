/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaledrod.bean;

import javafx.scene.control.Button;

public class TipoPlato {

    private int codigoTipoPlato;
    private String descripcionTipo;
    private Button eliminar;

    public TipoPlato() {
    }

    public TipoPlato(int codigoTipoPlato, String descripcionTipo) {
        this.codigoTipoPlato = codigoTipoPlato;
        this.descripcionTipo = descripcionTipo;
        this.eliminar = new Button("Borrar");
    }

    public int getCodigoTipoPlato() {
        return codigoTipoPlato;
    }

    public void setCodigoTipoPlato(int codigoTipoPlato) {
        this.codigoTipoPlato = codigoTipoPlato;
    }

    public String getDescripcionTipo() {
        return descripcionTipo;
    }

    public void setDescripcionTipo(String descripcionTipo) {
        this.descripcionTipo = descripcionTipo;
    }

    public Button getEliminar() {
        return eliminar;
    }

    public void setEliminar(Button eliminar) {
        this.eliminar = eliminar;
    }

    @Override
    public String toString() {
        return codigoTipoPlato + " | " + descripcionTipo;
    }
}
