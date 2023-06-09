/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaledrod.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kaledrod.main.Principal;
import org.kaledrod.controller.PresupuestoController;
import org.kaledrod.report.GenerarReporte;

/**
 *
 * @author informatica
 */
public class MenuPrincipalController implements Initializable {

    private Principal escenarioPrincipal;
    private PresupuestoController prespuestosC;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaProgramador() {
        escenarioPrincipal.ventanaProgramador();
    }

    public void ventanaEmpresa() {
        escenarioPrincipal.ventanaEmpresa();
    }

    public void ventanaProducto() {
        escenarioPrincipal.ventanaProducto();
    }

    public void ventanaTipoEmpleado() {
        escenarioPrincipal.ventanaTipoEmpleado();
    }

    public void ventanaTipoPlato() {
        escenarioPrincipal.ventanaTipoPlato();
    }

    public void ventanEmpleado() {
        escenarioPrincipal.ventanaEmpleado();
    }

    public void ventanaPresupuesto() {
        escenarioPrincipal.ventanaPresupuesto();
    }

    public void ventanaPlato() {
        escenarioPrincipal.ventanaPlato();
    }

    public void ventanaServicios() {
        escenarioPrincipal.ventanaServicios();
    }

    public void generarReporete() {
        imprimirReporte();
    }

    public void ventanaLogin() {
        escenarioPrincipal.ventanaLogin();
    }

    public void ventanaUsuario() {
        escenarioPrincipal.ventanaUsuario();
    }

    public void ventanaProductos_has_platos() {
        escenarioPrincipal.ventanaProductos_has_Platos();
    }

    public void ventanaServicios_has_Platos() {
        escenarioPrincipal.ventanaServicios_has_Platos();
    }

    public void imprimirReporte() {
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        GenerarReporte.mostarReporte("ReporteGeneral.jasper", "Reporte general", parametros);
    }

    public void buttonAccionCerrar() {
        System.exit(0);
    }

    public void buttonAccionMinimizar() {
        escenarioPrincipal.minimizarVentana();
    }

}
