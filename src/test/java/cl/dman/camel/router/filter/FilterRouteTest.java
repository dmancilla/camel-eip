package cl.dman.camel.router.filter;

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
 * Pruebas de filtro
 *
 * @see <a href="https://www.enterpriseintegrationpatterns.com/patterns/messaging/Filter.html">EIP - Filter</a>
 */
public class FilterRouteTest extends CamelTestSupport {
	private final String ORIGEN_URI = "direct:in";
	private final String DESTINO_FACT_URI = "mock:destino";

	@Produce(uri = ORIGEN_URI)
	private ProducerTemplate directIn;

	@EndpointInject(uri = DESTINO_FACT_URI)
	private MockEndpoint mockDestino;

	@Override
	protected RoutesBuilder createRouteBuilder() {
		return new FilterRoute(ORIGEN_URI, DESTINO_FACT_URI);
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
		mockDestino.expectedMessageCount(1);

		mockDestino.assertIsSatisfied();
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
		mockDestino.expectedMessageCount(1);

		mockDestino.assertIsSatisfied();
	}

	/**
	 * Se verifica que al enviar un documento de tipo Guia Despacho, no llegue al endpoint correspondiente
	 */
	@Test
	public void simpleTestGuia() throws Exception {
		//Arrange

		//Act
		directIn.sendBody(getDocumento(21, "Guia Despacho"));

		//Assert
		mockDestino.expectedMessageCount(0);

		mockDestino.assertIsSatisfied();
	}

	/**
	 * Se verifica que al enviar varios documentos de diferentes tipos, lleguen los que corresponden
	 */
	@Test
	public void simpleTestMultiple() throws Exception {
		//Arrange

		//Act
		directIn.sendBody(getDocumento(11, "Factura"));
		directIn.sendBody(getDocumento(21, "Guia Despacho"));
		directIn.sendBody(getDocumento(31, "Nota de Credito"));

		directIn.sendBody(getDocumento(12, "Factura"));
		directIn.sendBody(getDocumento(22, "Guia Despacho"));
		directIn.sendBody(getDocumento(32, "Nota de Credito"));

		//Assert
		mockDestino.expectedMessageCount(4);

		mockDestino.assertIsSatisfied();
	}


	private String getDocumento(int numero, String tipo) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		DocumentoRequest request = new DocumentoRequest(numero, tipo);
		return objectMapper.writeValueAsString(request);
	}
}