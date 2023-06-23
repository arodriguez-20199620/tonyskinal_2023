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
import org.kaledrod.controller.Productos_has_platoController;
import org.kaledrod.controller.ProgramadorController;
import org.kaledrod.controller.ServiciosController;
import org.kaledrod.controller.Servicios_has_empleadoController;
import org.kaledrod.controller.Servicios_has_platosController;
import org.kaledrod.controller.TipoEmpleadoController;
import org.kaledrod.controller.TipoPlatoController;
import org.kaledrod.controller.UsuarioController;

public class Principal extends Application {

    private final String PAQUETE_VISTA = "/org/kaledrod/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Método principal que se ejecuta al iniciar la aplicación JavaFX.
     * @param escenarioPrinciapal El escenario principal de la aplicación.
     * @throws Exception Si ocurre algún error durante la carga de la ventana de inicio de sesión.
     */
    @Override
    public void start(Stage escenarioPrinciapal) throws Exception {
        this.escenarioPrincipal = escenarioPrinciapal;
        this.escenarioPrincipal.setTitle("Tonys Kinal");
        escenarioPrinciapal.getIcons().add(new Image("org/kaledrod/image/Logo2Forma3.png"));
        escenarioPrincipal.centerOnScreen();
        escenarioPrinciapal.setResizable(false);
        escenarioPrincipal.initStyle(StageStyle.UNDECORATED);
        ventanaLogin();
        escenarioPrinciapal.show();
    }

    /**
     * Minimiza la ventana principal.
     */
    public void minimizarVentana() {
        escenarioPrincipal.setIconified(true);
    }

    /**
     * Abre la ventana del menú principal.
     */
    public void menuPrincipal() {
        try {
            MenuPrincipalController menu = (MenuPrincipalController) cargarEscena("MenuPrincipalView.fxml", 1000, 600);
            menu.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana de empresa.
     */
    public void ventanaEmpresa() {
        try {
            EmpresaController empresa = (EmpresaController) cargarEscena("EmpresaView.fxml", 1000, 650);
            empresa.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana de empleado.
     */
    public void ventanaEmpleado() {
        try {
            EmpleadoController empleado = (EmpleadoController) cargarEscena("EmpleadoView.fxml", 1000, 650);
            empleado.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana de producto.
     */
    public void ventanaProducto() {
        try {
            ProductoController producto = (ProductoController) cargarEscena("ProductoView.fxml", 1000, 650);
            producto.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Abre la ventana de tipo de empleado.
     */
    public void ventanaTipoEmpleado() {
        try {
            TipoEmpleadoController tipoEmpleado = (TipoEmpleadoController) cargarEscena("TipoEmpleadoView.fxml", 1000, 650);
            tipoEmpleado.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana de presupuesto.
     */
    public void ventanaPresupuesto() {
        try {
            PresupuestoController presupuesto = (PresupuestoController) cargarEscena("PresupuestoView.fxml", 1000, 650);
            presupuesto.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Abre la ventana de tipo de plato.
     */
    public void ventanaTipoPlato() {
        try {
            TipoPlatoController tipoPlato = (TipoPlatoController) cargarEscena("TipoPlato.fxml", 1000, 650);
            tipoPlato.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana de servicios.
     */
    public void ventanaServicios() {
        try {
            ServiciosController servicios = (ServiciosController) cargarEscena("ServiciosView.fxml", 1000, 650);
            servicios.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana de plato.
     */
    public void ventanaPlato() {
        try {
            PlatoController plato = (PlatoController) cargarEscena("PlatoView.fxml", 1000, 650);
            plato.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana de programador.
     */
    public void ventanaProgramador() {
        try {
            ProgramadorController programador = (ProgramadorController) cargarEscena("ProgramadorView.fxml", 1000, 650);
            programador.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana de inicio de sesión.
     */
    public void ventanaLogin() {
        try {
            LoginController login = (LoginController) cargarEscena("LoginView.fxml", 1000, 650);
            login.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Abre la ventana de usuario.
     */
    public void ventanaUsuario() {
        try {
            UsuarioController usuario = (UsuarioController) cargarEscena("UsuarioView.fxml", 1000, 650);
            usuario.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana de productos_has_platos.
     */
    public void ventanaProductos_has_platos() {
        try {
            Productos_has_platoController productosHasPlatos = (Productos_has_platoController) cargarEscena("Productos_has_platosView.fxml", 1000, 650);
            productosHasPlatos.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana de servicios_has_platos.
     */
    public void ventanaServicios_has_platos() {
        try {
            Servicios_has_platosController serviciosHasPlatos = (Servicios_has_platosController) cargarEscena("Servicios_has_PlatosView.fxml", 1000, 650);
            serviciosHasPlatos.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Abre la ventana de servicios_has_empleados.
     */
    public void ventanaServicios_has_empleados() {
        try {
            Servicios_has_empleadoController serviciosHasEmpleados = (Servicios_has_empleadoController) cargarEscena("Servicios_has_Empleados.fxml", 1000, 650);
            serviciosHasEmpleados.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método principal para ejecutar la aplicación.
     *
     * @param args los argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Carga una escena FXML y devuelve su controlador inicializable.
     *
     * @param fxml el nombre del archivo FXML
     * @param ancho el ancho de la escena
     * @param alto  el alto de la escena
     * @return el controlador inicializable de la escena cargada
     * @throws Exception si ocurre algún error durante la carga de la escena
     */
    public Initializable cargarEscena(String fxml, int ancho, int alto) throws Exception {
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