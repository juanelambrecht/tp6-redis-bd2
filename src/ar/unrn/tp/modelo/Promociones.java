package ar.unrn.tp.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public abstract class Promociones {
	@Id
	@GeneratedValue
	private Long id;
	protected Date fechaInicio;
	protected Date fechaFin;

	public Promociones(Date fechaInicio, Date fechaFin) {

		if (fechaInicio.equals(fechaFin) || fechaInicio.after(fechaFin)) {
			throw new RuntimeException("Fechas invalidas");
		}
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}

	public Promociones() {

	}

	double calculoPromocion(double total, ArrayList<Productos> productos, String tarjeta) {
		return 0;
	}

	public abstract Map<String, Object> Map();
}
