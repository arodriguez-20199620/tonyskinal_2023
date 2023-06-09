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
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javax.swing.JOptionPane;
import org.kaledrod.bean.Login;
import org.kaledrod.bean.Usuario;
import org.kaledrod.db.Conexion;
import org.kaledrod.main.Principal;

public class LoginController implements Initializable {

    Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
    private Principal escenarioPrincipal;
    private ObservableList<Usuario> listaUsuario;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public ObservableList<Usuario> getUsuario() {
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarUsuarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Usuario(resultado.getInt("codigoUsuario"),
                        resultado.getString("nombreUsuario"),
                        resultado.getString("apellidoUsuario"),
                        resultado.getString("usuarioLogin"),
                        resultado.getString("contrasena")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaUsuario = FXCollections.observableArrayList(lista);
    }

    @FXML
    private void sesion() {
        Login login = new Login();
        int x = 0;
        boolean bandera = false;
        login.setUsuarioMaster(txtUsuario.getText());
        login.setPasswordLogin(txtPassword.getText());
        String user = txtUsuario.getText().trim();
        String pass = txtPassword.getText().trim();
        if (!user.isEmpty() && !pass.isEmpty()) {
            while (x < getUsuario().size()) {
                user = getUsuario().get(x).getUsuarioLogin();
                pass = getUsuario().get(x).getContrasena();
                if (user.equals(login.getUsuarioMaster()) && pass.equals(login.getPasswordLogin())) {
                    confirmacion.setTitle("Confirmacion");
                    confirmacion.setHeaderText("Sesión iniciada");
                    confirmacion.setContentText("Bienvenido \n" + getUsuario().get(x).getNombreUsuario() + " "
                            + getUsuario().get(x).getApellidoUsuario());
                    confirmacion.showAndWait();
                    escenarioPrincipal.menuPrincipal();
                    x = getUsuario().size();
                    bandera = true;
                }
                x++; //  x+= x =x+1 ++x 
            }
            if (bandera == false) {
                JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
            }

        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Advertencia");
            alerta.setHeaderText(null);
            alerta.setContentText("Uno de los campos esta vacio");
            alerta.showAndWait();
        }

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaUsuario() {
        escenarioPrincipal.ventanaUsuario();
    }

    public void buttonAccionCerrar() {
        System.exit(0);
    }

    public void buttonAccionMinimizar() {
        escenarioPrincipal.minimizarVentana();
    }

}
