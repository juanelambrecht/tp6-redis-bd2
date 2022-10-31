package ar.unrn.tp.jpa.servicios;

import java.lang.reflect.Type;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.modelo.Carrito;
import ar.unrn.tp.modelo.Clientes;
import ar.unrn.tp.modelo.NextNumber;
import ar.unrn.tp.modelo.Productos;
import ar.unrn.tp.modelo.Promociones;
import ar.unrn.tp.modelo.Tarjetas;
import ar.unrn.tp.modelo.Ventas;
import ar.unrn.tp.redis.RedisCache;

public class VentaJPA implements VentaService {

	@Override
	public void realizarVenta(Long idCliente, List<Long> productos, Long idTarjeta) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			Clientes cliente = em.find(Clientes.class, idCliente);
			Tarjetas tarjeta = em.find(Tarjetas.class, idTarjeta);

			TypedQuery<Productos> productosQuery = em.createQuery("select p from Productos p where p.id in :id",
					Productos.class);
			productosQuery.setParameter("id", productos);
			List<Productos> productosCompra = productosQuery.getResultList();

			TypedQuery<Promociones> promociones = em.createQuery(
					"select p from Promociones p where " + "?1 between p.fechaInicio and p.fechaFin",
					Promociones.class);
			promociones.setParameter(1, new Date(), TemporalType.DATE);
			List<Promociones> listaPromociones = promociones.getResultList();

			// Numero unico irrepetible --> Manejo de concurrencia
			int anioActual = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
			System.out.println(anioActual);
			TypedQuery<NextNumber> query = em.createQuery("from NextNumber where anio = :anioActual", NextNumber.class);
			query.setParameter("anioActual", anioActual);
			query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
			// NextNumber number = new NextNumber(anioActual, 1);
			// Si no existe creo un registro con el anio actual mas 1
			NextNumber number = query.getSingleResult();
			number.setearSiguiente();

			Carrito carrito = new Carrito((ArrayList<Promociones>) listaPromociones);
			carrito.agregarListaProductos((ArrayList<Productos>) productosCompra);
			carrito.agregarTarjeta(tarjeta);

			Ventas venta = carrito.realizarCompra(cliente);
			venta.setUniqueNumber(number.uniqueNumber());
			em.persist(venta);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}

	}

	@Override
	public float calcularMonto(List<Long> productos, Long idTarjeta) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		// EntityTransaction tx = em.getTransaction();
		try {
			TypedQuery<Promociones> promociones = em.createQuery(
					"select p from Promociones p where " + "?1 between p.fechaInicio and p.fechaFin",
					Promociones.class);
			promociones.setParameter(1, new Date(), TemporalType.DATE);
			List<Promociones> listaPromociones = promociones.getResultList();

			TypedQuery<Productos> productosQuery = em.createQuery("select p from Productos p where p.id in :id",
					Productos.class);
			productosQuery.setParameter("id", productos);
			List<Productos> listaProductos = productosQuery.getResultList();

			Carrito carrito = new Carrito((ArrayList<Promociones>) listaPromociones);
			carrito.agregarListaProductos((ArrayList<Productos>) listaProductos);

			Tarjetas tarjeta = em.find(Tarjetas.class, idTarjeta);
			carrito.agregarTarjeta(tarjeta);
			return (float) carrito.calculoPrecioTotal();

		} catch (Exception e) {
			// tx.rollback();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}

	}

	@Override
	public List<Ventas> ventas() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Ventas> ventas = em.createQuery("select v from Ventas v", Ventas.class);
			return ventas.getResultList();
		} catch (Exception e) {
			// tx.rollback();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

	@Override
	public List<Ventas> ventasCache(Long idCliente) {
		RedisCache cache = new RedisCache("127.0.0.1", 6379);
		List<Ventas> ventasCacheadas = null;
		Gson gson = new Gson();
		String key = "ventas:" + 1;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		TypedQuery<Ventas> ventas = em.createQuery("select v from Ventas v where v.cliente.id in :id order by id desc",
				Ventas.class);
		ventas.setParameter("id", idCliente);
		ventas.setMaxResults(3);
		List<Ventas> ultimasVentasJPA = ventas.getResultList();
		ventasCacheadas = new ArrayList<>();

		for (Ventas venta : ultimasVentasJPA) {
			
			System.out.println(venta.getUniqueNumber());
			ventasCacheadas.add(venta);
		}
		System.out.println(ventasCacheadas);
		System.out.println("ventas cacheadas");
		cache.set(key, gson.toJson(ventasCacheadas));
		System.out.println(ultimasVentasJPA.toString());

		return ultimasVentasJPA;
	}

	@Override
	public List<Ventas> getVentasCache(Long idCliente) {
		RedisCache cache = new RedisCache("127.0.0.1", 6379);
		Gson gson = new Gson();
		String key = "ventas:" + 1;
		Type type = new TypeToken<List<Ventas>>() {
		}.getType();
		List<Ventas> ventasCacheadas = gson.fromJson(cache.get(key), type);

		if (ventasCacheadas == null) {
			System.out.println("No estan cacheada las ventas");
			 ventasCacheadas = this.ventasCache(idCliente);
		} else {
			System.out.println("Ventas en cache");
		}

		return ventasCacheadas;
	}

}
