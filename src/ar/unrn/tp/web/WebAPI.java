package ar.unrn.tp.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.modelo.Clientes;
import ar.unrn.tp.modelo.Productos;
import ar.unrn.tp.modelo.Promociones;
import ar.unrn.tp.modelo.Tarjetas;
import ar.unrn.tp.modelo.Ventas;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import com.google.gson.Gson;

public class WebAPI {
	ClienteService clienteService;
	DescuentoService descuentoService;
	ProductoService productosService;
	VentaService ventasService;
	int puerto;

	public WebAPI(ClienteService clienteService, DescuentoService descuentos, ProductoService productosService,
			VentaService ventasService, int puerto) {
		this.clienteService = clienteService;
		this.descuentoService = descuentos;
		this.productosService = productosService;
		this.ventasService = ventasService;
		this.puerto = puerto;
	}

	public void start() {
		Javalin javalin = Javalin.create(config -> {
			config.enableCorsForAllOrigins();
		}).start(this.puerto);
		javalin.get("/ventasService/precio", obtenerPrecio());
		javalin.get("/clientes", Clientes());
		javalin.get("/clientes/tarjetas/{id}", Tarjetas());
		javalin.get("/productos", Productos());
		javalin.get("/descuentos", Descuentos());
		javalin.get("/ultimasventas/{id}", ultimasVentas());
		javalin.post("/clienteNuevo", newCliente());
		javalin.post("/cliente/tarjetas/{id}", newTarjeta());
		javalin.post("/ventaNueva", newVenta());
		javalin.put("/clienteService/{id}", modificarCliente());
		javalin.exception(Exception.class, (e, ctx) -> {
			ctx.json(Map.of("result", "error", "message", "algo salio mal.: " + e.getMessage())).status(400);
		});
	}

	private Handler newCliente() {
		return ctx -> {
			ClienteDTO dto = ctx.bodyAsClass(ClienteDTO.class);
			this.clienteService.crearCliente(dto.getNombre(), dto.getApellido(), dto.getDni(), dto.getEmail());
			ctx.json(Map.of("result", "success"));
		};
	}

	private Handler Clientes() {
		return ctx -> {
			var client = this.clienteService.listarClientes();
			var list = new ArrayList<Map<String, Object>>();
			for (Clientes cliente1 : client) {
				list.add(cliente1.Map());
			}
			ctx.json(Map.of("result", "success", "clientes", list));
		};
	}

	private Handler modificarCliente() {
		return ctx -> {
			ClienteDTO dto = ctx.bodyAsClass(ClienteDTO.class);
			Long id = Long.valueOf(ctx.pathParam("id"));
			this.clienteService.modificarCliente(id, dto.getNombre(), dto.getApellido(), dto.getDni(), dto.getEmail());
			ctx.json(Map.of("result", "success"));
		};
	}

	private Handler newTarjeta() {
		return ctx -> {
			TarjetaDTO tarjetaDto = ctx.bodyAsClass(TarjetaDTO.class);
			Long id = Long.valueOf(ctx.pathParam("id"));
			this.clienteService.agregarTarjeta(id, tarjetaDto.getDigito(), tarjetaDto.getDescripcion(),
					tarjetaDto.getBanco(), tarjetaDto.getSaldo());
			ctx.json(Map.of("result", "success"));
		};
	}

	private Handler Tarjetas() {
		return ctx -> {
			var id = Long.valueOf(ctx.pathParam("id"));
			var tarjetasBD = this.clienteService.listarTarjetas(id);
			var list = new ArrayList<Map<String, Object>>();
			for (Tarjetas t : tarjetasBD) {
				list.add(t.Map());
			}
			ctx.json(Map.of("result", "success", "tarjetas", list));
		};
	}

	private Handler Productos() {
		return ctx -> {
			var productosBD = this.productosService.listarProductos();
			var list = new ArrayList<Map<String, Object>>();
			for (Productos producto : productosBD) {
				list.add(producto.Map());
			}
			ctx.json(Map.of("result", "success", "productos", list));
		};
	}

	private Handler Descuentos() {
		return ctx -> {
			var descuentos = this.descuentoService.listarDescuentos();
			var list = new ArrayList<Map<String, Object>>();
			for (Promociones descuento : descuentos) {
				System.out.println(descuento.Map());
				list.add(descuento.Map());
			}
			ctx.json(Map.of("result", "success", "descuentos", list));
		};
	}

	private Handler newVenta() {
		return ctx -> {
			Gson gson = new Gson();
			var idCliente = ctx.queryParam("cliente");
			var idTarjeta = ctx.queryParam("tarjeta");
			var productosService = ctx.bodyAsClass(Long[].class);
			List<Long> productosServiceList = Arrays.asList(productosService);
			// List<Long> productosServiceList = new ArrayList<Long>();
			// productosServiceList.add(1009L);
			Long clienteId = gson.fromJson(idCliente, Long.class);
			Long tarjetaId = gson.fromJson(idTarjeta, Long.class);

			this.ventasService.realizarVenta(clienteId, productosServiceList, tarjetaId);

			ctx.json(Map.of("result", "success", "message", "Venta realizada con exito"));
		};
	}

	private Handler obtenerPrecio() {
		return ctx -> {
			Gson gson = new Gson();
			var idTarjeta = ctx.queryParam("tarjeta");
			var productosService = ctx.queryParam("productosService");

			Long[] prodsIds = gson.fromJson(productosService, Long[].class);
			List<Long> productosServiceList = Arrays.asList(prodsIds);

			Long tarjeta = gson.fromJson(idTarjeta, Long.class);

			var precio = this.ventasService.calcularMonto(productosServiceList, tarjeta);

			ctx.json(Map.of("result", "success", "precio", precio));
		};
	}

	private Handler ultimasVentas() {
		return ctx -> {
			Gson gson = new Gson();
			var id = Long.valueOf(ctx.pathParam("id"));
			System.out.println(id);

			List<Ventas> vent = this.ventasService.getVentasCache(id);
			var list = new ArrayList<Map<String, Object>>();
			for (Ventas v : vent) {
				list.add(v.Map());
			}
			ctx.json(Map.of("result", "success", "ventas", list));
		};
	}
}
