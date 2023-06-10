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
import org.kaledrod.bean.TipoEmpleado;
import org.kaledrod.db.Conexion;
import org.kaledrod.main.Principal;

/**
 *
 * @author informatica
 */
public class TipoEmpleadoController implements Initializable {

    Alert alerta = new Alert(Alert.AlertType.WARNING);
    private Principal escenarioPrincipal;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;

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
    private TextField txtCodTipoEmpleado;
    @FXML
    private TextField txtDescripcion;

//  Declaracion de la tablita 7u7
    @FXML
    private TableView tblTipoEmpleados;
    @FXML
    private TableColumn colCodTipoEmpleado;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colAccion;

// Declaracion de las imagenes :D
    @FXML
    private ImageView imgRegresar;
    @FXML
    private ImageView imgCerrar;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        desactivarControles();
        cargarDatos();
        asignarBoton();
    }

    public void cargarDatos() {
        tblTipoEmpleados.setItems(getTipoEmpleado());
        colCodTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("codigoTipoEmpleado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcion"));
        colAccion.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("eliminar"));
        asignarBoton();
    }

    public ObservableList<TipoEmpleado> getTipoEmpleado() {
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        if (tblTipoEmpleados.getSelectionModel().getSelectedItem() != null) {
            txtCodTipoEmpleado.setText(String.valueOf(((TipoEmpleado) tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
            txtDescripcion.setText(((TipoEmpleado) tblTipoEmpleados.getSelectionModel().getSelectedItem()).getDescripcion());
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
        tblTipoEmpleados.setOnMouseClicked(null);
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
            TipoEmpleado registro = new TipoEmpleado();
            registro.setDescripcion(txtDescripcion.getText());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoEmpleado(?)");
                procedimiento.setString(1, registro.getDescripcion());
                procedimiento.executeQuery();
                listaTipoEmpleado.add(registro);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void eliminar(ActionEvent event) {
        for (int i = 0; i < listaTipoEmpleado.size(); i++) {
            if (event.getSource() == listaTipoEmpleado.get(i).getEliminar()) {

                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmación");
                confirmationAlert.setHeaderText("¿Está seguro de eliminar el registro?");

                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoEmpleado(?)");
                        procedimiento.setInt(1, listaTipoEmpleado.get(i).getCodigoTipoEmpleado());
                        procedimiento.execute();
                        listaTipoEmpleado.remove(tblTipoEmpleados.getItems().get(i));
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
        if (tblTipoEmpleados.getSelectionModel().getSelectedItem() != null) {
            btnNuevo.setDisable(true);
            btnEditar.setDisable(true);
            btnReporte.setDisable(true);
            activarControles();
            btnCancelar.setOnAction(e -> {
                actualizar();
                deseleccionar();
                limpiarControles();
                desactivarControles();
                cargarDatos();
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoEmpleado(?, ?)");
            TipoEmpleado registro = (TipoEmpleado) tblTipoEmpleados.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reporte() {

    }

    public void deseleccionar() {
        tblTipoEmpleados.getSelectionModel().clearSelection();
        limpiarControles();
    }

    private void activarTbl() {
        tblTipoEmpleados.setOnMouseClicked(a -> {
            seleccionarElemento();
        });
    }

    private void asignarBoton() {
        for (int i = 0; i < listaTipoEmpleado.size(); i++) {
            listaTipoEmpleado.get(i).getEliminar().setOnAction(this::eliminar);
            listaTipoEmpleado.get(i).getEliminar().getStylesheets().add("/org/kaledrod/resourse/TonysKinal.css");
            listaTipoEmpleado.get(i).getEliminar().getStyleClass().add("eliminar");
        }
    }

    public void activarControles() {
        btnConfirmar.setVisible(true);
        btnCancelar.setVisible(true);
        txtCodTipoEmpleado.setEditable(false);
        txtDescripcion.setEditable(true);
    }

    public void desactivarControles() {
        btnConfirmar.setVisible(false);
        btnCancelar.setVisible(false);
        txtCodTipoEmpleado.setEditable(false);
        txtDescripcion.setEditable(false);
    }

    public void limpiarControles() {
        txtCodTipoEmpleado.clear();
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
