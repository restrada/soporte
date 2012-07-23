package reports;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import play.Play;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import models.Asunto;

public class ReportesXLS {

	static String REPORT_DEFINITION_PATH = "./public/jasper/";

	public static InputStream generateReport(String reportDefFile,
			Map reportParams, Asunto bean_asunto, String rutaXLS) {

		File fileLogo = Play.getFile("./public/images/");
		
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
			JRExporter exporter = new JExcelApiExporter();

			/*
			 * Asigna a nuestro objeto "exporter" el "jasperPrint" (el reporte
			 * con datos) que creamos anteriormente.
			 */
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jrprint);

			exporter.setParameter(
					JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
					Boolean.TRUE);

			// exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE,
			// outFileName);

			// exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,
			// rutaXLS); Empiezo mis métodos después de os.

			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);

			/* Realiza el proceso de exportación */
			exporter.exportReport();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(
				((ByteArrayOutputStream) os).toByteArray());
	}
}