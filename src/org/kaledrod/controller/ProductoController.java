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
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.kaledrod.bean.Producto;
import org.kaledrod.db.Conexion;

import org.kaledrod.main.Principal;

/**
 *
 * @author Kaled Rodriguez
 */
public class ProductoController implements Initializable {

    Alert alerta = new Alert(Alert.AlertType.WARNING);
    private Principal escenarioPrincipal;
    private ObservableList<Producto> listaProducto;

//  Declaracion de Botoncitos    
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

//  Declaracion de texfields
    @FXML
    private TextField txtCodProducto;
    @FXML
    private TextField txtNombreProducto;
    @FXML
    private TextField txtCantidad;
// Declaracion de tablita y columnas
    @FXML
    private TableView tblProductos;
    @FXML
    private TableColumn colCodProducto;
    @FXML
    private TableColumn colNombreProducto;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colAccion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        desactivarControles();
        cargarDatos();
        asignarBoton();
        formatoNumero(txtCantidad); 
    }
//  Cargar los datos en ls columnas de la tabla

    public void cargarDatos() {
        tblProductos.setItems(getProductos());
        colCodProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, String>("cantidad"));
        colAccion.setCellValueFactory(new PropertyValueFactory<Producto, String>("eliminar"));
        asignarBoton();
    }

// Obtener los datos desde la base de datos
    public ObservableList<Producto> getProductos() {
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"),
                        resultado.getInt("cantidad")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista);
    }

//  Setear los valores de la tabla en cada texfield correspondiente   
    public void seleccionarElemento() {
        if (tblProductos.getSelectionModel().getSelectedItem() != null) {
            txtCodProducto.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            txtNombreProducto.setText(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
            txtCantidad.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
        } else {
            alerta.setTitle("Advertencia");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecciona un campo que tenga datos");
            alerta.showAndWait();
        }
    }

//  Cambio de botones de cancelar y guardar   
    public void nuevo() {
        btnNuevo.setDisable(true);
        btnEditar.setDisable(true);
        btnReporte.setDisable(true);
        tblProductos.setOnMouseClicked(null);
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
            String nombreProducto = txtNombreProducto.getText().trim();
            String cantidad = txtCantidad.getText().trim();
            if (!nombreProducto.isEmpty() && !cantidad.isEmpty()) {
                guardar();
                cargarDatos();
                limpiarControles();
                desactivarControles();
                activarTbl();
                btnNuevo.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                cargarDatos();
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

// Mandar los datos a la base para luego almacenarlos
    public void guardar() {
        Producto registro = new Producto();
        registro.setNombreProducto(txtNombreProducto.getText());
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        try {
            PreparedStatement prodedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProducto(?, ?)");
            prodedimiento.setString(1, registro.getNombreProducto());
            prodedimiento.setInt(2, registro.getCantidad());
            prodedimiento.execute();
            listaProducto.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        if (tblProductos.getSelectionModel().getSelectedItem() != null) {
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

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarProducto(?, ?, ?)");
            Producto registro = (Producto) tblProductos.getSelectionModel().getSelectedItem();
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar(ActionEvent event) {
        for (int i = 0; i < listaProducto.size(); i++) {
            if (event.getSource() == listaProducto.get(i).getEliminar()) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmación");
                confirmationAlert.setHeaderText("¿Está seguro de eliminar el registro?");
                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProducto(?)");
                        procedimiento.setInt(1, listaProducto.get(i).getCodigoProducto());
                        procedimiento.execute();
                        listaProducto.remove(tblProductos.getItems().get(i));
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

    public void reporte() {

    }

    public void formatoNumero(TextField textField) {
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
        textField.setTextFormatter(textFormatter);
    }

    public void deseleccionar() {
        tblProductos.getSelectionModel().clearSelection();
    }

    public void activarTbl() {
        tblProductos.setOnMouseClicked(a -> {
            seleccionarElemento();
        });
    }

    public void asignarBoton() {
        for (int i = 0; i < listaProducto.size(); i++) {
            listaProducto.get(i).getEliminar().setOnAction(this::eliminar);
            listaProducto.get(i).getEliminar().getStylesheets().add("/org/kaledrod/resourse/TonysKinal.css");
            listaProducto.get(i).getEliminar().getStyleClass().add("eliminar");
        }
    }

// deshabilitar los texfield
    public void desactivarControles() {
        btnConfirmar.setVisible(false);
        btnCancelar.setVisible(false);
        txtCodProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidad.setEditable(false);
    }

//  habilitar los texfield
    public void activarControles() {
        btnConfirmar.setVisible(true);
        btnCancelar.setVisible(true);
        txtCodProducto.setEditable(false);
        txtNombreProducto.setEditable(true);
        txtCantidad.setEditable(true);
    }

//  Borrar todo dato ecrito en el textfield
    public void limpiarControles() {
        txtCodProducto.clear();
        txtNombreProducto.clear();
        txtCantidad.clear();
    }

//  Obtener el ecsenario
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

//  Establecer el ecsenario
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

// Regresar al menu principal
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

// Accion para cerrar el 
    public void buttonAccionCerrar() {
        System.exit(0);
    }

    public void buttonAccionMinimizar() {
        escenarioPrincipal.minimizarVentana();
    }

}
