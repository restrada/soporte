package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.db.jpa.GenericModel;

@Entity
public class Asunto extends GenericModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long asuntoID;
	public int sucursalID;

	@Column(length = 20)
	public String clavePersona;

	@Column(length = 100)
	public String titulo;

	@Column(length = 250)
	public String detalle;
	public Date fechaRegistro;
	public Date fechaRespuesta;
	public int estatus;
	public int tipo;
	public int prioridad;

	@Column(length = 5, precision = 2)
	public double SLA;

	public Asunto(Long asuntoID, int sucursalID, String clavePersona,
			String titulo, String detalle, Date fechaRegistro,
			Date fechaRespuesta, int estatus, int tipo, int prioridad,
			double sLA) {

		this.asuntoID = asuntoID;
		this.sucursalID = sucursalID;
		this.clavePersona = clavePersona;
		this.titulo = titulo;
		this.detalle = detalle;
		this.fechaRegistro = fechaRegistro;
		this.fechaRespuesta = fechaRespuesta;
		this.estatus = estatus;
		this.tipo = tipo;
		this.prioridad = prioridad;
		SLA = sLA;
	}

	public Asunto(int sucursalID, String clavePersona, String titulo,
			String detalle) {

		this.sucursalID = sucursalID;
		this.clavePersona = clavePersona;
		this.titulo = titulo;
		this.detalle = detalle;
		this.fechaRegistro = new Date();
		this.estatus = 1;
	}

	public static List<Asunto> listarTodos() {
		return Asunto.findAll();
	}

	/* Ordena por fechas */
	public static List<Asunto> lista() {
		return Asunto.find("order by Fecha").fetch();
	}

	/* Filtra por Asunto los Comentarios */
	public static List<Asunto> listaPerfiles(Long asuntoID) {
		return Asunto.find("byAsuntoID", asuntoID).fetch();
	}
}