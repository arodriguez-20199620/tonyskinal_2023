package org.kaledrod.controller;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import org.kaledrod.main.Principal;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * El controlador para la vista Programador.fxml.
 */
public class ProgramadorController implements Initializable {

    private Principal escenarioPrincipal;
    private Stage stage;

    /**
     * Método inicializador de la interfaz.
     * Se ejecuta cuando se carga el archivo FXML asociado.
     *
     * @param location  La ubicación utilizada para resolver rutas relativas de recursos.
     * @param resources Los recursos utilizados para localizar el archivo FXML.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Método inicializador de la interfaz, no contiene código en este caso
    }

    /**
     * Maneja el evento de acción para descargar un archivo PDF.
     * Abre un cuadro de diálogo de selección de archivo para guardar el archivo PDF.
     * Luego, copia un archivo PDF de origen a la ubicación seleccionada por el usuario.
     *
     * @param event El evento de acción desencadenado por el usuario.
     */
    @FXML
    private void descargarPdf(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar CV");
        fileChooser.setInitialFileName("cv.pdf"); // Nombre de archivo predeterminado al guardar

        File destinationFile = fileChooser.showSaveDialog(stage);

        if (destinationFile != null) {
            try (InputStream inputStream = getClass().getResourceAsStream("/org/kaledrod/resourse/CV.pdf")) {
                Path destinationPath = destinationFile.toPath();
                Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Devuelve el objeto Principal que representa la clase principal de la aplicación.
     *
     * @return El objeto Principal de la aplicación.
     */
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    /**
     * Establece el Stage (escenario) principal para el controlador.
     *
     * @param stage El Stage principal de la aplicación.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Establece el objeto Principal que representa la clase principal de la aplicación.
     *
     * @param escenarioPrincipal El objeto Principal de la aplicación.
     */
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    /**
     * Muestra el menú principal de la aplicación.
     */
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    /**
     * Cierra la aplicación.
     */
    public void buttonAccionCerrar() {
        System.exit(0);
    }

    /**
     * Minimiza la ventana de la aplicación.
     */
    public void buttonAccionMinimizar() {
        escenarioPrincipal.minimizarVentana();
    }
}
