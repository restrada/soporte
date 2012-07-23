package controllers.soporte;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import enlace.lkAsunto;

import models.Asunto;
import models.Bitacora;
import play.Logger;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.modules.paginate.ValuePaginator;
import play.mvc.Controller;

public class Sistemas extends Controller {

	/*
	 * Funciones relacionadas con las vistas
	 */
	public static void activar(
			Asunto bean_asunto,
			@Required(message = "Seleccione el Tipo de Problema") int cbTipo,
			@Required(message = "Seleccione el Nivel de Prioridad") int cbPrioridad) {
		if (validation.hasErrors() || (cbTipo == 0 || cbPrioridad == 0)) {
			if (cbTipo == 0)
				validation.addError("cbTipo",
						"Seleccione el Problema y la Prioridad", "");
			params.flash();
			validation.keep();
			detalle(bean_asunto);
		} else {
			bean_asunto.tipo = cbTipo;
			bean_asunto.prioridad = cbPrioridad;
			bean_asunto.estatus = 2;
			bean_asunto.save();
			render();
		}
	}

	public static void detalle(Asunto bean_asunto) {
		render(bean_asunto);
	}
	
	public static void detSeguimiento(Asunto bean_asunto) {
		// ValuePaginator paginator2 = new
		// ValuePaginator(Bitacora.listarTodos());
		ValuePaginator paginator2 = new ValuePaginator(
				Bitacora.listaPerfiles(bean_asunto.asuntoID));
		paginator2.setPageSize(30);
		render(bean_asunto, paginator2);
	}

	public static void index() {
		render();
	}

	public static void pendientes() {
		ValuePaginator paginator = new ValuePaginator(Asunto.listarTodos());
		paginator.setPageSize(30);
		render(paginator);
	}

	public static void seguimiento() {
		ValuePaginator paginator = new ValuePaginator(Asunto.listarTodos());
		paginator.setPageSize(30);
		render(paginator);
	}

	public static void verReportes(Asunto bean_asunto) {
		String rutaPDF = "./public/reportes/" + bean_asunto.asuntoID + ".pdf";
		render(bean_asunto, rutaPDF);
	}

	/*
	 * Funciones Auxiliares para los Comentarios
	 */

	// Aquí vamos a grabar un comentario en la tabla bitácora (Modelo Bitácora)
	public static void agregarComentario(
			Asunto bean_asunto,
			@Required(message = "Agregue un Comentario") @MaxSize(100) String txComentario,
			File imImagen) {
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			detSeguimiento(bean_asunto);
		} else {
			Logger.info("Imagen Nula");
			lkAsunto.agregarimagen(bean_asunto, "002", txComentario, imImagen);
			detSeguimiento(bean_asunto);
		}
	}

	public static void terminarAsunto(Asunto bean_asunto,
			@MaxSize(100) String txComentario) {
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			detSeguimiento(bean_asunto);
		} else {
			bean_asunto.fechaRespuesta = new Date();
			bean_asunto.estatus = 3;
			bean_asunto.save();
			Bitacora bitacora = new Bitacora(bean_asunto, "002", txComentario,
					bean_asunto.fechaRespuesta, bean_asunto.estatus);
			bitacora.save();
			detSeguimiento(bean_asunto);
		}
	}

	public static void verreportedetalle(Asunto bean_asunto) {
		ValuePaginator paginator3 = new ValuePaginator(Bitacora.listarTodos());
		paginator3.setPageSize(30);
		render(bean_asunto, paginator3);
	}

	/*
	 * Funciones Auxiliares para los Reportes
	 */

	public static void descargar_reporte(Asunto bean_asunto) {
		Map reportParams = new HashMap();
		String rutaPDF = "./public/reportes/" + bean_asunto.asuntoID + ".pdf";
		renderBinary(reports.ReportesPDF.generateReport("reporte_sistemas",
				reportParams, bean_asunto, rutaPDF), "Reporte_Sistemas.pdf");
	}

	public static void descargar_reporteXLS(Asunto bean_asunto) {
		Map reportParams = new HashMap();
		String rutaXLS = "./public/reportes/" + bean_asunto.asuntoID + ".xls";
		renderBinary(reports.ReportesXLS.generateReport("reporte_sistemasXLS",
				reportParams, bean_asunto, rutaXLS), "Reporte_Sistemas.xls");
	}
}