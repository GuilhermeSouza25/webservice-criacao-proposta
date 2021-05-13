package br.com.zupacademy.webservice_propostas.proposta.analisefinanceira_client;

import javax.validation.constraints.NotBlank;

import br.com.zupacademy.webservice_propostas.proposta.Proposta;

/**
 * Esta classe é usada para representar tanto uma solicitação de
 * análise financeira, como uma solicitação de novo cartão.
 * @author Jose
 */
public class SolicitacaoAnalise {
	
	@NotBlank
	private String documento;
	@NotBlank
	private String nome;
	@NotBlank
	private String idProposta;
	
	public SolicitacaoAnalise(Proposta proposta) {
		super();
		this.documento = proposta.getDocumento();
		this.nome = proposta.getNome();
		this.idProposta = proposta.getId().toString();
	}

	public String getDocumento() {
		return documento;
	}

	public String getNome() {
		return nome;
	}

	public String getIdProposta() {
		return idProposta;
	}
	
}
