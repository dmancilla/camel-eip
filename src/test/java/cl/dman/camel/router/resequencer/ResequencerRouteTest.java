package cl.dman.camel.router.resequencer;

import cl.dman.camel.router.DocumentoRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Pruebas de Resequencer
 *
 * @see <a href="https://www.enterpriseintegrationpatterns.com/patterns/messaging/Resequencer.html">EIP - Resequencer</a>
 */
public class ResequencerRouteTest extends CamelTestSupport {
	private final String ORIGEN_URI = "direct:in";
	private final String DESTINO_URI = "mock:destino";

	@Produce(uri = ORIGEN_URI)
	private ProducerTemplate directIn;

	@EndpointInject(uri = DESTINO_URI)
	private MockEndpoint mockDestino;

	@Override
	protected RoutesBuilder createRouteBuilder() {
		return new ResequencerRoute(ORIGEN_URI, DESTINO_URI);
	}

	/**
	 * Se verifica que al enviar 3 documentos desordenados, se devuelvan en orden
	 */
	@Test
	public void simpleTestTres() throws Exception {
		//Arrange
		String doc10 = getDocumentoJson(10, "Factura");
		String doc20 = getDocumentoJson(20, "Factura");
		String doc30 = getDocumentoJson(30, "Factura");

		//Act
		//Se envian los mensajes desordenados
		directIn.sendBody(doc20);
		directIn.sendBody(doc10);
		directIn.sendBody(doc30);

		//Assert
		mockDestino.expectedMessageCount(3);
		mockDestino.message(0).simple("body.numero").isEqualTo(10);
		mockDestino.message(1).simple("body.numero").isEqualTo(20);
		mockDestino.message(2).simple("body.numero").isEqualTo(30);
		mockDestino.assertIsSatisfied();

	}

	private String getDocumentoJson(int numero, String tipo) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		DocumentoRequest request = new DocumentoRequest(numero, tipo);
		return objectMapper.writeValueAsString(request);
	}

}