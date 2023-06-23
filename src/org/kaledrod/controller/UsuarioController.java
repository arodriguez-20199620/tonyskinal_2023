package org.kaledrod.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import javafx.scene.paint.Color;
import org.kaledrod.bean.Usuario;
import org.kaledrod.db.Conexion;
import org.kaledrod.main.Principal;

/**
 * Controlador para la ventana de creación de usuarios.
 */
public class UsuarioController implements Initializable {

    private Principal escenarioPrincipal;

    @FXML
    private JFXProgressBar pbUser;
    @FXML
    private Label lblUserConfirm;
    @FXML
    private Label lblPassConfirm;
    @FXML
    private TextField txtNombreUsuario;
    @FXML
    private TextField txtApellidoUsuario;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private PasswordField txtConfrimarContrasena;
    @FXML
    private JFXButton btnCrear;

    private Timer timer = new Timer();
    private Alert alerta = new Alert(Alert.AlertType.WARNING);

    /**
     * Método inicializador del controlador.
     * 
     * @param location  URL de la ubicación del FXML.
     * @param resources ResourceBundle utilizado para localizar el FXML.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnCrear.setDisable(true);
        pbUser.setVisible(false);
        btnCrear.disableProperty().bind(
                txtApellidoUsuario.textProperty().isEmpty()
                        .or(txtNombreUsuario.textProperty().isEmpty())
                        .or(txtApellidoUsuario.textProperty().isEmpty())
                        .or(txtContrasena.textProperty().isEmpty())
                        .or(txtConfrimarContrasena.textProperty().isEmpty())
        );
    }

    /**
     * Crea un nuevo usuario.
     */
    public void nuevo() {
        pbUser.setVisible(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isExistUser() && confirmarContrasena()) {
                    guardar();
                    Platform.runLater(() -> escenarioPrincipal.ventanaLogin());
                } else {
                    confirmarContrasena();
                    isExistUser();
                    limpiarControles();
                    pbUser.setVisible(false);
                }
            }
        }, 500);
    }

    /**
     * Verifica si el usuario ya existe en la base de datos.
     * 
     * @return true si el usuario no existe, false si el usuario ya existe.
     */
    public boolean isExistUser() {
        boolean user = true;
        String userConfirm = txtUsuario.getText();
        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarUsuarios(?)}");
            sp.setString(1, userConfirm);
            ResultSet rs = sp.executeQuery();
            if (rs.next()) {
                Platform.runLater(() -> {
                    lblUserConfirm.setVisible(true);
                    lblUserConfirm.setTextFill(Color.RED);
                    lblUserConfirm.setText("El correo ingresado ya existe");
                });
                user = false;
            }
            tiempoVisibilidad(lblUserConfirm);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Verifica si la contraseña y su confirmación coinciden.
     * 
     * @return true si las contraseñas coinciden, false si no coinciden.
     */
    public boolean confirmarContrasena() {
        boolean pass = true;
        String contrasena = txtContrasena.getText();
        String confirmacion = txtConfrimarContrasena.getText();
        boolean contrasenasConcuerdan = contrasena.equals(confirmacion);
        if (!contrasenasConcuerdan) {
            Platform.runLater(() -> {
                lblPassConfirm.setVisible(true);
                lblPassConfirm.setTextFill(Color.RED);
                lblPassConfirm.setText("¡Las contraseñas deben coincidir!");
            });
            pass = false;
        }
        tiempoVisibilidad(lblPassConfirm);
        return pass;
    }

    /**
     * Establece un temporizador para ocultar una etiqueta después de un tiempo determinado.
     * 
     * @param label La etiqueta que se ocultará después de un tiempo.
     */
    public void tiempoVisibilidad(Label label) {
        Timer tm = new Timer();
        tm.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> label.setVisible(false));
            }
        }, 1000);
    }

    /**
     * Guarda los datos del nuevo usuario en la base de datos.
     */
    public void guardar() {
        Usuario registros = new Usuario();
        registros.setNombreUsuario(txtNombreUsuario.getText());
        registros.setApellidoUsuario(txtApellidoUsuario.getText());
        registros.setUsuarioLogin(txtUsuario.getText());
        registros.setContrasena(txtContrasena.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarUsuario(?, ?, ?, ?);");
            procedimiento.setString(1, registros.getNombreUsuario());
            procedimiento.setString(2, registros.getApellidoUsuario());
            procedimiento.setString(3, registros.getUsuarioLogin());
            procedimiento.setString(4, registros.getContrasena());
            procedimiento.execute();
            pbUser.setVisible(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Desactiva los controles de entrada de texto.
     */
    public void desactivarControles() {
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtContrasena.setEditable(false);
        txtConfrimarContrasena.setEditable(false);
    }

    /**
     * Activa los controles de entrada de texto.
     */
    public void activarControles() {
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtContrasena.setEditable(true);
        txtConfrimarContrasena.setEditable(true);
    }

    /**
     * Limpia los campos de entrada de texto.
     */
    public void limpiarControles() {
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuario.clear();
        txtContrasena.clear();
        txtConfrimarContrasena.clear();
    }

    /**
     * Obtiene el objeto Principal del controlador.
     * 
     * @return El objeto Principal.
     */
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    /**
     * Establece el objeto Principal del controlador.
     * 
     * @param escenarioPrincipal El objeto Principal a establecer.
     */
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    /**
     * Abre la ventana de inicio de sesión.
     */
    public void ventanaLogin() {
        escenarioPrincipal.ventanaLogin();
    }

    /**
     * Acción del botón para cerrar la aplicación.
     */
    public void buttonAccionCerrar() {
        System.exit(0);
    }

    /**
     * Acción del botón para minimizar la ventana.
     */
    public void buttonAccionMinimizar() {
        escenarioPrincipal.minimizarVentana();
    }
}
