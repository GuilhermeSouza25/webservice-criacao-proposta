package br.com.zupacademy.webservice_propostas.proposta.analisefinanceira_client;

import br.com.zupacademy.webservice_propostas.proposta.EstadoProposta;

public enum StatusAnalise {
	
	COM_RESTRICAO(EstadoProposta.NAO_ELEGIVEL),
	SEM_RESTRICAO(EstadoProposta.ELEGIVEL);
	
	private final EstadoProposta estadoProposta;
	
	private StatusAnalise(EstadoProposta estado) {
		this.estadoProposta = estado;
	}
	
	public EstadoProposta getEstadoProposta() {
		return estadoProposta;
	}
}
