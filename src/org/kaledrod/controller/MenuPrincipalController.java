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
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
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

    @FXML
    private AnchorPane pane1;

    @FXML
    private AnchorPane pane2;

    @FXML
    private AnchorPane pane3;
    @FXML
    private Label num;

    public void transicion(double duration, Node node, double width) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), node);
        translateTransition.setByX(width);
        translateTransition.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transicion(0.5, pane2, 1000);
        transicion(0.5, pane3, 1000);
    }

    int show = 0;

    @FXML
    public void next(ActionEvent event) {
        if (show == 0) {
            transicion(0.5, pane2, -1000);
            show++;
            num.setText("2/3");
        } else if (show == 1) {
            transicion(0.5, pane3, -1000);
            show++;
            num.setText("3/3");
        }
    }

    @FXML
    public void back(ActionEvent event) {
        if (show == 1) {
            transicion(0.5, pane2, 1000);
            show--;
            num.setText("1/3");
        } else if (show == 2) {
            transicion(0.5, pane3, 1000);
            show--;
            num.setText("2/3");
        }
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
        escenarioPrincipal.ventanaProductos_has_platos();
    }

    public void ventanaServicios_has_platos() {
        escenarioPrincipal.ventanaServicios_has_platos();
    }

    public void ventanaServicios_has_empleados() {
        escenarioPrincipal.ventanaServicios_has_empleados();
    }

    public void cerrarSesion() {
        escenarioPrincipal.ventanaLogin();
    }

    public void imprimirReporte() {
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        GenerarReporte.mostarReporte("ReporteGeneral.jasper", "Reporte general", parametros);
    }

    public void cerrarAplicacion() {
        System.exit(0);
    }

    public void buttonAccionMinimizar() {
        escenarioPrincipal.minimizarVentana();
    }

}
