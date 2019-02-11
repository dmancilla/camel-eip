package cl.dman.camel.router.splitter;

import cl.dman.camel.router.DocumentoRequest;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.springframework.stereotype.Component;

/**
 * Ruta que recibe un mensaje con documento, y lo separa en  el mensaje de acuerdo al contenido
 */
@Component
public class SplitterRoute extends RouteBuilder {
	private final String origen;
	private final String destino;

	//Constructor necesario para crear los endpoints de Test
	public SplitterRoute(String origen, String destino) {
		this.origen = origen;
		this.destino = destino;
	}

	//Constructor necesario para crear los endpoints reales
	public SplitterRoute() {
		this.origen = "rest:post:filter";
		this.destino = "stream:out";
	}

	@Override
	public void configure() {
		JacksonDataFormat format = new ListJacksonDataFormat(DocumentoRequest.class);

		from(origen)

			//Transformación de JSON al tipo de dato interno List<DocumentoRequest>
			.unmarshal(format)

			.split(body())
			.to(destino)
			.end()

			.transform().simple("OK")
		;
	}
}
