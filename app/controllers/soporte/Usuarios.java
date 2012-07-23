package controllers.soporte;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import enlace.lkAsunto;

import models.Asunto;
import models.Bitacora;
import play.Logger;
import play.Play;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.modules.paginate.ValuePaginator;
import play.mvc.Controller;

public class Usuarios extends Controller {

	/*
	 * Funciones relacionadas con las vistas
	 */

	public static void comentarios(Asunto bean_asunto) {
		ValuePaginator paginator2 = new ValuePaginator(Bitacora.listarTodos());
		paginator2.setPageSize(30);
		render(bean_asunto, paginator2);
	}

	public static void crearReporte() {
		render();
	}

	public static void index() {
		render();
	}

	public static void reporteDetalle(Asunto bean_asunto) {
		// ValuePaginator paginator3 = new
		// ValuePaginator(Bitacora.listarTodos());
		ValuePaginator paginator3 = new ValuePaginator(
				Bitacora.listaPerfiles(bean_asunto.asuntoID));
		paginator3.setPageSize(30);
		render(bean_asunto, paginator3);
	}

	public static void reportes(Asunto bean_asunto) {
		ValuePaginator paginator = new ValuePaginator(Asunto.listarTodos());
		paginator.setPageSize(30);
		render(paginator);
	}

	public static void verReporte() {
		ValuePaginator paginator = new ValuePaginator(Asunto.listarTodos());
		paginator.setPageSize(30);
		render(paginator);
	}

	public static void verReporteDetallePDF(Asunto bean_asunto) {
		String rutaPDF = "./public/reportes/" + bean_asunto.asuntoID + ".pdf";
		render(bean_asunto, rutaPDF);
	}

	/*
	 * Funciones Auxiliares para los Comentarios
	 */

	public static void agregarComentario_user(
			Asunto bean_asunto,
			@Required(message = "Agregue un Comentario") @MaxSize(100) String txComentario,
			File imImagen) {
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			comentarios(bean_asunto);
		} else {
			//Bitacora bitacora = new Bitacora(bean_asunto, "002", txComentario,
					//bean_asunto.fechaRegistro, bean_asunto.estatus);
			Logger.info("Imagen Nula");
			lkAsunto.agregarimagen(bean_asunto, "002", txComentario, imImagen);
			comentarios(bean_asunto);
		}
	}

	public static void grabar(
			@Required(message = "Capture el Título") @MaxSize(100) String txTitulo,
			@Required(message = "Capture el Detalle") @MaxSize(250) String txDetalle,
			File imImagen) {
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			crearReporte();
		} else {
			Asunto reporte = new Asunto(120, "00001", txTitulo, txDetalle);
			reporte.save();
			lkAsunto.agregarimagen(reporte, "", "", imImagen);
			verReporte();
		}
	}

	/*
	 * Funciones Auxiliares para los Reportes
	 */

	public static void descargar_reporte(Asunto bean_asunto) {
		Map reportParams = new HashMap();
		String rutaPDF = "./public/reportes/" + bean_asunto.asuntoID + ".pdf";
		renderBinary(reports.ReportesPDF.generateReport("reporte_usuarios",
				reportParams, bean_asunto, rutaPDF), "Reporte_Usuarios.pdf");
	}

	public static void descargar_reporte_detalle(Asunto bean_asunto) {
		Map reportParams = new HashMap();
		String rutaPDF = "./public/reportes/" + bean_asunto.asuntoID + ".pdf";
		renderBinary(reports.Reporte_DetallePDF.generateReport(
				"Reporte_Detalle", reportParams, bean_asunto, rutaPDF),
				"Reporte_Detalle.pdf");
	}

	public static void descargar_reporteXLS(Asunto bean_asunto) {
		Map reportParams = new HashMap();
		String rutaXLS = "./public/reportes/" + bean_asunto.asuntoID + ".xls";
		renderBinary(reports.ReportesXLS.generateReport("reporte_usuariosXLS",
				reportParams, bean_asunto, rutaXLS), "reporte_usuariosXLS.xls");
	}

	public static void descargar_reporte_detalleXLS(Asunto bean_asunto) {
		Map reportParams = new HashMap();
		String rutaXLS = "./public/reportes/" + bean_asunto.asuntoID + ".xls";
		renderBinary(reports.Reporte_DetalleXLS.generateReport(
				"Reporte_DetalleXLS", reportParams, bean_asunto, rutaXLS),
				"Reporte_DetalleXLS.xls");
	}

	public static void reporte_usuarios() {
		render();
	}

}