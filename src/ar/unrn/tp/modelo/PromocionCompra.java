package ar.unrn.tp.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;

@Entity
public class PromocionCompra extends Promociones {
	// private Date fechaInicio;
	// private Date fechaFin;

	private String tarjeta;
	private double descuento = 0.08;

	public PromocionCompra(Date fechaInicio, Date fechaFin, String tarjeta) {
		super(fechaInicio, fechaFin);
		this.tarjeta = tarjeta;
	}

	public PromocionCompra() {

	}

	@Override
	public double calculoPromocion(double totalN, ArrayList<Productos> productos, String tarjeta) {

		Date hoy = new Date();
		double total = totalN;

		if (hoy.before(this.fechaFin) && hoy.after(this.fechaInicio) && this.tarjeta.equals(tarjeta)) {

			for (Productos p : productos) {
				total = total + p.getPrecio();
			}

			total = total * this.descuento;
		}

		return total;
	}

	@Override
	public Map<String, Object> Map() {
		// TODO Auto-generated method stub
		return Map.of("tarjeta", tarjeta, "descuento", descuento, "marca", "");
	}

}
