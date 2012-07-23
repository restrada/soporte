package reports;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Map;

import models.Asunto;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.commons.io.output.ByteArrayOutputStream;

import play.Logger;
import play.Play;

public class Reporte_DetallePDF {

	static String REPORT_DEFINITION_PATH = "./public/jasper/";

	public static InputStream generateReport(String reportDefFile,
			Map reportParams, Asunto bean_asunto, String rutaPDF) {

		File file = Play.getFile("./public/reportes/");
		File fileLogo = Play.getFile("./public/images/");
		Logger.info("Probando la ruta de imagen" + file.getAbsolutePath());

		Calendar hoy = Calendar.getInstance();
		Logger.info("Este es el ID que manda " + bean_asunto.asuntoID);

		reportParams.put("fechaActual", hoy.get(Calendar.DAY_OF_MONTH) + "/"
				+ (hoy.get(Calendar.MONTH) + 1) + "/" + hoy.get(Calendar.YEAR));

		reportParams.put("bean_asunto.asuntoID", bean_asunto.asuntoID);

		reportParams.put("titulo", "Reporte Detalle del Asunto: "
				+ bean_asunto.asuntoID);

		reportParams.put("ruta", file.getAbsolutePath() + "\\");
		
		reportParams.put("rutaLogo", fileLogo.getAbsolutePath() + "\\");

		OutputStream os = new ByteArrayOutputStream();
		try {

			String compiledFile = REPORT_DEFINITION_PATH + reportDefFile
					+ ".jasper";

			JasperCompileManager.compileReportToFile(REPORT_DEFINITION_PATH
					+ reportDefFile + ".jrxml", compiledFile);
			/*
			 * Nos permite llenar el reporte con datos obtenidos de distintas
			 * fuentes de datos En este caso la fuente de datos es la conexión
			 * directa a la base de datos que creamos:
			 */
			JasperPrint jrprint = JasperFillManager.fillReport(compiledFile,
					reportParams, play.db.DB.getConnection());

			// File reporte = new File("./app/reports/reporte_usuarios.pdf");
			/*
			 * Permite generar el reporte en distintos formatos (PDF,Excel,HTML)
			 */
			JRExporter exporter = new JRPdfExporter();

			/*
			 * Asigna a nuestro objeto "exporter" el "jasperPrint" (el reporte
			 * con datos) que creamos anteriormente.
			 */
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jrprint);
			// exporter.setParameter(JRExporterParameter.OUTPUT_FILE, reporte);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
			/* Realiza el proceso de exportación */
			JasperExportManager.exportReportToPdfFile(jrprint, rutaPDF);
			exporter.exportReport();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(
				((ByteArrayOutputStream) os).toByteArray());
	}
}