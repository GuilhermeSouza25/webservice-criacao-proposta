package br.com.zupacademy.webservice_propostas.proposta.event;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;
import br.com.zupacademy.webservice_propostas.cartao.cartao_client.CartaoClient;
import br.com.zupacademy.webservice_propostas.cartao.cartao_client.CartaoResponse;
import br.com.zupacademy.webservice_propostas.proposta.Proposta;
import br.com.zupacademy.webservice_propostas.shared.ExecutorTransacao;

@Component
public class PropostaElegivelListener implements ApplicationListener<PropostaElegivelEvent>  {
	
	@Autowired private CartaoClient cartaoClient;
	@Autowired private ExecutorTransacao transacao;
	@Autowired private EntityManager manager;
	
	@Override
	public void onApplicationEvent(PropostaElegivelEvent event) {
		System.out.println("Escutou o evento...");
		System.out.println(event.getMessage());
		
//		CartaoResponse cartaoResponse = cartaoClient.solicitaCartao(event.getAnaliseRequest());
//		
//		Cartao cartao = cartaoResponse.converter();
//		
//		Proposta proposta = manager.find(Proposta.class, Long.valueOf(cartaoResponse.getIdProposta()));
//		proposta.associarCartao(cartao);
//		
//		transacao.atualizaEComita(proposta);
	}
}
