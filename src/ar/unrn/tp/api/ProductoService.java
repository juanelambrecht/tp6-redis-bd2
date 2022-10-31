package ar.unrn.tp.api;

import java.util.List;

import ar.unrn.tp.modelo.CategoriaProducto;
import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.Productos;

public interface ProductoService {
	// validar que sea una categor�a existente y que codigo no se repita
	void crearProducto(int codigo, String descripcion, float precio, Long IdCategoria, Long idMarca);

	// Devuelve todos los productos
	List<Productos> listarProductos();

	void crearCategoriaProducto(String nombreCategoria, String descripcion);

	void crearMarcaProducto(String nombreMarca, String descripcion);

	void modificarProducto(Long idProducto, int codigo, String descripcion, double precio, Long idCategoria,
			Long idMarca, Long version);

}
