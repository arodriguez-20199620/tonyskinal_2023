/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kaledrod.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.kaledrod.bean.TipoPlato;
import org.kaledrod.db.Conexion;
import org.kaledrod.main.Principal;

/**
 *
 * @author Kaled Rodriguez
 */
public class TipoPlatoController implements Initializable {

    Alert alerta = new Alert(Alert.AlertType.WARNING);
    private Principal escenarioPrincipal;
    private ObservableList<TipoPlato> listaTipoPlato;

//  Declaracion de los botoncitos :3
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

// Declaracion de los texfields :3
    @FXML
    private TextField txtCodTipoPlato;
    @FXML
    private TextField txtDescripcion;

//  Declaracion de la tablita 7u7
    @FXML
    private TableView tblTipoPlatos;
    @FXML
    private TableColumn colCodTipoPlato;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colAccion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        desactivarControles();
        cargarDatos();
        asignarBoton();
    }

    public void cargarDatos() {
        tblTipoPlatos.setItems(getTipoPlato());
        colCodTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("codigoTipoPlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcionTipo"));
        colAccion.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("eliminar"));
        asignarBoton();
    }

    public ObservableList<TipoPlato> getTipoPlato() {
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                        resultado.getString("descripcionTipo")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        if (tblTipoPlatos.getSelectionModel().getSelectedItem() != null) {
            txtCodTipoPlato.setText(String.valueOf(((TipoPlato) tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
            txtDescripcion.setText(((TipoPlato) tblTipoPlatos.getSelectionModel().getSelectedItem()).getDescripcionTipo());
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
        tblTipoPlatos.setOnMouseClicked(null);
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
            String descripcion = txtDescripcion.getText().trim();
            if (!descripcion.isEmpty()) {
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

    public void guardar() {
        if (txtDescripcion.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Todos los campos deben ser llenados");
        } else {
            TipoPlato registro = new TipoPlato();
            registro.setDescripcionTipo(txtDescripcion.getText());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoPlato(?)");
                procedimiento.setString(1, registro.getDescripcionTipo());
                procedimiento.executeQuery();
                listaTipoPlato.add(registro);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void editar() {
        if (tblTipoPlatos.getSelectionModel().getSelectedItem() != null) {
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoPlato(?, ?)");
            TipoPlato registro = (TipoPlato) tblTipoPlatos.getSelectionModel().getSelectedItem();
            registro.setDescripcionTipo(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2, registro.getDescripcionTipo());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar(ActionEvent event) {
        for (int i = 0; i < listaTipoPlato.size(); i++) {
            if (event.getSource() == listaTipoPlato.get(i).getEliminar()) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmación");
                confirmationAlert.setHeaderText("¿Está seguro de eliminar el registro?");
                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoPlato(?)");
                        procedimiento.setInt(1, listaTipoPlato.get(i).getCodigoTipoPlato());
                        procedimiento.execute();
                        listaTipoPlato.remove(tblTipoPlatos.getItems().get(i));
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

    public void deseleccionar() {
        tblTipoPlatos.getSelectionModel().clearSelection();
        limpiarControles();
    }

    public void activarTbl() {
        tblTipoPlatos.setOnMouseClicked(a -> {
            seleccionarElemento();
        });
    }

    public void asignarBoton() {
        for (int i = 0; i < listaTipoPlato.size(); i++) {
            listaTipoPlato.get(i).getEliminar().setOnAction(this::eliminar);
            listaTipoPlato.get(i).getEliminar().getStylesheets().add("/org/kaledrod/resourse/TonysKinal.css");
            listaTipoPlato.get(i).getEliminar().getStyleClass().add("eliminar");
        }
    }

    public void desactivarControles() {
        btnConfirmar.setVisible(false);
        btnCancelar.setVisible(false);
        txtCodTipoPlato.setEditable(false);
        txtDescripcion.setEditable(false);
    }

    public void activarControles() {
        btnConfirmar.setVisible(true);
        btnCancelar.setVisible(true);
        txtCodTipoPlato.setEditable(false);
        txtDescripcion.setEditable(true);
    }

    public void limpiarControles() {
        txtCodTipoPlato.clear();
        txtDescripcion.clear();
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
