package br.com.zupacademy.webservice_proposta.proposta.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import br.com.zupacademy.webservice_proposta.proposta.analisefinanceira_client.SolicitacaoAnalise;

@Component
public class PropostaElegivelPublisher {
	
	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(final String message, SolicitacaoAnalise analiseRequest) {
    	
        System.out.println("Publicando o PropostaElegivelEvent...");
        
        PropostaElegivelEvent propostaElegivelEvent = new PropostaElegivelEvent(this, message, analiseRequest);
        applicationEventPublisher.publishEvent(propostaElegivelEvent);
    }
}

