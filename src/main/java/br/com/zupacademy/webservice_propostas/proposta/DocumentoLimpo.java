package br.com.zupacademy.webservice_propostas.proposta;

import javax.validation.constraints.NotBlank;

import br.com.zupacademy.webservice_propostas.shared.StringCryptService;

public class DocumentoLimpo {
	
	private String documento;
	
	public DocumentoLimpo(@NotBlank @ValidaCPFOuCNPJ String documento) {
		this.documento = documento;
	}
	
	public String encrypt() {
		return StringCryptService.encrypt(documento);
	}
}
