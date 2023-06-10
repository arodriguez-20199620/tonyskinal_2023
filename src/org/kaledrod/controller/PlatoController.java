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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
        formatoNumero(txtCantidad);
        cmbCodigoTipoPlato.setItems(getTipoPlato());

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
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
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
        btnNuevo.setDisable(true);
        btnEditar.setDisable(true);
        btnReporte.setDisable(true);
        tblPlatos.setOnMouseClicked(null);
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
            String cantidad = txtCantidad.getText().trim();
            String nombrePlato = txtNombrePlato.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String precioPlato = txtPrecioPlato.getText().trim();
            if (!cantidad.isEmpty() && !nombrePlato.isEmpty() && !descripcion.isEmpty() && !precioPlato.isEmpty()&&(cmbCodigoTipoPlato.getSelectionModel().getSelectedItem() != null)){
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
        Plato registro = new Plato();
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setDescripcion(txtDescripcion.getText());
        registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
        registro.setCodigoTipoPlato(((TipoPlato) cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPlato(?,?,?,?,?)");
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setString(2, registro.getNombrePlato());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setDouble(4, registro.getPrecioPlato());
            procedimiento.setInt(5, registro.getCodigoTipoPlato());
            procedimiento.execute();
            listaPlato.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar(ActionEvent event) {
        for (int i = 0; i < listaPlato.size(); i++) {
            if (event.getSource() == listaPlato.get(i).getEliminar()) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmación");
                confirmationAlert.setHeaderText("¿Está seguro de eliminar el registro?");
                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPlato(?)");
                        procedimiento.setInt(1, listaPlato.get(i).getCodigoPlato());
                        procedimiento.execute();
                        listaPlato.remove(tblPlatos.getItems().get(i));
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

    public void editar() {
        if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
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
        Plato registro = (Plato) tblPlatos.getSelectionModel().getSelectedItem();
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setDescripcion(txtDescripcion.getText());
        registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
        registro.setCodigoTipoPlato(((TipoPlato) cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPlato(?,?,?,?,?,?)");
            procedimiento.setInt(1, registro.getCodigoPlato());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setString(3, registro.getNombrePlato());
            procedimiento.setString(4, registro.getDescripcion());
            procedimiento.setDouble(5, registro.getPrecioPlato());
            procedimiento.setInt(6, registro.getCodigoTipoPlato());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reporte() {

    }

    public void deseleccionar() {
        tblPlatos.getSelectionModel().clearSelection();
    }

    public void activarTbl() {
        tblPlatos.setOnMouseClicked(a -> {
            seleccionarElementos();
        });
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
