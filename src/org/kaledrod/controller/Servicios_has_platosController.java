/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaledrod.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import org.kaledrod.bean.Plato;
import org.kaledrod.bean.Servicios;
import org.kaledrod.bean.Servicios_has_platos;
import org.kaledrod.db.Conexion;
import org.kaledrod.main.Principal;

/**
 *
 * @author Kaled Rodriguez
 */
public class Servicios_has_platosController implements Initializable {

    Alert alerta = new Alert(Alert.AlertType.WARNING);
    private ObservableList<Plato> listaPlatos;
    private ObservableList<Servicios> listarServicios;
    private ObservableList<Servicios_has_platos> listaServicios_has_platos;
    private Principal escenarioPrincipal;

    @FXML
    private Button btnConfirmar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnNuevo;
    @FXML
    private TextField txtServicioCodPlato;
    @FXML
    private ComboBox cmbCodPlato;
    @FXML
    private ComboBox cmbCodServicio;
    @FXML
    private TableView tblHasServvicios;
    @FXML
    private TableColumn colServiciosCodPlato;
    @FXML
    private TableColumn colCodPlato;
    @FXML
    private TableColumn colCodServicio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodPlato.setItems(getPlato());
        cmbCodServicio.setItems(getServicios());
        desactivarControles();
    }

    public void cargarDatos() {
        tblHasServvicios.setItems(getServicioss_has_plato());
        colServiciosCodPlato.setCellValueFactory(new PropertyValueFactory<Servicios_has_platos, Integer>("Servicios_codigoServicio"));
        colCodPlato.setCellValueFactory(new PropertyValueFactory<Servicios_has_platos, Integer>("codigoPlato"));
        colCodServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_platos, Integer>("codigoServicio"));
    }

    public void seleccionarElemento() {
        if (tblHasServvicios.getSelectionModel().getSelectedItem() != null) {
            txtServicioCodPlato.setText(String.valueOf(((Servicios_has_platos) tblHasServvicios.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
            cmbCodPlato.getSelectionModel().select(buscarPlato(((Servicios_has_platos) tblHasServvicios.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            cmbCodServicio.getSelectionModel().select(buscarServicios(((Servicios_has_platos) tblHasServvicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        } else {
            alerta.setTitle("Advertencia");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecciona un campo que tenga datos");
            alerta.showAndWait();
        }
    }

    public ObservableList<Servicios_has_platos> getServicioss_has_plato() {
        ArrayList<Servicios_has_platos> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios_has_Platos();");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicios_has_platos(
                        resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoServicio")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicios_has_platos = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Plato> getPlato() {
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcion"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlatos = FXCollections.observableArrayList(lista);
    }

    public Plato buscarPlato(int codigoPlato) {
        Plato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlato(?)}");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Plato(registro.getInt("codigoPlato"),
                        registro.getInt("cantidad"),
                        registro.getString("nombrePlato"),
                        registro.getString("descripcion"),
                        registro.getDouble("precioPlato"),
                        registro.getInt("codigoTipoPlato"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
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

    public Servicios buscarServicios(int codigoServicios) {
        Servicios registro = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarServicio(?)");
            procedimiento.setInt(1, codigoServicios);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                registro = new Servicios(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio").toLocalDate(),
                        resultado.getString("tipoServicio"),
                        resultado.getTime("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }

    public void nuevo() {
        tblHasServvicios.setOnMouseClicked(null);
        limpiarControles();
        activarControles();
        deseleccionar();
        btnCancelar.setOnAction(e -> {
            limpiarControles();
            desactivarControles();
        });
        btnConfirmar.setOnAction(e -> {
            String ProdCodProducto = txtServicioCodPlato.getText().trim();
            if (!ProdCodProducto.isEmpty() && (cmbCodPlato.getSelectionModel().getSelectedItem() != null) && (cmbCodServicio.getSelectionModel().getSelectedItem() != null)) {
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
        Servicios_has_platos registro = new Servicios_has_platos();
        registro.setServicios_codigoServicio(Integer.parseInt(txtServicioCodPlato.getText()));
        registro.setCodigoPlato(((Plato) cmbCodPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoServicio(((Servicios) cmbCodServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio_has_Plato(?,?,?)}");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoServicio());
            procedimiento.execute();
            listaServicios_has_platos.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deseleccionar() {
        tblHasServvicios.getSelectionModel().clearSelection();
    }

    public void desactivarControles() {
        btnConfirmar.setVisible(false);
        btnCancelar.setVisible(false);
        txtServicioCodPlato.setEditable(false);
        cmbCodPlato.setDisable(true);
        cmbCodServicio.setDisable(true);
    }

    public void activarControles() {
        btnConfirmar.setVisible(true);
        btnCancelar.setVisible(true);
        txtServicioCodPlato.setEditable(true);
        cmbCodPlato.setDisable(false);
        cmbCodServicio.setDisable(false);
    }

    public void limpiarControles() {
        txtServicioCodPlato.clear();
        cmbCodPlato.setValue(null);
        cmbCodServicio.setValue(null);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void buttonAccionCerrar() {
        System.exit(0);
    }

    public void buttonAccionMinimizar() {
        escenarioPrincipal.minimizarVentana();
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
}
