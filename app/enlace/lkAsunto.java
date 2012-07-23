package enlace;

import java.io.File;
import java.util.Date;

import models.Asunto;
import models.Bitacora;

import play.Logger;
import play.Play;

public class lkAsunto {
	/*
	 * Para Agregar la Imagen del Asunto
	 */

	public static boolean agregarimagen(Asunto bean_asunto, String Usuario,
			String Comentario, File imImagen) {
		if (imImagen == null) {
			Logger.info("Imagen Nula");
			return false;
		}

		File newFile = Play.getFile("/public/reportes/" + imImagen.getName());

		imImagen.renameTo(newFile);
		imImagen.delete();
		Logger.info("Success " + newFile.getAbsolutePath());

		Bitacora bitacora = new Bitacora(bean_asunto, Usuario, Comentario,
				new Date(), bean_asunto.estatus);
		bitacora.imagen = newFile.getName();
		bitacora.save();
		return true;
	}
}