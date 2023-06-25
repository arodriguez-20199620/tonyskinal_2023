/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaledrod.controller;

import com.jfoenix.controls.JFXDatePicker;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.util.Locale;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.kaledrod.bean.Empresa;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javax.swing.JOptionPane;
import org.kaledrod.bean.Presupuesto;
import org.kaledrod.db.Conexion;
import org.kaledrod.main.Principal;
import java.sql.Date;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.kaledrod.report.GenerarReporte;

/**
 *
 * @author Kaled Rodriguez
 */
public class PresupuestoController implements Initializable {

    Alert alerta = new Alert(Alert.AlertType.WARNING);
    private Principal escenarioPrincipal;
    private ObservableList<Presupuesto> listaPresupuesto;
    private ObservableList<Empresa> listaEmpresa;

    @FXML
    private JFXDatePicker fecha;
    @FXML
    private TextField txtCodigoPresupuesto;
    @FXML
    private TextField txtCantidadPresupuesto;
    @FXML
    private GridPane grpFecha;
    @FXML
    private ComboBox cmbCodigoEmpresa;
    @FXML
    private TableView tblPresupuestos;
    @FXML
    private TableColumn colCodigoPresupuesto;
    @FXML
    private TableColumn colFechaSolicitud;
    @FXML
    private TableColumn colCantidadPresupuesto;
    @FXML
    private TableColumn colCodigoEmpresa;
    @FXML
    private TableColumn colAccion;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private Button btnConfirmar;
    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        asignarBoton();
        fecha = new JFXDatePicker();
        fecha.getStylesheets().add("/org/kaledrod/resourse/TonysKinal.css");
        fecha.getStyleClass().add("jfx-date-picker1");
        grpFecha.add(fecha, 3, 0);
        cmbCodigoEmpresa.setItems(getEmpresa());
        desactivarControles();

    }

    public void cargarDatos() {
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, String>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, String>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, String>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, String>("codigoEmpresa"));
        colAccion.setCellValueFactory(new PropertyValueFactory<Presupuesto, String>("eliminar"));
        asignarBoton();
    }

    public ObservableList<Presupuesto> getPresupuesto() {
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPresupuestos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                        resultado.getDate("fechaSolicitud").toLocalDate(),
                        resultado.getDouble("cantidadPresupuesto"),
                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPresupuesto = FXCollections.observableArrayList(lista);
    }

    public Empresa buscarEmpresa(int codigoEmpresa) {
        Empresa resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                        registro.getString("nombreEmpresa"),
                        registro.getString("direccion"),
                        registro.getString("telefono"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
            txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
            fecha.setValue(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
            txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
            cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        } else {
            alerta.setTitle("Advertencia");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecciona un campo que tenga datos");
            alerta.showAndWait();
        }
    }

    public void nuevo() {
        btnNuevo.setDisable(true);
        btnEditar.setDisable(true);
        btnReporte.setDisable(true);
        tblPresupuestos.setOnMouseClicked(null);
        limpiarControles();
        activarControles();
        deseleccionar();
        btnCancelar.setOnAction(e -> {
            limpiarControles();
            desactivarControles();
            activarTbl();
            btnNuevo.setDisable(false);
            btnEditar.setDisable(false);
            btnReporte.setDisable(false);
        });
        btnConfirmar.setOnAction(e -> {
            String cantidadPresupuesto = txtCantidadPresupuesto.getText().trim();
            if (!cantidadPresupuesto.isEmpty() && (cmbCodigoEmpresa.getSelectionModel().getSelectedItem() != null) && (fecha.getValue() != null)) {
                guardar();
                cargarDatos();
                limpiarControles();
                desactivarControles();
                activarTbl();
                btnNuevo.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
            } else {
                limpiarControles();
                desactivarControles();
                activarTbl();
                alerta.setTitle("Advertencia");
                alerta.setHeaderText(null);
                alerta.setContentText("Por favor, complete todos los campos.");
                alerta.showAndWait();
                btnNuevo.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
            }
        });
    }

    public void eliminar(ActionEvent event) {
        for (int i = 0; i < listaPresupuesto.size(); i++) {
            if (event.getSource() == listaPresupuesto.get(i).getEliminar()) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmación");
                confirmationAlert.setHeaderText("¿Está seguro de eliminar el registro?");
                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPresupuesto(?)");
                        procedimiento.setInt(1, listaPresupuesto.get(i).getCodigoPresupuesto());
                        procedimiento.execute();
                        listaPresupuesto.remove(tblPresupuestos.getItems().get(i));
                        deseleccionar();
                        limpiarControles();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    desactivarControles();
                    deseleccionar();
                    limpiarControles();
                }
            }
        }
    }

    public void editar() {
        if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
            btnNuevo.setDisable(true);
            btnEditar.setDisable(true);
            btnReporte.setDisable(true);
            activarControles();
            btnCancelar.setOnAction(e -> {
                limpiarControles();
                desactivarControles();
                deseleccionar();
                btnNuevo.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
            });
            btnConfirmar.setOnAction(e -> {
                actualizar();
                deseleccionar();
                limpiarControles();
                desactivarControles();
                cargarDatos();
                btnNuevo.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
            });
        } else {
            alerta.setTitle("Advertencia");
            alerta.setHeaderText(null);
            alerta.setContentText("Es importante que seleccione un elemento para poder editar");
            alerta.showAndWait();
            btnNuevo.setDisable(false);
            btnEditar.setDisable(false);
            btnReporte.setDisable(false);
        }
    }

    public void reporte() {

    }

    public void actualizar() {
        Presupuesto registro = (Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem();
        registro.setFechaSolicitud(fecha.getValue());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPresupuesto(?, ?, ?, ?)");
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, Date.valueOf(registro.getFechaSolicitud()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            procedimiento.setInt(4, registro.getCodigoEmpresa());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardar() {
        Presupuesto registro = new Presupuesto();
        registro.setFechaSolicitud(fecha.getValue());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPresupuesto(?,?,?)");
            procedimiento.setDate(1, Date.valueOf(registro.getFechaSolicitud()));
            procedimiento.setDouble(2, registro.getCantidadPresupuesto());
            procedimiento.setInt(3, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaPresupuesto.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generarReporte() {
        imprimirReporte();
    }

    public void imprimirReporte() {
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa", codEmpresa);
        parametros.put("RUTA_IMAGEN", EmpleadoController.class.getResource("/org/kaledrod/image/Logo2Forma2.png"));
        parametros.put("RUTA_IMAGEN", EmpleadoController.class.getResource("/org/kaledrod/report/SubReportePresupuesto.jrrml"));
        GenerarReporte.mostarReporte("ReportePresupuesto.jasper", "Reporte de Presupuesto", parametros);
    }

    public void deseleccionar() {
        tblPresupuestos.getSelectionModel().clearSelection();
        limpiarControles();
    }

    public void activarTbl() {
        tblPresupuestos.setOnMouseClicked(a -> {
            seleccionarElemento();
        });
    }

    public void asignarBoton() {
        for (int i = 0; i < listaPresupuesto.size(); i++) {
            listaPresupuesto.get(i).getEliminar().setOnAction(this::eliminar);
            listaPresupuesto.get(i).getEliminar().getStylesheets().add("/org/kaledrod/resourse/TonysKinal.css");
            listaPresupuesto.get(i).getEliminar().getStyleClass().add("eliminar");
        }
    }

    public void desactivarControles() {
        btnCancelar.setVisible(false);
        btnConfirmar.setVisible(false);
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(false);
        fecha.setDisable(true);
        cmbCodigoEmpresa.setDisable(true);
    }

    public void activarControles() {
        btnCancelar.setVisible(true);
        btnConfirmar.setVisible(true);
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(true);
        fecha.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }

    public void limpiarControles() {
        txtCodigoPresupuesto.clear();
        txtCantidadPresupuesto.clear();
        fecha.setValue(null);
        cmbCodigoEmpresa.setValue(null);
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
