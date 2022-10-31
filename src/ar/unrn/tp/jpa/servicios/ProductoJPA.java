package ar.unrn.tp.jpa.servicios;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.modelo.CategoriaProducto;
import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.Productos;

public class ProductoJPA implements ProductoService {

	@Override
	public void crearProducto(int codigo, String descripcion, float precio, Long IdCategoria, Long idMarca) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			CategoriaProducto categoria = em.find(CategoriaProducto.class, IdCategoria);
			Marca marca = em.find(Marca.class, idMarca);
			Productos producto = new Productos(codigo, descripcion, precio, categoria, marca);
			em.persist(producto);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

	@Override
	public void modificarProducto(Long idProducto, int codigo, String descripcion, double precio, Long idCategoria,
			Long idMarca, Long version) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			CategoriaProducto categoria = em.find(CategoriaProducto.class, idCategoria);
			Marca marca = em.find(Marca.class, idMarca);
			Productos producto = em.getReference(Productos.class, idProducto);

			producto.setCodigo(codigo);
			producto.setDescripcion(descripcion);
			producto.setPrecio(precio);
			producto.setMarca(marca);
			producto.setCategoria(categoria);
			producto.setVersion(version);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}

	}

	@Override
	public List<Productos> listarProductos() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Productos> productos = em.createQuery("select p from Productos p", Productos.class);
			return productos.getResultList();
		} catch (Exception e) {
			// tx.rollback();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

	@Override
	public void crearCategoriaProducto(String nombreCategoria, String descripcion) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			CategoriaProducto categoria = new CategoriaProducto(nombreCategoria, descripcion);
			em.persist(categoria);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

	@Override
	public void crearMarcaProducto(String nombreMarca, String descripcion) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Marca marca = new Marca(nombreMarca, descripcion);
			em.persist(marca);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}

	}

}
