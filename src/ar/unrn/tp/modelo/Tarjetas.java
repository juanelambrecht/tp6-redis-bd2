package ar.unrn.tp.modelo;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Tarjetas {
	@Id
	@GeneratedValue
	private Long id;
	String descripcion, banco, digitos;
	double saldo;
	int idTarjeta;

	public Tarjetas() {
		// TODO Auto-generated constructor stub
	}

	public Tarjetas(String digitos, String descripcion, String banco, double saldo) {
		super();
		this.digitos = digitos;
		this.descripcion = descripcion;
		this.banco = banco;
		this.saldo = saldo;
	}

	public String bancoTarjeta() {
		return this.banco;
	}

	public int getIdTarjeta() {
		return idTarjeta;
	}

	@Override
	public String toString() {
		return "Tarjetas [descripcion=" + descripcion + ", banco=" + banco + ", digitos=" + digitos + ", saldo=" + saldo
				+ "]";
	}

	public String getBanco() {
		return banco;
	}

	public void descontarCompra(double monto) {
		this.saldo = this.saldo - monto;
	}

	public Map<String, Object> Map() {
		return Map.of("id", id, "descripcion", descripcion, "banco", banco, "digitos", digitos, "saldo", saldo);
	}

}
