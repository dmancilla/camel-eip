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
	private final String DESTINO_URI = "mock:destino";

	@Produce(uri = ORIGEN_URI)
	private ProducerTemplate directIn;

	@EndpointInject(uri = DESTINO_URI)
	private MockEndpoint mockDestino;

	@Override
	protected RoutesBuilder createRouteBuilder() {
		return new FilterRoute(ORIGEN_URI, DESTINO_URI);
	}

	/**
	 * Se verifica que al enviar un documento de tipo Factura, llegue al endpoint correspondiente
	 */
	@Test
	public void simpleTestFactura() throws Exception {
		//Arrange
		String doc10 = getDocumentoJson(10, "Factura");

		//Act
		directIn.sendBody(doc10);
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
		String doc31 = getDocumentoJson(31, "Nota de Credito");

		//Act
		directIn.sendBody(doc31);

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
		String doc21 = getDocumentoJson(21, "Guia Despacho");

		//Act
		directIn.sendBody(doc21);

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
		String doc11 = getDocumentoJson(11, "Factura");
		String doc12 = getDocumentoJson(12, "Factura");
		String doc21 = getDocumentoJson(21, "Guia Despacho");
		String doc22 = getDocumentoJson(22, "Guia Despacho");
		String doc31 = getDocumentoJson(31, "Nota de Credito");
		String doc32 = getDocumentoJson(32, "Nota de Credito");

		//Act
		directIn.sendBody(doc11);
		directIn.sendBody(doc21);
		directIn.sendBody(doc31);
		directIn.sendBody(doc12);
		directIn.sendBody(doc22);
		directIn.sendBody(doc32);

		//Assert
		mockDestino.expectedMessageCount(4);

		mockDestino.assertIsSatisfied();
	}


	private String getDocumentoJson(int numero, String tipo) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		DocumentoRequest request = new DocumentoRequest(numero, tipo);
		return objectMapper.writeValueAsString(request);
	}
}