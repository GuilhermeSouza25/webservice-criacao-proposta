package br.com.zupacademy.webservice_propostas.proposta.event;

import org.springframework.context.ApplicationEvent;

import br.com.zupacademy.webservice_propostas.proposta.analisefinanceira_client.SolicitacaoAnalise;

public class PropostaElegivelEvent extends ApplicationEvent  {

	private static final long serialVersionUID = 1L;
	
	private String message;
	
	private SolicitacaoAnalise analiseRequest;
	
	public PropostaElegivelEvent(Object source, String message, SolicitacaoAnalise analiseRequest) {
		super(source);
		System.out.println("Criou o evento");
		this.message = message;
		this.analiseRequest = analiseRequest;
	}
	
	public String getMessage() {
		return message;
	}
	
	public SolicitacaoAnalise getAnaliseRequest() {
		return analiseRequest;
	}
}
