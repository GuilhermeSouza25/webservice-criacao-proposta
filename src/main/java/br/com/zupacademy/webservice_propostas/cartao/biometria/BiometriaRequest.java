package br.com.zupacademy.webservice_propostas.cartao.biometria;

import javax.validation.constraints.NotEmpty;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;

public class BiometriaRequest {
	
	@NotEmpty
	private String biometria;
	
	public String getBiometria() {
		return biometria;
	}

	public Biometria converter(Cartao cartao) {
		return new Biometria(biometria, cartao);
	}
}
