package ar.unrn.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.jpa.servicios.ClienteJPA;
import ar.unrn.tp.jpa.servicios.DescuentoJPA;
import ar.unrn.tp.jpa.servicios.ProductoJPA;
import ar.unrn.tp.jpa.servicios.VentaJPA;
import ar.unrn.tp.modelo.Promociones;
import ar.unrn.tp.modelo.Tarjetas;
import ar.unrn.tp.modelo.Ventas;
import ar.unrn.tp.web.WebAPI;

public class Main {

	public static void main(String[] args) {
		VentaService ventaCarrito = new VentaJPA();
		// ventaCarrito.ventasCache(1004L);
		// List<Ventas> ventas = ventaCarrito.getVentasCache(1004L);
		System.out.println("En main....");

		ClienteService client = new ClienteJPA();
		ProductoService prod = new ProductoJPA();
		DescuentoService descuento = new DescuentoJPA();
	
		WebAPI servicio = new WebAPI(client, descuento, prod, ventaCarrito, 1234);
		servicio.start();
	}

}
