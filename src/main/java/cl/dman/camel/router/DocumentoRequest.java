package cl.dman.camel.router;

public class DocumentoRequest {
	private int numero;
	private String tipo;
	private int ref;

	public DocumentoRequest() {
	}

	public DocumentoRequest(int numero, String tipo) {
		this.numero = numero;
		this.tipo = tipo;
	}

	public DocumentoRequest(int numero, String tipo, int ref) {
		this.numero = numero;
		this.tipo = tipo;
		this.ref = ref;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getRef() {
		return ref;
	}

	public void setRef(int ref) {
		this.ref = ref;
	}
}
