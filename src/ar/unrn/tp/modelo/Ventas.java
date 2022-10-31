package ar.unrn.tp.modelo;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Ventas {
	@Id
	@GeneratedValue
	private Long id;
	Date fecha;
	LocalTime hora;
	@ManyToOne(fetch = FetchType.EAGER)
	Clientes cliente;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<Productos> productos;
	double montoTotal;
	private String uniqueNumber;

	public Ventas() {

	}

	public Ventas(Date fecha, Clientes cliente, ArrayList<Productos> productos, double montoTotal) {
		super();
		this.fecha = fecha;
		this.cliente = cliente;
		this.productos = productos;
		this.montoTotal = montoTotal;
	}

	@Override
	public String toString() {
		return "Ventas [id=" + id + ", fecha=" + fecha + ", hora=" + hora + ", cliente=" + cliente + ", productos="
				+ productos + ", montoTotal=" + montoTotal + ", uniqueNumber=" + uniqueNumber + "]";
	}

	public String getUniqueNumber() {
		return uniqueNumber;
	}

	public void setUniqueNumber(String uniqueNumber) {
		this.uniqueNumber = uniqueNumber;
	}

	public Map<String, Object> Map() {
		String productosList = "";

		for (Productos p : productos) {

			productosList += p.getDescripcion() + "\n";
		}

		return Map.of("id", id, "fecha", fecha.toLocaleString(), "cliente", cliente.getNombre(), "productos", productosList);
	}

}
