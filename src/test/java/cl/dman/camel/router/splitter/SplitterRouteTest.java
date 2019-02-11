package cl.dman.camel.router.splitter;

import cl.dman.camel.router.DocumentoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Pruebas de splitter
 *
 * @see <a href="https://www.enterpriseintegrationpatterns.com/patterns/messaging/Sequencer.html">EIP - Splitter</a>
 */
public class SplitterRouteTest extends CamelTestSupport {
	private final String ORIGEN_URI = "direct:in";
	private final String DESTINO_URI = "mock:destino";

	@Produce(uri = ORIGEN_URI)
	private ProducerTemplate directIn;

	@EndpointInject(uri = DESTINO_URI)
	private MockEndpoint mockDestino;

	@Override
	protected RoutesBuilder createRouteBuilder() {
		return new SplitterRoute(ORIGEN_URI, DESTINO_URI);
	}

	/**
	 * Se verifica que al enviar un documento de tipo Factura, llegue al endpoint correspondiente
	 */
	@Test
	public void simpleTestUno() throws Exception {
		//Arrange
		DocumentoRequest doc10 = new DocumentoRequest(10, "Factura");
		String json = getDocumentos(doc10);

		//Act
		directIn.sendBody(json);

		//Assert
		mockDestino.expectedMessageCount(1);

		mockDestino.assertIsSatisfied();
	}

	/**
	 * Se verifica que al enviar multiples documentos en el body, se reciban multiples items en el endpoint
	 */
	@Test
	public void simpleTestVarios() throws Exception {
		//Arrange
		DocumentoRequest doc10 = new DocumentoRequest(10, "Factura");
		DocumentoRequest doc20 = new DocumentoRequest(20, "Guia de Despacho");
		String json = getDocumentos(doc10, doc20);

		//Act
		directIn.sendBody(json);

		//Assert
		mockDestino.expectedMessageCount(2);

		mockDestino.assertIsSatisfied();
	}

	private String getDocumentos(DocumentoRequest... documentos) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		objectMapper.writeValue(out, documentos);
		final byte[] data = out.toByteArray();
		return new String(data, StandardCharsets.UTF_8);
	}
}