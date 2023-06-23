package org.kaledrod.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.kaledrod.bean.Usuario;
import org.kaledrod.db.Conexion;
import org.kaledrod.main.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controlador para la ventana de inicio de sesión.
 */
public class LoginController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private Principal escenarioPrincipal;
    private ObservableList<Usuario> listaUsuario;

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    /**
     * Método inicializador del controlador.
     * 
     * @param location  URL de la ubicación del FXML.
     * @param resources ResourceBundle utilizado para localizar el FXML.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaUsuario = FXCollections.observableArrayList();
    }

    /**
     * Obtiene la lista de usuarios desde la base de datos.
     */
    private void obtenerUsuarios() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarUsuarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaUsuario.add(new Usuario(resultado.getInt("codigoUsuario"),
                        resultado.getString("nombreUsuario"),
                        resultado.getString("apellidoUsuario"),
                        resultado.getString("usuarioLogin"),
                        resultado.getString("contrasena")));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener la lista de usuarios", e);
        }
    }

    /**
     * Método que se ejecuta al hacer clic en el botón de iniciar sesión.
     */
    @FXML
    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Advertencia", null, "Uno de los campos está vacío");
            return;
        }
        obtenerUsuarios();
        boolean encontrado = false;
        for (Usuario u : listaUsuario) {
            if (usuario.equals(u.getUsuarioLogin()) && password.equals(u.getContrasena())) {
                mostrarAlerta(AlertType.INFORMATION, "Confirmación", "Sesión iniciada",
                        "Bienvenido\n" + u.getNombreUsuario() + " " + u.getApellidoUsuario());
                escenarioPrincipal.menuPrincipal();
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            mostrarAlerta(AlertType.ERROR, "Error", null, "Usuario o contraseña incorrectos");
        }
    }

    /**
     * Muestra una alerta con el tipo, título, encabezado y contenido especificados.
     * 
     * @param tipo      Tipo de alerta.
     * @param titulo    Título de la alerta.
     * @param encabezado Encabezado de la alerta.
     * @param contenido Contenido de la alerta.
     */
    public void mostrarAlerta(AlertType tipo, String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    /**
     * Abre la ventana de gestión de usuarios.
     */
    @FXML
    private void abrirVentanaUsuario() {
        escenarioPrincipal.ventanaUsuario();
    }

    /**
     * Cierra la aplicación.
     */
    @FXML
    private void cerrarAplicacion() {
        System.exit(0);
    }

    /**
     * Minimiza la ventana principal.
     */
    @FXML
    private void minimizarVentana() {
        escenarioPrincipal.minimizarVentana();
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
}
