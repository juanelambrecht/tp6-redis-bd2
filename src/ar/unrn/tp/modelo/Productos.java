package ar.unrn.tp.modelo;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Productos {
	@Id
	@GeneratedValue
	private Long id;
	private int codigo; // codigo con el que se identifica univocamente
	private String descripcion;
	private double precio;
	@ManyToOne(fetch = FetchType.EAGER)
	private CategoriaProducto categoria;
	@ManyToOne(fetch = FetchType.EAGER)
	private Marca marca;
	private Long version;

	public Productos() {
		// TODO Auto-generated constructor stub
	}

	public Productos(int codigo, String descripcion, double precio, CategoriaProducto categoria, Marca marca) {
		super();
		if (descripcion.isEmpty()) {
			throw new RuntimeException("La descripcion no puede estar vacio");
		}
		if (categoria.descripcionCategoria().isEmpty()) {
			throw new RuntimeException("La categoria no puede estar vacio");
		}
		if (precio == 0) {
			throw new RuntimeException("El precio no puede estar vacio");
		}
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.precio = precio;
		this.categoria = categoria;
		this.marca = marca;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public CategoriaProducto getCategoria() {
		return categoria;
	}

	public Marca getMarca() {
		return marca;
	}

	@Override
	public String toString() {
		return "Productos [codigo=" + codigo + ", descripcion=" + descripcion + ", precio=" + precio + ", categoria="
				+ categoria + ", marca=" + marca + "]";
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setCategoria(CategoriaProducto categoria) {
		this.categoria = categoria;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public Map<String, Object> Map() {

		return Map.of("id", id, "codigo", codigo, "descripcion", descripcion, "precio", precio, "categoria",
				categoria.descripcionCategoria(), "marca", marca.getNombreMarca());
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getLista(List<Productos> productos) {

		String productosList = "";

		for (Productos p : productos) {

			productosList += p.getDescripcion() + "\n";
		}

		return productosList;
	}
}
