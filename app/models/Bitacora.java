package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.db.jpa.GenericModel;

@Entity
public class Bitacora extends GenericModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long bitacoraID;

	@ManyToOne
	@JoinColumn(name = "asuntoID")
	public Asunto asunto;
	@Column(length = 20)
	public String clavePersona_Atiende;

	@Column(length = 100)
	public String comentario;
	public Date fecha;
	public int estatus;
	@Column(length = 250)
	public String imagen;

	public Bitacora(Asunto asunto, String clavePersona_Atiende,
			String comentario, Date fecha, int estatus) {
		this.asunto = asunto;
		this.clavePersona_Atiende = clavePersona_Atiende;
		this.comentario = comentario;
		this.fecha = fecha;
		this.estatus = estatus;
	}

	public static java.util.List<Bitacora> listarTodos() {
		return Bitacora.findAll();
	}

	/* Ordena por Fechas */
	public static List<Bitacora> lista() {
		return Bitacora.find("order by Fecha").fetch();
	}

	/* Filtra por Asunto los Comentarios */
	public static List<Bitacora> listaPerfiles(Long asuntoID) {
		return Bitacora.find("byAsuntoID", asuntoID).fetch();
	}
}