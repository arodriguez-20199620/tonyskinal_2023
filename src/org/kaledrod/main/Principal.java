/*
Angel Kaled Rodriguez Soc
IN5BM
2019620
Fecha De Creacion 12/04/2023 Hora : 8:00
 */
package org.kaledrod.main;

import java.io.InputStream;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.kaledrod.bean.Login;
import org.kaledrod.controller.EmpleadoController;
import org.kaledrod.controller.EmpresaController;
import org.kaledrod.controller.LoginController;
import org.kaledrod.controller.MenuPrincipalController;
import org.kaledrod.controller.PlatoController;
import org.kaledrod.controller.PresupuestoController;
import org.kaledrod.controller.ProductoController;
import org.kaledrod.controller.ProgramadorController;
import org.kaledrod.controller.ServiciosController;
import org.kaledrod.controller.TipoEmpleadoController;  
import org.kaledrod.controller.TipoPlatoController;
import org.kaledrod.controller.UsuarioController;

public class Principal extends Application {

    private final String PAQUETE_VISTA = "/org/kaledrod/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage escenarioPrinciapal) throws Exception {
        this.escenarioPrincipal = escenarioPrinciapal;
        this.escenarioPrincipal.setTitle("Tonys Kinal");
        escenarioPrinciapal.getIcons().add(new Image("org/kaledrod/image/Logo2Forma2.png"));
        escenarioPrincipal.centerOnScreen();
        escenarioPrinciapal.setResizable(false);
        escenarioPrincipal.initStyle(StageStyle.UNDECORATED);
//        Parent root = FXMLLoader.load(getClass().getResource("/org/kaledrod/view/LoginView.fxml"));
//        escenarioPrinciapal.setScene(escena);
//        escenarioPrinciapal.show();
        ventanaLogin();
//        menuPrincipal();
        escenarioPrinciapal.show();

    }

    public void minimizarVentana() {
        escenarioPrincipal.setIconified(true);

    }

    public void menuPrincipal() {
        try {
            MenuPrincipalController menu = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 1000, 600);
            menu.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmpresa() {
        try {
            EmpresaController empresa = (EmpresaController) cambiarEscena("EmpresaView.fxml", 1000, 650);
            empresa.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
        }
    }

    public void ventanaEmpleado() {
        try {
            EmpleadoController empleado = (EmpleadoController) cambiarEscena("EmpleadoView.fxml", 1000, 650);
            empleado.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaProducto() {
        try {
            ProductoController producto = (ProductoController) cambiarEscena("ProductoView.fxml", 1000, 650);
            producto.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void ventanaTipoEmpleado() {
        try {
            TipoEmpleadoController tipoEmlpeado = (TipoEmpleadoController) cambiarEscena("TipoEmpleadoView.fxml", 1000, 650);
            tipoEmlpeado.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void ventanaPresupuesto() {
        try {
            PresupuestoController presupuesto = (PresupuestoController) cambiarEscena("PresupuestoView.fxml", 1000, 600);
            presupuesto.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTipoPlato() {
        try {
            TipoPlatoController tipoPlato = (TipoPlatoController) cambiarEscena("TipoPlato.fxml", 1000, 650);
            tipoPlato.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaServicios() {
        try {
            ServiciosController servicios = (ServiciosController) cambiarEscena("ServiciosView.fxml", 1000, 650);
            servicios.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
        }
    }

    public void ventanaPlato() {
        try {
            PlatoController plato = (PlatoController) cambiarEscena("PlatoView.fxml", 1000, 650);
            plato.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
//         e.printStackTrace();
        }
    }

    public void ventanaProgramador() {
        try {
            ProgramadorController programador = (ProgramadorController) cambiarEscena("ProgramadorView.fxml", 800, 500);
            programador.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaLogin() {
        try {
            LoginController login = (LoginController) cambiarEscena("LoginView.fxml", 1000, 650);
            login.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void ventanaUsuario() {
        try {
            UsuarioController usuario = (UsuarioController) cambiarEscena("UsuarioView.fxml", 1000, 650);
            usuario.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
        escena.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        escena.setOnMouseDragged(e -> {
            escenarioPrincipal.setX(e.getScreenX() - xOffset);
            escenarioPrincipal.setY(e.getScreenY() - yOffset);
        });
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }

}
