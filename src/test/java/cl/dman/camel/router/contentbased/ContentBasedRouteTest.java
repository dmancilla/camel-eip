package cl.dman.camel.router.contentbased;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

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

	@Test
	public void simpleTest() throws Exception {
		//Arrange
		mockDestinoFact.expectedMessageCount(1);
		mockDestinoGuia.expectedMessageCount(2);
		mockDestinoOtro.expectedMessageCount(3);

		//Act
		directIn.sendBody(getDocumento(10, "Factura"));
		directIn.sendBody(getDocumento(21, "Guia Despacho"));
		directIn.sendBody(getDocumento(22, "Guia Despacho"));
		directIn.sendBody(getDocumento(31, "Nota de Credito"));
		directIn.sendBody(getDocumento(32, "Nota de Credito"));
		directIn.sendBody(getDocumento(33, "Nota de Credito"));

		//Assert
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