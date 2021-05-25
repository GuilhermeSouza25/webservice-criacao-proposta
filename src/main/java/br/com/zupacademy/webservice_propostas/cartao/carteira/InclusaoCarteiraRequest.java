package br.com.zupacademy.webservice_propostas.cartao.carteira;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;

public class InclusaoCarteiraRequest {
	
	@Email
	@NotBlank
	private String email;
	
	@NotBlank
	private String carteira;

	public InclusaoCarteiraRequest(@Email @NotBlank String email, @NotBlank String carteira) {
		super();
		this.email = email;
		this.carteira = carteira;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getCarteira() {
		return carteira;
	}

	public CarteiraDigital converter(Cartao cartao) {
		return new CarteiraDigital(email, carteira , cartao);
	}
}
