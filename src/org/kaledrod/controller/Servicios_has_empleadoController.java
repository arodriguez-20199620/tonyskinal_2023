/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaledrod.controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.kaledrod.bean.Empleado;
import org.kaledrod.bean.Servicios;
import org.kaledrod.bean.Servicios_has_empleado;
import org.kaledrod.db.Conexion;
import org.kaledrod.main.Principal;

/**
 *
 * @author Kaled Rodriguez
 */
public class Servicios_has_empleadoController implements Initializable {

    Alert alerta = new Alert(Alert.AlertType.WARNING);
    private ObservableList<Servicios> listarServicios;
    private ObservableList<Empleado> listarEmpleados;
    private ObservableList<Servicios_has_empleado> listaServicios_has_platos;

    private Principal escenarioPrincipal;

    @FXML
    private GridPane grpEvento;

    @FXML
    private TextField txServiciosCodServicios;

    @FXML
    private TextField txtLugarEvento;

    @FXML
    private ComboBox cmbCodServicios;

    @FXML
    private ComboBox cmbCodEmpleado;

    @FXML
    private TableView tblHasEmpleados;

    @FXML
    private TableColumn colServiciosCodServicios;

    @FXML
    private TableColumn colCodServicios;

    @FXML
    private TableColumn colCodEmpleado;

    @FXML
    private TableColumn colFechaEvento;

    @FXML
    private TableColumn colHoraEvento;

    @FXML
    private TableColumn colLugarEvento;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnConfirmar;

    @FXML
    private JFXDatePicker fecha;

    @FXML
    private JFXTimePicker hora;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new JFXDatePicker();
        fecha.getStylesheets().add("/org/kaledrod/resourse/TonysKinal.css");
        fecha.getStyleClass().add("jfx-date-picker2");
        hora = new JFXTimePicker();
        hora.getStylesheets().add("/org/kaledrod/resourse/TonysKinal.css");
        hora.getStyleClass().add("jfx-time-picker2");
        grpEvento.add(fecha, 0, 1);
        grpEvento.add(hora, 1, 1);
        cmbCodServicios.setItems(getServicios());
        cmbCodEmpleado.setItems(getEmpleado());
        desactivarControles();
    }

    public void cargarDatos() {
        tblHasEmpleados.setItems(getServicios_has_Empleados());
        colServiciosCodServicios.setCellValueFactory(new PropertyValueFactory<Servicios_has_empleado, String>("Servicios_codigoServicio"));
        colCodServicios.setCellValueFactory(new PropertyValueFactory<Servicios_has_empleado, String>("codigoServicio"));
        colCodEmpleado.setCellValueFactory(new PropertyValueFactory<Servicios_has_empleado, String>("codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_empleado, String>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_empleado, String>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_empleado, String>("lugarEvento"));
    }

    public ObservableList<Servicios_has_empleado> getServicios_has_Empleados() {
        ArrayList<Servicios_has_empleado> lista = new ArrayList<Servicios_has_empleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Empleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicios_has_empleado(resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoServicio"),
                        resultado.getInt("codigoEmpleado"),
                        resultado.getDate("fechaEvento").toLocalDate(),
                        resultado.getTime("horaEvento"),
                        resultado.getString("lugarEvento")));
            }
        } catch (Exception e) {
        }
        return listaServicios_has_platos = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Servicios> getServicios() {
        ArrayList<Servicios> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicios(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio").toLocalDate(),
                        resultado.getString("tipoServicio"),
                        resultado.getTime("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarServicios = FXCollections.observableArrayList(lista);
    }

    public Servicios buscarServicio(int codigoServicio) {
        Servicios resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Servicios(registro.getInt("codigoServicio"),
                        registro.getDate("fechaServicio").toLocalDate(),
                        registro.getString("tipoServicio"),
                        registro.getTime("horaServicio"),
                        registro.getString("lugarServicio"),
                        registro.getString("telefonoContacto"),
                        registro.getInt("codigoEmpresa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public ObservableList<Empleado> getEmpleado() {
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarEmpleados = FXCollections.observableArrayList(lista);
    }

    public Empleado buscarEmpleado(int codigoEmpleado) {
        Empleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleado(?)}");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Empleado(registro.getInt("codigoEmpleado"),
                        registro.getInt("numeroEmpleado"),
                        registro.getString("apellidosEmpleado"),
                        registro.getString("nombresEmpleado"),
                        registro.getString("direccionEmpleado"),
                        registro.getString("telefonoContacto"),
                        registro.getString("gradoCocinero"),
                        registro.getInt("codigoTipoEmpleado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void seleccionarElementos() {
        if (tblHasEmpleados.getSelectionModel().getSelectedItem() != null) {
            txServiciosCodServicios.setText(String.valueOf(((Servicios_has_empleado) tblHasEmpleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
            cmbCodServicios.getSelectionModel().select(buscarServicio(((Servicios_has_empleado) tblHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            cmbCodEmpleado.getSelectionModel().select(buscarEmpleado(((Servicios_has_empleado) tblHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            fecha.setValue(((Servicios_has_empleado) tblHasEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
            hora.setValue(((Servicios_has_empleado) tblHasEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento().toLocalTime());
            txtLugarEvento.setText(((Servicios_has_empleado) tblHasEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento());
        } else {
            alerta.setTitle("Advertencia");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecciona un campo que tenga datos");
            alerta.showAndWait();
        }
    }

    public void nuevo() {
        tblHasEmpleados.setOnMouseClicked(null);
        limpiarControles();
        activarControles();
        deseleccionar();
        btnCancelar.setOnAction(e -> {
            limpiarControles();
            desactivarControles();
        });
        btnConfirmar.setOnAction(e -> {
            String serviciosCodServicios = txServiciosCodServicios.getText().trim();
            String lugarServicios = txtLugarEvento.getText().trim();
            if (!serviciosCodServicios.isEmpty() && (cmbCodServicios.getSelectionModel().getSelectedItem() != null)
                    && (cmbCodServicios.getSelectionModel().getSelectedItem() != null) && !serviciosCodServicios.isEmpty()
                    && (fecha.getValue() != null) && (hora.getValue() != null)) {
                guardar();
                cargarDatos();
                limpiarControles();
                desactivarControles();
            } else {
                limpiarControles();
                desactivarControles();
                alerta.setTitle("Advertencia");
                alerta.setHeaderText(null);
                alerta.setContentText("Por favor, complete todos los campos.");
                alerta.showAndWait();
            }
        });
    }

    public void guardar() {
        Servicios_has_empleado registro = new Servicios_has_empleado();
        registro.setServicios_codigoServicio(Integer.parseInt(txServiciosCodServicios.getText()));
        registro.setCodigoServicio(((Servicios) cmbCodServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleado) cmbCodEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setFechaEvento(fecha.getValue());
        registro.setHoraEvento(Time.valueOf(hora.getValue()));
        registro.setLugarEvento(txtLugarEvento.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicio_has_Empleado(?,?,?,?,?,?)");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoServicio());
            procedimiento.setInt(3, registro.getCodigoEmpleado());
            procedimiento.setDate(4, Date.valueOf(registro.getFechaEvento()));
            procedimiento.setTime(5, registro.getHoraEvento());
            procedimiento.setString(6, registro.getLugarEvento());
            procedimiento.execute();
            listaServicios_has_platos.add(registro);
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deseleccionar() {
        tblHasEmpleados.getSelectionModel().clearSelection();
    }

    public void desactivarControles() {
        btnConfirmar.setVisible(false);
        btnCancelar.setVisible(false);
        txServiciosCodServicios.setEditable(false);
        cmbCodServicios.setDisable(true);
        cmbCodServicios.setDisable(true);
        fecha.setDisable(true);
        hora.setDisable(true);
        txtLugarEvento.setEditable(false);
    }

    public void activarControles() {
        btnConfirmar.setVisible(true);
        btnCancelar.setVisible(true);
        txServiciosCodServicios.setEditable(true);
        cmbCodServicios.setDisable(false);
        cmbCodServicios.setDisable(false);
        fecha.setDisable(false);
        hora.setDisable(false);
        txtLugarEvento.setEditable(true);
    }

    public void limpiarControles() {
        txServiciosCodServicios.clear();
        cmbCodServicios.setValue(null);
        cmbCodServicios.setValue(null);
        fecha.setValue(null);
        hora.setValue(null);
        txtLugarEvento.clear();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    public void buttonAccionCerrar() {
        System.exit(0);
    }

    public void buttonAccionMinimizar() {
        escenarioPrincipal.minimizarVentana();
    }
}
