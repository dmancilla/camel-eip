package cl.dman.camel.router.aggregator;

import cl.dman.camel.router.DocumentoRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;
import org.apache.camel.support.ExpressionAdapter;
import org.springframework.stereotype.Component;

/**
 * Ruta que recibe un mensaje con documento, y lo separa en  el mensaje de acuerdo al contenido
 */
@Component
public class AggregatorRoute extends RouteBuilder {
	private final String origen;
	private final String destino;

	//Constructor necesario para crear los endpoints de Test
	public AggregatorRoute(String origen, String destino) {
		this.origen = origen;
		this.destino = destino;
	}

	//Constructor necesario para crear los endpoints reales
	public AggregatorRoute() {
		this.origen = "rest:post:aggregator";
		this.destino = "stream:out";
	}

	@Override
	public void configure() {
		JacksonDataFormat format = new JacksonDataFormat(DocumentoRequest.class);

		Expression correlationExpression1 = new ExpressionAdapter() {
			@Override
			public Object evaluate(Exchange exchange) {
				DocumentoRequest request = exchange.getIn().getBody(DocumentoRequest.class);
				return request.getRef() == 0 ? request.getNumero() : request.getRef();
			}
		};
		GroupedBodyAggregationStrategy aggregationStrategy = new GroupedBodyAggregationStrategy();

		from(origen)

			//Transformación de JSON al tipo de dato interno List<DocumentoRequest>
			.unmarshal(format)

			.aggregate(correlationExpression1, aggregationStrategy)
			.completionTimeout(1000L)
			.to(destino)
			.end()

			.transform().simple("OK")
		;
	}
}
