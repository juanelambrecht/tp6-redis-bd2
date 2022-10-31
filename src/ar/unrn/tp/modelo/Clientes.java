package ar.unrn.tp.modelo;

import java.util.regex.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.*;

@Entity
public class Clientes {

	private String nombre, apellido, dni, email;
	@Id
	@GeneratedValue
	private Long id;

	@OneToMany(cascade = CascadeType.PERSIST)
	private List<Tarjetas> tarjetas = new ArrayList<Tarjetas>();

	public Clientes() {

	}

	public Clientes(String nombre, String apellido, String dni, String email) {
		if (nombre.isEmpty()) {
			throw new RuntimeException("El nombre no puede estar vacio");
		}
		if (apellido.isEmpty()) {
			throw new RuntimeException("El apellido no puede estar vacio");
		}
		if (dni.isEmpty()) {
			throw new RuntimeException("El dni no puede estar vacio");
		}
		if (email.isEmpty()) {
			throw new RuntimeException("El email no puede estar vacio");
		}

		if (!this.validarMail(email)) {
			throw new RuntimeException("El email no es valido");
		}

		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.email = email;
	}

	public void agregarTarjeta(Tarjetas tarjeta) {
		this.tarjetas.add(tarjeta);
	}

	@Override
	public String toString() {
		return "Clientes [nombre=" + nombre + ", apellido=" + apellido + ", dni=" + dni + ", email=" + email
				+ ", tarjetas=" + tarjetas + "]";
	}

	private boolean validarMail(String email) {
		String regx = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTarjetas(List<Tarjetas> tarjetas) {
		this.tarjetas = tarjetas;
	}

	public List<Tarjetas> getTarjetas() {
		return tarjetas;
	}

	public Tarjetas getTarjetaPorId(Long idTarjeta) {

		for (Tarjetas t : tarjetas) {
			if (t.getIdTarjeta() == idTarjeta) {
				return t;
			}
		}
		return null;
	}

	public void mostrarTarjetas() {

		for (Tarjetas t : tarjetas) {
			System.out.println(t.toString());
		}

	}

	public String getNombre() {
		return nombre + " " + apellido;
	}

	public Map<String, Object> Map() {
		List<Map<String, Object>> tarjetasMap = new ArrayList<>();
		if (tarjetas != null) {
			for (Tarjetas tarjeta : tarjetas) {
				tarjetasMap.add(tarjeta.Map());
			}
		}
		return Map.of("id", id, "nombre", nombre, "apellido", apellido, "dni", dni, "email", email, "tarjetas",
				tarjetasMap);
	}

}
