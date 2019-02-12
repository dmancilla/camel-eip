package cl.dman.camel.router.aggregator;

import cl.dman.camel.router.DocumentoRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.AssertionClause;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Pruebas de Aggregator
 *
 * @see <a href="https://www.enterpriseintegrationpatterns.com/patterns/messaging/Aggregator.html">EIP - Aggregator</a>
 */
public class AggregatorRouteTest extends CamelTestSupport {
	private final String ORIGEN_URI = "direct:in";
	private final String DESTINO_URI = "mock:destino";

	@Produce(uri = ORIGEN_URI)
	private ProducerTemplate directIn;

	@EndpointInject(uri = DESTINO_URI)
	private MockEndpoint mockDestino;

	@Override
	protected RoutesBuilder createRouteBuilder() {
		return new AggregatorRoute(ORIGEN_URI, DESTINO_URI);
	}

	/**
	 *
	 */
	@Test
	public void simpleTestTres() throws Exception {
		//Arrange
		String doc10 = getDocumentoJson(10, "Factura");
		String doc11 = getDocumentoJson(11, "Factura");
		String doc20 = getDocumentoJson(20, "Guia de Despacho", 10);
		String doc21 = getDocumentoJson(21, "Guia de Despacho", 11);
		String doc30 = getDocumentoJson(30, "Nota de Credito", 10);
		String doc31 = getDocumentoJson(31, "Nota de Credito", 11);

		//Act
		//Se envian 6 mensajes en total, ordenados por numero
		directIn.sendBody(doc10);
		directIn.sendBody(doc11);
		directIn.sendBody(doc20);
		directIn.sendBody(doc21);
		directIn.sendBody(doc31);
		directIn.sendBody(doc30);

		//Assert
		//Los mensajes deben quedar agrupados por ref o por numero (si ref es 0)
		mockDestino.expectedMessageCount(2);
		AssertionClause message0 = mockDestino.message(0);
		message0.simple("${body[0].numero}").isEqualTo(11);
		message0.simple("${body[1].numero}").isEqualTo(21);
		message0.simple("${body[2].numero}").isEqualTo(31);

		AssertionClause message1 = mockDestino.message(1);
		message1.simple("${body[0].numero}").isEqualTo(10);
		message1.simple("${body[1].numero}").isEqualTo(20);
		message1.simple("${body[2].numero}").isEqualTo(30);

		mockDestino.assertIsSatisfied();

	}

	private String getDocumentoJson(int numero, String tipo) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		DocumentoRequest request = new DocumentoRequest(numero, tipo);
		return objectMapper.writeValueAsString(request);
	}

	private String getDocumentoJson(int numero, String tipo, int ref) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		DocumentoRequest request = new DocumentoRequest(numero, tipo, ref);
		return objectMapper.writeValueAsString(request);
	}

}