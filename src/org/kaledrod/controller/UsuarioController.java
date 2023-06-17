package org.kaledrod.controller;

import java.net.URL;
import java.sql.PreparedStatement;
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
import java.sql.ResultSet;
import javafx.concurrent.Task;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import org.kaledrod.bean.Usuario;
import org.kaledrod.db.Conexion;
import org.kaledrod.main.Principal;

public class UsuarioController implements Initializable {

    private Principal escenarioPrincipal;

    @FXML
    private JFXProgressBar pbUser = new JFXProgressBar();
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
    private Timer timer;
    Alert alerta = new Alert(Alert.AlertType.WARNING);
    private boolean user;
    private boolean pass;

    @Override

    public void initialize(URL location, ResourceBundle resources) {
        btnCrear.setDisable(true);
        pbUser.setVisible(false);
        btnCrear.disableProperty().bind(txtApellidoUsuario.textProperty().isEmpty()
                .or(txtNombreUsuario.textProperty().isEmpty())
                .or(txtApellidoUsuario.textProperty().isEmpty())
                .or(txtContrasena.textProperty().isEmpty())
                .or(txtConfrimarContrasena.textProperty().isEmpty()));
    }

    public void nuevo() {
        pbUser.setVisible(true);
        if (user) {
            if (pass) {
                guardar();
                escenarioPrincipal.ventanaLogin();
            } else {
                confirmarContrasena();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        limpiarControles();
                    }
                }, 500);
            }
        } else {

            isExistUser();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    limpiarControles();
                }
            }, 500);

        }
    }

    public void isExistUser() {
        user = true;
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String userConfirm = txtUsuario.getText();
                Platform.runLater(() -> {
                    try {
                        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarUsuarios(?)}");
                        sp.setString(1, userConfirm);
                        ResultSet rs = sp.executeQuery();
                        if (rs.next()) {
                            lblUserConfirm.setVisible(true);
                            lblUserConfirm.setTextFill(Color.RED);
                            lblUserConfirm.setText("El coreo ingresado ya existe");
                            pbUser.setVisible(false);
                            user = false;
                        }
                        tiempoVisibilidad(lblUserConfirm);
                    } catch (SQLException e) {
                    }
                });
            }

        }, 500);
    }

    public void confirmarContrasena() {
        pass = true;
        String confirmacion = txtConfrimarContrasena.getText().trim();
        if (confirmacion.isEmpty()) {

        } else {
            if (timer != null) {
                timer.cancel();
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    String contrasena = txtContrasena.getText();
                    String confirmacion = txtConfrimarContrasena.getText();
                    boolean contrasenasConcuerdan = contrasena.equals(confirmacion);
                    Platform.runLater(() -> {
                        if (contrasenasConcuerdan != true) {
                            lblPassConfirm.setVisible(true);
                            lblPassConfirm.setTextFill(Color.RED);
                            lblPassConfirm.setText("¡Las contraseñas deben de coincidir!");
                            pbUser.setVisible(false);
                            pass = false;
                        }
                        tiempoVisibilidad(lblPassConfirm);
                    });
                }
            }, 500);
        }
    }

    public void tiempoVisibilidad(Label label) {
        Timer tm = new Timer();
        tm.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> label.setVisible(false));
            }
        }, 1000);
    }

    public void guardar() {
//        String contra = txtContrasena.getText();
//        String encrip = DigestUtils.md5Hex(contra);
//        System.out.println(encrip);
        Usuario registros = new Usuario();
        registros.setNombreUsuario(txtNombreUsuario.getText());
        registros.setApellidoUsuario(txtApellidoUsuario.getText());
        registros.setUsuarioLogin(txtUsuario.getText());
        registros.setContrasena(txtContrasena.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarUsuario  (?, ?, ?, ?);");
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

    public void desactivarControles() {
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtContrasena.setEditable(false);
        txtConfrimarContrasena.setEditable(false);
    }

    public void activarControles() {
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtContrasena.setEditable(true);
        txtConfrimarContrasena.setEditable(true);
    }

    public void limpiarControles() {
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuario.clear();
        txtContrasena.clear();
        txtConfrimarContrasena.clear();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaLogin() {
        escenarioPrincipal.ventanaLogin();
    }

    public void buttonAccionCerrar() {
        System.exit(0);
    }

    public void buttonAccionMinimizar() {
        escenarioPrincipal.minimizarVentana();
    }
}
