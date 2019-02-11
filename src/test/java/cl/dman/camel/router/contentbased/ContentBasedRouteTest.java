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
	@Produce(uri = "direct:in")
	private ProducerTemplate directIn;

	@EndpointInject(uri = "mock:destinoFacturas")
	private MockEndpoint mockDestinoFacturas;

	@EndpointInject(uri = "mock:destinoGuia")
	private MockEndpoint mockDestinoGuia;

	@EndpointInject(uri = "mock:destinoOtro")
	private MockEndpoint mockDestinoOtro;

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		String origen = "direct:in";
		String destinoFacturas = "mock:destinoFacturas";
		String destinoGuia = "mock:destinoGuia";
		String destinoOtro = "mock:destinoOtro";
		return new ContentBasedRoute(origen, destinoFacturas, destinoGuia, destinoOtro);
	}

	@Test
	public void simpleTest() throws Exception {
		//Arrange
		mockDestinoFacturas.expectedMessageCount(1);
		mockDestinoGuia.expectedMessageCount(2);
		mockDestinoOtro.expectedMessageCount(3);

		//Act
		directIn.sendBody(toJson(new ContentRequest(10, "Factura")));
		directIn.sendBody(toJson(new ContentRequest(21, "Guia Despacho")));
		directIn.sendBody(toJson(new ContentRequest(22, "Guia Despacho")));
		directIn.sendBody(toJson(new ContentRequest(31, "Nota de Credito")));
		directIn.sendBody(toJson(new ContentRequest(32, "Nota de Credito")));
		directIn.sendBody(toJson(new ContentRequest(33, "Nota de Credito")));

		//Assert
		mockDestinoFacturas.assertIsSatisfied();
		mockDestinoGuia.assertIsSatisfied();
		mockDestinoOtro.assertIsSatisfied();
	}

	private String toJson(ContentRequest request) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(request);
	}

}