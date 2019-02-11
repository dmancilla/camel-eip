package cl.dman.camel.router.contentbased;

import cl.dman.camel.router.DocumentoRequest;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

/**
 * Ruta que recibe un documento, y rutea a diferentes endpoints de acuerdo al contenido
 */
@Component
public class ContentBasedRoute extends RouteBuilder {
	private final String origen;
	private final String destinoFact;
	private final String destinoGuia;
	private final String destinoOtro;

	//Constructor necesario para crear los endpoints de Test
	public ContentBasedRoute(String origen, String destinoFact, String destinoGuia, String destinoOtro) {
		this.origen = origen;
		this.destinoFact = destinoFact;
		this.destinoGuia = destinoGuia;
		this.destinoOtro = destinoOtro;
	}

	//Constructor necesario para crear los endpoints reales
	public ContentBasedRoute() {
		this.origen = "rest:post:content-based";
		this.destinoFact = "stream:out";
		this.destinoGuia = "stream:out";
		this.destinoOtro = "stream:out";
	}

	@Override
	public void configure() {
		from(origen)

			//Transformación de JSON al tipo de dato interno DocumentoRequest
			.unmarshal().json(JsonLibrary.Jackson, DocumentoRequest.class)

			//Se crea un choice, que verifica el tipo de documento y lo envía al endpoint correspondiente
			.choice()

			.when(simple("${body.tipo} == 'Factura'"))
			.to(destinoFact)
			.endChoice()

			.when(simple("${body.tipo} == 'Guia Despacho'"))
			.to(destinoGuia)
			.endChoice()

			//Si no se cumple ninguna condicion, se pasa al otherwise()
			.otherwise()
			.to(destinoOtro)
			.endChoice()

			.end()

			.transform().simple("OK")
		;
	}
}
