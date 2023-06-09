/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaledrod.bean;

/**
 *
 * @author Kaled Rodriguez
 */
public class Servicios_has_platos {

    private int Servicios_codigoServicio;
    private int codigoPlato;
    private int codigoServicio;

    public Servicios_has_platos() {
    }

    public Servicios_has_platos(int Servicios_codigoServicio, int codigoPlato, int codigoServicio) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
        this.codigoPlato = codigoPlato;
        this.codigoServicio = codigoServicio;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public int getServicios_codigoServicio() {
        return Servicios_codigoServicio;
    }

    public void setServicios_codigoServicio(int Servicios_codigoServicio) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }
    
}
