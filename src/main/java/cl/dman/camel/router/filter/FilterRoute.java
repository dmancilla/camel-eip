package cl.dman.camel.router.filter;

import cl.dman.camel.router.DocumentoRequest;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

/**
 * Ruta que recibe un mensaje, y lo filtra de acuerdo al contenido
 */
@Component
public class FilterRoute extends RouteBuilder {
	private final String origen;
	private final String destino;

	//Constructor necesario para crear los endpoints de Test
	public FilterRoute(String origen, String destino) {
		this.origen = origen;
		this.destino = destino;
	}

	//Constructor necesario para crear los endpoints reales
	public FilterRoute() {
		this.origen = "rest:post:filter";
		this.destino = "stream:out";
	}

	@Override
	public void configure() {
		JacksonDataFormat format = new JacksonDataFormat(DocumentoRequest.class);

		from(origen)

			//Transformación de JSON al tipo de dato interno DocumentoRequest
			.unmarshal(format)

			//Se crea un filtro, que sólo deja pasar Facturas y Notas de Crédito
			.filter().simple("${body.tipo} == 'Factura' || ${body.tipo} == 'Nota de Credito'")

			.to(destino)

			.transform().simple("OK")
		;
	}
}
