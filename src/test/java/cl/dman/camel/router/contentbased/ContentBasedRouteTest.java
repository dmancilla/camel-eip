package cl.dman.camel.router.contentbased;

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
 * Pruebas de ruteo basado en contenido
 *
 * @see <a href="https://www.enterpriseintegrationpatterns.com/patterns/messaging/ContentBasedRouter.html">EIP - Content-Based Router</a>
 */
public class ContentBasedRouteTest extends CamelTestSupport {
	private final String ORIGEN_URI = "direct:in";
	private final String DESTINO_FACT_URI = "mock:destinoFacturas";
	private final String DESTINO_GUIA_URI = "mock:destinoGuia";
	private final String DESTINO_OTRO_URI = "mock:destinoOtro";

	@Produce(uri = ORIGEN_URI)
	private ProducerTemplate directIn;

	@EndpointInject(uri = DESTINO_FACT_URI)
	private MockEndpoint mockDestinoFact;

	@EndpointInject(uri = DESTINO_GUIA_URI)
	private MockEndpoint mockDestinoGuia;

	@EndpointInject(uri = DESTINO_OTRO_URI)
	private MockEndpoint mockDestinoOtro;

	@Override
	protected RoutesBuilder createRouteBuilder() {
		return new ContentBasedRoute(ORIGEN_URI, DESTINO_FACT_URI, DESTINO_GUIA_URI, DESTINO_OTRO_URI);
	}

	/**
	 * Se verifica que al enviar un documento de tipo Factura, llegue al endpoint correspondiente
	 */
	@Test
	public void simpleTestFactura() throws Exception {
		//Arrange

		//Act
		directIn.sendBody(getDocumento(10, "Factura"));
		//Assert
		mockDestinoFact.expectedMessageCount(1);
		mockDestinoGuia.expectedMessageCount(0);
		mockDestinoOtro.expectedMessageCount(0);

		mockDestinoFact.assertIsSatisfied();
		mockDestinoGuia.assertIsSatisfied();
		mockDestinoOtro.assertIsSatisfied();
	}

	/**
	 * Se verifica que al enviar un documento de tipo Guia Despacho, llegue al endpoint correspondiente
	 */
	@Test
	public void simpleTestGuia() throws Exception {
		//Arrange

		//Act
		directIn.sendBody(getDocumento(21, "Guia Despacho"));

		//Assert
		mockDestinoFact.expectedMessageCount(0);
		mockDestinoGuia.expectedMessageCount(1);
		mockDestinoOtro.expectedMessageCount(0);

		mockDestinoFact.assertIsSatisfied();
		mockDestinoGuia.assertIsSatisfied();
		mockDestinoOtro.assertIsSatisfied();
	}

	/**
	 * Se verifica que al enviar un documento de tipo Nota de Credito, llegue al endpoint correspondiente
	 */
	@Test
	public void simpleTestOtro() throws Exception {
		//Arrange

		//Act
		directIn.sendBody(getDocumento(31, "Nota de Credito"));

		//Assert
		mockDestinoFact.expectedMessageCount(0);
		mockDestinoGuia.expectedMessageCount(0);
		mockDestinoOtro.expectedMessageCount(1);

		mockDestinoFact.assertIsSatisfied();
		mockDestinoGuia.assertIsSatisfied();
		mockDestinoOtro.assertIsSatisfied();
	}


	private String getDocumento(int numero, String tipo) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		DocumentoRequest request = new DocumentoRequest(numero, tipo);
		return objectMapper.writeValueAsString(request);
	}
}