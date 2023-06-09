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
import org.kaledrod.bean.Producto;
import org.kaledrod.bean.Productos_has_plato;
import org.kaledrod.db.Conexion;
import org.kaledrod.main.Principal;

/**
 *
 * @author informatica
 */
public class Productos_has_platoController implements Initializable {

    Alert alerta = new Alert(Alert.AlertType.WARNING);
    private ObservableList<Plato> listaPlatos;
    private ObservableList<Producto> listaProductos;
    private ObservableList<Productos_has_plato> listaProductos_has_platoController;
    private Principal escenarioPrincipal;

    @FXML
    private Button btnConfirmar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnNuevo;
    @FXML
    private TextField txtProdCodProducto;
    @FXML
    private ComboBox cmbCodPlato;
    @FXML
    private ComboBox cmbCodProducto;
    @FXML
    private TableView tblHasProductos;
    @FXML
    private TableColumn colProdCodProducto;
    @FXML
    private TableColumn colCodPlato;
    @FXML
    private TableColumn colCodProducto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodPlato.setItems(getPlato());
        cmbCodProducto.setItems(getProducto());
        desactivarControles();
    }

    public void cargarDatos() {
        tblHasProductos.setItems(getProductos_has_plato());
        colProdCodProducto.setCellValueFactory(new PropertyValueFactory<Productos_has_plato, Integer>("Productos_codigoProducto"));
        colCodPlato.setCellValueFactory(new PropertyValueFactory<Productos_has_plato, Integer>("codigoPlato"));
        colCodProducto.setCellValueFactory(new PropertyValueFactory<Productos_has_plato, Integer>("codigoProducto"));
    }

    public void seleccionarElemento() {
        if (tblHasProductos.getSelectionModel().getSelectedItem() != null) {
            txtProdCodProducto.setText(String.valueOf(((Productos_has_plato) tblHasProductos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
            cmbCodPlato.getSelectionModel().select(buscarPlato(((Productos_has_plato) tblHasProductos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            cmbCodProducto.getSelectionModel().select(buscarProducto(((Productos_has_plato) tblHasProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        } else {
            alerta.setTitle("Advertencia");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecciona un campo que tenga datos");
            alerta.showAndWait();
        }
    }

    public ObservableList<Productos_has_plato> getProductos_has_plato() {
        ArrayList<Productos_has_plato> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos_has_Platos();");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Productos_has_plato(
                        resultado.getInt("Productos_codigoProducto"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoProducto")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProductos_has_platoController = FXCollections.observableArrayList(lista);
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

    public ObservableList<Producto> getProducto() {
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"),
                        resultado.getInt("cantidad")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableArrayList(lista);
    }

    public Producto buscarProducto(int codigoProducto) {
        Producto resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarProducto(?)");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Producto(registro.getInt("codigoProducto"),
                        registro.getString("nombreProducto"),
                        registro.getInt("cantidad"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void nuevo() {
        tblHasProductos.setOnMouseClicked(null);
        limpiarControles();
        activarControles();
        deseleccionar();
        btnCancelar.setOnAction(e -> {
            limpiarControles();
            desactivarControles();
        });
        btnConfirmar.setOnAction(e -> {
            String ProdCodProducto = txtProdCodProducto.getText().trim();
            if (!ProdCodProducto.isEmpty() && (cmbCodPlato.getSelectionModel().getSelectedItem() != null) && (cmbCodProducto.getSelectionModel().getSelectedItem() != null)) {
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
        Productos_has_plato registro = new Productos_has_plato();
        registro.setProductos_codigoProducto(Integer.parseInt(txtProdCodProducto.getText()));
        registro.setCodigoPlato(((Plato) cmbCodPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoProducto(((Producto) cmbCodProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProducto_has_Plato(?,?,?)}");
            procedimiento.setInt(1, registro.getProductos_codigoProducto());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoProducto());
            procedimiento.execute();
            listaProductos_has_platoController.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deseleccionar() {
        tblHasProductos.getSelectionModel().clearSelection();
    }

    public void desactivarControles() {
        btnConfirmar.setVisible(false);
        btnCancelar.setVisible(false);
        txtProdCodProducto.setEditable(false);
        cmbCodPlato.setDisable(true);
        cmbCodProducto.setDisable(true);
    }

    public void activarControles() {
        btnConfirmar.setVisible(true);
        btnCancelar.setVisible(true);
        txtProdCodProducto.setEditable(true);
        cmbCodPlato.setDisable(false);
        cmbCodProducto.setDisable(false);
    }

    public void limpiarControles() {
        txtProdCodProducto.clear();
        cmbCodPlato.setValue(null);
        cmbCodProducto.setValue(null);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }

    public void buttonAccionCerrar() {
        System.exit(0);
    }

    public void buttonAccionMinimizar() {
        escenarioPrincipal.minimizarVentana();
    }

}
