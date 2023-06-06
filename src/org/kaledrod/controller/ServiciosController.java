/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaledrod.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.kaledrod.bean.Empresa;
import org.kaledrod.bean.Servicios;
import com.jfoenix.controls.JFXTimePicker;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.kaledrod.db.Conexion;
import org.kaledrod.main.Principal;

/**
 *
 * @author Kaled Rodriguez
 */
public class ServiciosController implements Initializable {
    Alert alerta = new Alert(Alert.AlertType.WARNING);
    private Principal escenarioPrincipal;
    private ObservableList<Servicios> listaServicio;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    private JFXTimePicker hora;
//  Declaracion de Botones
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
// Declaracion de texfield`s
    @FXML
    private TextField txtCodServicios;

    @FXML
    private TextField txtTipoServicio;

    @FXML
    private TextField txtLugarServicio;

    @FXML
    private TextField txtTelefonoContacto;
    // declaracion de combox
    @FXML
    private ComboBox cmbCodEmpresa;
    // Declaracion de grindPane para las fecha y hora
    @FXML
    private GridPane grpFechaHora;
    // Declaracion de tblView
    @FXML
    private TableView tblServicios;
    @FXML
    private TableColumn colCodServicios;
    @FXML
    private TableColumn colFechaServicio;
    @FXML
    private TableColumn colTipoServicio;
    @FXML
    private TableColumn colHoraServicio;
    @FXML
    private TableColumn colLugarServicio;
    @FXML
    private TableColumn colTelefonoContacto;
    @FXML
    private TableColumn colCodEmpresa;
    @FXML
    private TableColumn colAccion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        asignarBoton();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/kaledrod/resourse/TonysKinal.css");
        fecha.setAlignment(Pos.CENTER);
        hora = new JFXTimePicker();
        hora.getStylesheets().add("/org/kaledrod/resourse/TonysKinal.css");
        grpFechaHora.add(fecha, 3, 0);
        grpFechaHora.add(hora, 3, 1);
        cmbCodEmpresa.setItems(getEmpresa());
        desactivarControles();
    }

    public void cargarDatos() {
        tblServicios.setItems(getServicios());
        colCodServicios.setCellValueFactory(new PropertyValueFactory<Servicios, String>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicios, String>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicios, String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicios, String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicios, String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicios, String>("telefonoContacto"));
        colCodEmpresa.setCellValueFactory(new PropertyValueFactory<Servicios, String>("codigoEmpresa"));
        colAccion.setCellValueFactory(new PropertyValueFactory<Servicios, String>("eliminar"));
        asignarBoton();
    }

    public ObservableList<Servicios> getServicios() {
        ArrayList<Servicios> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicios(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"),
                        resultado.getString("tipoServicio"),
                        resultado.getTime("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista);
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

    public void seleccionarElemento() {
        if (tblServicios.getSelectionModel().getSelectedItem() != null) {
            txtCodServicios.setText(String.valueOf(((Servicios) tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            fecha.selectedDateProperty().set(((Servicios) tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
        } else {
            alerta.setTitle("Advertencia");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecciona un campo que tenga datos");
            alerta.showAndWait();
        }
    }

    public void nuevo() {
        tblServicios.setOnMouseClicked(null);
        limpiarControles();
        activarControles();
        deseleccionar();
        btnCancelar.setOnAction(e -> {
            limpiarControles();
            desactivarControles();
            activarTbl();
        });
        btnConfirmar.setOnAction(e -> {
            String codServicios = txtCodServicios.getText().trim();
            String tipoServicio = txtTipoServicio.getText().trim();
            String lugarServicio = txtTipoServicio.getText().trim();
            String telefonoContacto = txtTelefonoContacto.getText().trim();
            if (!codServicios.isEmpty() && !tipoServicio.isEmpty() && !lugarServicio.isEmpty() && !telefonoContacto.isEmpty()&&(cmbCodEmpresa.getSelectionModel().getSelectedItem()!= null)) {
                guardar();
                cargarDatos();
                limpiarControles();
                desactivarControles();
                activarTbl();
            } else {
                limpiarControles();
                desactivarControles();
                activarTbl();
                alerta.setTitle("Advertencia");
                alerta.setHeaderText(null);
                alerta.setContentText("Por favor, complete todos los campos.");
                alerta.showAndWait();
            }
        });
    }

    public void guardar() {
        Servicios registro = new Servicios();
        registro.setFechaServicio(fecha.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(Time.valueOf(hora.getValue()));
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEmpresa(((Empresa) cmbCodEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicio(?,?,?,?,?,?)");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(2, registro.getTipoServicio());
            procedimiento.setTime(3, registro.getHoraServicio());
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaServicio.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void eliminar(ActionEvent event) {
        for (int i = 0; i < listaServicio.size(); i++) {
            if (event.getSource() == listaServicio.get(i).getEliminar()) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmación");
                confirmationAlert.setHeaderText("¿Está seguro de eliminar el registro?");
                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicio(?)");
                        procedimiento.setInt(1, listaServicio.get(i).getCodigoServicio());
                        procedimiento.execute();
                        listaServicio.remove(tblServicios.getItems().get(i));
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

    public void formatoNumero() {
        // Crear un TextFormatter que solo permita dígitos
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) { // Verificar si solo contiene dígitos
                return change; // Aceptar el cambio
            } else {
                alerta.setTitle("Advertencia");
                alerta.setHeaderText(null);
                alerta.setContentText("Debes ingresar numeros");
                alerta.showAndWait();

                return null; // Rechazar el cambio
            }
        });
        txtTelefonoContacto.setTextFormatter(textFormatter);
    }

    public void deseleccionar() {
        tblServicios.getSelectionModel().clearSelection();
    }

    public void activarTbl() {
        tblServicios.setOnMouseClicked(a -> {
            seleccionarElemento();
        });
    }

    public void asignarBoton() {
        for (int i = 0; i < listaServicio.size(); i++) {
            listaServicio.get(i).getEliminar().setOnAction(this::eliminar);
            listaServicio.get(i).getEliminar().getStylesheets().add("/org/kaledrod/resourse/TonysKinal.css");
            listaServicio.get(i).getEliminar().getStyleClass().add("eliminar");
        }
    }

    public void desactivarControles() {
        btnCancelar.setVisible(false);
        btnConfirmar.setVisible(false);
        txtCodServicios.setEditable(false);
        fecha.setDisable(true);
        txtTipoServicio.setEditable(false);
        hora.setDisable(true);
        txtLugarServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        cmbCodEmpresa.setDisable(true);
    }

    public void activarControles() {
        btnCancelar.setVisible(true);
        btnConfirmar.setVisible(true);
        txtCodServicios.setEditable(false);
        fecha.setDisable(false);
        txtTipoServicio.setEditable(true);
        hora.setDisable(false);
        txtLugarServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        cmbCodEmpresa.setDisable(false);
    }

    public void limpiarControles() {
        txtCodServicios.clear();
        fecha.setSelectedDate(null);
        txtTipoServicio.clear();
        hora.setValue(null);
        txtLugarServicio.clear();
        txtTelefonoContacto.clear();
        cmbCodEmpresa.setDisable(true);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void regresar() {
        escenarioPrincipal.menuPrincipal();
    }

    public void buttonAccionCerrar() {
        System.exit(0);
    }

    public void buttonAccionMinimizar() {
        escenarioPrincipal.minimizarVentana();
    }

}
