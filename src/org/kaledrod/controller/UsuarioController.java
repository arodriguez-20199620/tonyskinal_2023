package org.kaledrod.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;
import org.kaledrod.bean.Usuario;
import org.kaledrod.db.Conexion;
import org.kaledrod.main.Principal;

public class UsuarioController implements Initializable {

    private Principal escenarioPrincipal;

    @FXML
    private Button btnCrear;
    // TEXTFIELDS
    @FXML
    private TextField txtCodUsuario;
    @FXML
    private TextField txtNombreUsuario;
    @FXML
    private TextField txtApellidoUsuario;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtContrasena;
    @FXML
    private TextField txtConfrimarContrasena;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void nuevo() {
            String nombreUsuario = txtNombreUsuario.getText().trim();
            String apellidoUsuario = txtApellidoUsuario.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String contrasena = txtContrasena.getText().trim();
            if (!nombreUsuario.isEmpty() && !apellidoUsuario.isEmpty() && !usuario.isEmpty() && !contrasena.isEmpty()) {
                guardar();
                escenarioPrincipal.ventanaLogin();
            } else {
                JOptionPane.showMessageDialog(null, "No se han ingresado datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desactivarControles() {
        txtCodUsuario.setEditable(false);
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtContrasena.setEditable(false);
        txtConfrimarContrasena.setEditable(false);
    }

    public void activarControles() {
        txtCodUsuario.setEditable(false);
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
