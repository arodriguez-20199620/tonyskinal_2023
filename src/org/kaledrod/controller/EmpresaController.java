package org.kaledrod.controller;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.kaledrod.bean.Empresa;
import org.kaledrod.bean.Presupuesto;
import org.kaledrod.db.Conexion;
import org.kaledrod.main.Principal;
import org.kaledrod.report.GenerarReporte;

/**
 *
 * @author Kaled Rodriguez
 */
public class EmpresaController implements Initializable {

    Alert alerta = new Alert(Alert.AlertType.WARNING);

    private Principal escenarioPrincipal;

    private ObservableList<Empresa> listaEmpresa;

//  Botones
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
//  Texfield   
    @FXML
    private TextField txtCodEmpresa;
    @FXML
    private TextField txtNombreEmpresa;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;
//  TableView
    @FXML
    private TableView tblEmpresas;
    @FXML
    private TableColumn colCodEmpresa;
    @FXML
    private TableColumn colNombreEmpresa;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colAccion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        desactivarControles();
        cargarDatos();
        asignarBoton();
    }

    public void cargarDatos() {
        tblEmpresas.setItems(getEmpresa());
        colCodEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
        colAccion.setCellValueFactory(new PropertyValueFactory<Empresa, String>("eliminar"));
        asignarBoton();
    }

    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
            txtCodEmpresa.setText(String.valueOf(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            txtNombreEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
            txtDireccion.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
            txtTelefono.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
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
        tblEmpresas.setOnMouseClicked(null);
        limpiarControles();
        activarControles();
        deseleccionar();
        btnCancelar.setOnAction(e -> {
            limpiarControles();
            desactivarControles();
            activarTbl();
        });
        btnConfirmar.setOnAction(e -> {
            String nombreEmpresa = txtNombreEmpresa.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String telefono = txtTelefono.getText().trim();
            if (!nombreEmpresa.isEmpty() && !direccion.isEmpty() && !telefono.isEmpty()) {
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
        Empresa registro = new Empresa();
        registro.setNombreEmpresa(txtNombreEmpresa.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpresa(?, ?, ?)");
            procedimiento.setString(1, registro.getNombreEmpresa());
            procedimiento.setString(2, registro.getDireccion());
            procedimiento.setString(3, registro.getTelefono());
            procedimiento.execute();
            listaEmpresa.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean buscarPresupuesto(int codEmpresa) {
        boolean salida = false;
        Presupuesto resultado = new Presupuesto();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPresupuesto(?)");
            procedimiento.setInt(1, codEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            if (registro.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salida;
    }

    public void editar() {
        if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
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
        Empresa registro = (Empresa) tblEmpresas.getSelectionModel().getSelectedItem();
        registro.setNombreEmpresa(txtNombreEmpresa.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpresa(?,?,?,?)");
            procedimiento.setInt(1, registro.getCodigoEmpresa());
            procedimiento.setString(2, registro.getNombreEmpresa());
            procedimiento.setString(3, registro.getDireccion());
            procedimiento.setString(4, registro.getTelefono());
            procedimiento.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
        }
    }

    public void eliminar(ActionEvent event) {
        for (int i = 0; i < listaEmpresa.size(); i++) {
            if (event.getSource() == listaEmpresa.get(i).getEliminar()) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmación");
                confirmationAlert.setHeaderText("¿Está seguro de eliminar el registro?");
                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpresa(?)");
                        procedimiento.setInt(1, listaEmpresa.get(i).getCodigoEmpresa());
                        procedimiento.execute();
                        listaEmpresa.remove(tblEmpresas.getItems().get(i));
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

    public void generarReporete() {
        imprimirReporte();
    }

    public void imprimirReporte() {
        String ruta = "ola";
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        parametros.put("RUTA_IMAGEN", EmpleadoController.class.getResource("/org/kaledrod/image/Logo2Forma2.png"));
        GenerarReporte.mostarReporte("ReporteEmpresa.jasper", "Reporte de empresa", parametros);
    }

    public void reporte() {

    }

    public void deseleccionar() {
        tblEmpresas.getSelectionModel().clearSelection();
    }

    public void activarTbl() {
        tblEmpresas.setOnMouseClicked(a -> {
            seleccionarElemento();
        });
    }

    public void asignarBoton() {
        for (int i = 0; i < listaEmpresa.size(); i++) {
            listaEmpresa.get(i).getEliminar().setOnAction(this::eliminar);
            listaEmpresa.get(i).getEliminar().getStylesheets().add("/org/kaledrod/resourse/TonysKinal.css");
            listaEmpresa.get(i).getEliminar().getStyleClass().add("eliminar");
        }
    }

    public void desactivarControles() {
        btnConfirmar.setVisible(false);
        btnCancelar.setVisible(false);
        txtCodEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
    }

    public void activarControles() {
        btnConfirmar.setVisible(true);
        btnCancelar.setVisible(true);
        txtCodEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
    }

    public void limpiarControles() {
        txtCodEmpresa.clear();
        txtNombreEmpresa.clear();
        txtDireccion.clear();
        txtTelefono.clear();
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

    public void ventanaPresupuesto() {
        escenarioPrincipal.ventanaPresupuesto();
    }

    public void buttonAccionCerrar() {
        System.exit(0);
    }

    public void buttonAccionMinimizar() {
        escenarioPrincipal.minimizarVentana();
    }

}
