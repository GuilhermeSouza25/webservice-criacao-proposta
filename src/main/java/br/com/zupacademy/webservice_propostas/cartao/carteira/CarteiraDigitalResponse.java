package br.com.zupacademy.webservice_propostas.cartao.carteira;

public class CarteiraDigitalResponse {
	
	private String email;
	
	private String associadaEm;
	
	private String emissor;

	public CarteiraDigitalResponse(CarteiraDigital carteiraDigital) {
		super();
		this.email = carteiraDigital.getEmail();
		this.associadaEm = carteiraDigital.formataData("dd/MM/yyyy HH:mm");
		this.emissor = carteiraDigital.getEmissor().toString();
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getAssociadaEm() {
		return associadaEm;
	}
	
	public String getEmissor() {
		return emissor;
	}
}
