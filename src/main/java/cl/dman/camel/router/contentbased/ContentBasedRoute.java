package cl.dman.camel.router.contentbased;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class ContentBasedRoute extends RouteBuilder {
	private final String origen;
	private final String destinoFacturas;
	private final String destinoGuia;
	private final String destinoOtro;

	public ContentBasedRoute(String origen, String destinoFacturas, String destinoGuia, String destinoOtro) {
		this.origen = origen;
		this.destinoFacturas = destinoFacturas;
		this.destinoGuia = destinoGuia;
		this.destinoOtro = destinoOtro;
	}

	public ContentBasedRoute() {
		this.origen = "FALTA DEFINIR ORIGEN";
		this.destinoFacturas = "FALTA DEFINIR DESTINO OTRO";
		this.destinoGuia = "FALTA DEFINIR DESTINO GUIA";
		this.destinoOtro = "FALTA DEFINIR DESTINO OTRO";
	}

	@Override
	public void configure() {
		from(origen)
			.unmarshal().json(JsonLibrary.Jackson, ContentRequest.class)
			.choice()
				.when(simple("${body.tipo} == 'Factura'"))
					.to(destinoFacturas)
				.when(simple("${body.tipo} == 'Guia Despacho'"))
					.to(destinoGuia)
				.otherwise()
					.to(destinoOtro)
		;
	}
}
