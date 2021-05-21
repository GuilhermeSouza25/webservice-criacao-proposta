package br.com.zupacademy.webservice_propostas.proposta.analisefinanceira_client;

import br.com.zupacademy.webservice_propostas.proposta.EstadoProposta;

public class ResultadoAnalise {
	
	private String documento;
	private String nome;
	private StatusAnalise resultadoSolicitacao;
	private String idProposta;
	
	public String getDocumento() {
		return documento;
	}

	public String getNome() {
		return nome;
	}

	public StatusAnalise getResultadoSolicitacao() {
		return resultadoSolicitacao;
	}

	public String getIdProposta() {
		return idProposta;
	}

	public EstadoProposta getEstadoProposta() {
		return resultadoSolicitacao.getEstadoProposta();
	}
}
