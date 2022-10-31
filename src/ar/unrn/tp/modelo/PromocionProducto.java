package ar.unrn.tp.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import javax.persistence.Entity;

@Entity
public class PromocionProducto extends Promociones {
	// private Date fechaInicio;
	// private Date fechaFin;

	private String marcaPromo;
	private double descuento = 0.05;

	public PromocionProducto(Date fechaInicio, Date fechaFin, String marca) {
		super(fechaInicio, fechaFin);
		this.marcaPromo = marca;
	}

	public PromocionProducto() {

	}

	@Override
	public double calculoPromocion(double totalN, ArrayList<Productos> productos, String tarjeta) {
		Date hoy = new Date();
		double total = 0;

		for (Productos p : productos) {
			if (hoy.before(this.fechaFin) && hoy.after(this.fechaInicio)
					&& p.getMarca().getNombreMarca().equals(this.marcaPromo)) {
				total = total + (p.getPrecio() * this.descuento);
			}
		}
		return total;
	}

	@Override
	public Map<String, Object> Map() {
		// TODO Auto-generated method stub
		return Map.of("marca", marcaPromo, "descuento", descuento, "tarjeta", "");
	}
}
