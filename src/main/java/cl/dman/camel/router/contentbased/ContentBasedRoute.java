package cl.dman.camel.router.contentbased;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 * Ruta que recibe un documento, y rutea a diferentes endpoints de acuerdo al contenido
 */
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

			//Transformación de JSON al tipo de dato interno DocumentoRequest
			.unmarshal().json(JsonLibrary.Jackson, DocumentoRequest.class)

			//Se crea un choice, que verifica el tipo de documento y lo envía al endpoint correspondiente
			.choice()
				.when(simple("${body.tipo} == 'Factura'"))
					.to(destinoFacturas)
				.when(simple("${body.tipo} == 'Guia Despacho'"))
					.to(destinoGuia)

				//Si no se cumple ninguna condicion, se pasa al otherwise()
				.otherwise()
					.to(destinoOtro)
		;
	}
}
