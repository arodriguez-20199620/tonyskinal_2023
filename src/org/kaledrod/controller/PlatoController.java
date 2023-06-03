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
import javafx.event.ActionEvent;
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
import org.kaledrod.bean.TipoPlato;
import org.kaledrod.db.Conexion;
import org.kaledrod.main.Principal;

public class PlatoController implements Initializable {

    Alert alerta = new Alert(Alert.AlertType.WARNING);
    private ObservableList<Plato> listaPlato;
    private ObservableList<TipoPlato> listaTipoPlato;
    private Principal escenarioPrincipal;

    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private Button btnConfirmar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TableView tblPlatos;
    @FXML
    private TableColumn colCodPlato;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colNombrePlato;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colPrecioPlato;
    @FXML
    private TableColumn colCodTipoPlato;
    @FXML
    private TableColumn colAccion;
    @FXML
    private TextField txtCodigoPlato;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtNombrePlato;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtPrecioPlato;
    @FXML
    private ComboBox cmbCodigoTipoPlato;

    @Override

    public void initialize(URL location, ResourceBundle resources) {
        desactivarControles();
        cargarDatos();
        asiganarBtn();
        
    }

    public void cargarDatos() {
        tblPlatos.setItems(getPlato());
        colCodPlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato, String>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("nombrePlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Plato, String>("descripcion"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("precioPlato"));
        colCodTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("codigoTipoPlato"));
        colAccion.setCellValueFactory(new PropertyValueFactory<Plato, String>("eliminar"));
        asiganarBtn();
    }

    public ObservableList<Plato> getPlato() {
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos");
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
        return listaPlato = FXCollections.observableArrayList(lista);
    }

    public TipoPlato buscarTipoPlato(int codigoTipoPlato) {
        TipoPlato registro = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoPlato(?);");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                registro = new TipoPlato(resultado.getInt("codigoTipoPlato"),
                        resultado.getString("descripcionTipo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }

    public void seleccionarElementos() {
        if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
            txtCodigoPlato.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            txtCantidad.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
            txtNombrePlato.setText(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
            txtDescripcion.setText(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getDescripcion());
            txtPrecioPlato.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
            cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        } else {
            alerta.setTitle("Advertencia");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecciona un campo que tenga datos");
            alerta.showAndWait();
        }
    }

    public void nuevo() {

    }

    public void guardar() {

    }

    public void eliminar(ActionEvent event) {

    }

    public void editar() {

    }

    public void actualizar() {

    }

    public void reporte() {

    }

    public void asiganarBtn() {
        for (int i = 0; i < listaPlato.size(); i++) {
            listaPlato.get(i).getEliminar().setOnAction(this::eliminar);
            listaPlato.get(i).getEliminar().getStylesheets().add("/org/kaledrod/resourse/TonysKinal.css");
            listaPlato.get(i).getEliminar().getStyleClass().add("eliminar");
        }
    }

    public void desactivarControles() {
        btnConfirmar.setVisible(false);
        btnCancelar.setVisible(false);
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcion.setEditable(false);
        txtPrecioPlato.setEditable(false);
        cmbCodigoTipoPlato.setDisable(true);
    }

    public void activarControles() {
        btnConfirmar.setVisible(true);
        btnCancelar.setVisible(true);
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcion.setEditable(true);
        txtPrecioPlato.setEditable(true);
        cmbCodigoTipoPlato.setDisable(false);
    }

    public void limpiarControles() {
        txtCodigoPlato.clear();
        txtCantidad.clear();
        txtNombrePlato.clear();
        txtDescripcion.clear();
        txtPrecioPlato.clear();
        cmbCodigoTipoPlato.setValue(null);
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
