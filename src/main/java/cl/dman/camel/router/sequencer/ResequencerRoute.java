package cl.dman.camel.router.sequencer;

import cl.dman.camel.router.DocumentoRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.config.BatchResequencerConfig;
import org.springframework.stereotype.Component;

/**
 * Ruta que recibe varios mensajes desordenados, y los ordena mediante un criterio
 */
@Component
public class ResequencerRoute extends RouteBuilder {
	private final String origen;
	private final String destino;

	//Constructor necesario para crear los endpoints de Test
	public ResequencerRoute(String origen, String destino) {
		this.origen = origen;
		this.destino = destino;
	}

	//Constructor necesario para crear los endpoints reales
	public ResequencerRoute() {
		this.origen = "rest:post:resequencer";
		this.destino = "stream:out";
	}

	@Override
	public void configure() {
		JacksonDataFormat format = new JacksonDataFormat(DocumentoRequest.class);
		BatchResequencerConfig batchConfig = new BatchResequencerConfig(100, 5000L);

		from(origen)

			//Transformación de JSON al tipo de dato interno List<DocumentoRequest>
			.unmarshal(format)

			//Ordena los mensajes por numero
			.resequence(simple("body.numero"))
			.batch(batchConfig)

			.to(destino)
		;
	}
}
