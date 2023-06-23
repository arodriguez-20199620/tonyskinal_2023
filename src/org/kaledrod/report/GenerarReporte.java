package org.kaledrod.report;

import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.kaledrod.db.Conexion;

/**
 * Clase para generar y mostrar reportes utilizando JasperReports.
 */
public class GenerarReporte {

    /**
     * Muestra un reporte en la interfaz de usuario utilizando JasperReports.
     *
     * @param nombreReporte El nombre del archivo del reporte.
     * @param titulo        El título del visor del reporte.
     * @param parametros    Los parámetros a pasar al reporte.
     */
    public static void mostarReporte(String nombreReporte, String titulo, Map<String, Object> parametros) {
        // Carga el archivo del reporte desde el recurso
        InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);

        try {
            // Carga el reporte desde el archivo
            JasperReport reporteMaestro = (JasperReport) JRLoader.loadObject(reporte);

            // Llena el reporte con los datos y parámetros proporcionados
            JasperPrint reporteImpreso = JasperFillManager.fillReport(
                    reporteMaestro,
                    parametros,
                    Conexion.getInstance().getConexion()
            );

            // Crea un visor de reporte y lo muestra en la interfaz de usuario
            JasperViewer visor = new JasperViewer(reporteImpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
