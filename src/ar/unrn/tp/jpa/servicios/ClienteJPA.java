package ar.unrn.tp.jpa.servicios;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.modelo.Clientes;
import ar.unrn.tp.modelo.Tarjetas;
import ar.unrn.tp.modelo.Ventas;

public class ClienteJPA implements ClienteService {

	@Override
	public void crearCliente(String nombre, String apellido, String dni, String email) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Clientes cliente = new Clientes(nombre, apellido, dni, email);
			em.persist(cliente);
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
	public void modificarCliente(Long idCliente, String nombre, String apellido, String dni, String email) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			Clientes cliente = em.getReference(Clientes.class, idCliente);
			cliente.setNombre(nombre);
			cliente.setApellido(apellido);
			cliente.setDni(dni);
			cliente.setEmail(email);

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
	public void agregarTarjeta(Long idCliente, String digitos, String descripcion, String banco, double saldo) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			Clientes cliente = em.getReference(Clientes.class, idCliente);

			if (cliente != null) {
				Tarjetas tarjeta = new Tarjetas(digitos, descripcion, banco, saldo);
				cliente.agregarTarjeta(tarjeta);
			}

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
	public List<Tarjetas> listarTarjetas(Long idCliente) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		try {
			Clientes cliente = em.find(Clientes.class, idCliente);
			cliente.mostrarTarjetas();
			return cliente.getTarjetas();
		} catch (Exception e) {
			// tx.rollback();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

	@Override
	public List<Clientes> listarClientes() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-objectdb");
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Clientes> Clientes = em.createQuery("select c from Clientes c", Clientes.class);
			return Clientes.getResultList();
		} catch (Exception e) {
			// tx.rollback();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}

	}

}
