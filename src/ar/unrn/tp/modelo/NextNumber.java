package ar.unrn.tp.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class NextNumber {
	@Id
	private Long id;

	private int anio;
	private int numero;

	protected NextNumber() {

	}

	public NextNumber(int anio, int numero) {
		super();
		this.anio = anio;
		this.numero = numero;
	}

	public void setearSiguiente() {
		this.numero = this.numero() + 1;
	}

	public int anio() {
		return anio;
	}

	public int numero() {
		return numero;
	}

	public String uniqueNumber() {
		return this.numero + "-" + this.anio;
	}

}
