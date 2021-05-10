package br.com.zupacademy.webservice_proposta.proposta.schedule;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.zupacademy.webservice_proposta.cartao.Cartao;
import br.com.zupacademy.webservice_proposta.cartao.cartao_client.CartaoClient;
import br.com.zupacademy.webservice_proposta.cartao.cartao_client.CartaoResponse;
import br.com.zupacademy.webservice_proposta.proposta.Proposta;
import br.com.zupacademy.webservice_proposta.proposta.analisefinanceira_client.SolicitacaoAnalise;
import br.com.zupacademy.webservice_proposta.shared.ExecutorTransacao;

@Component
public class GeraCartaoJob {
	
	@PersistenceContext private EntityManager manager;
	@Autowired private CartaoClient cartaoClient;
	@Autowired ExecutorTransacao executorTransacao;
	
	@Scheduled(fixedRateString = "${periodicidade.gera-cartao}")
	public void verifica() {
		
		List<Proposta> propostas = manager
				.createQuery("SELECT p FROM Proposta p WHERE p.cartao IS NULL", Proposta.class)
				.getResultList();
		
		if(!propostas.isEmpty()) {
			propostas.forEach(proposta -> {
				geraCartao(proposta);
			});
		}
	}
	
	private void geraCartao(Proposta proposta) {
		
		CartaoResponse cartaoResponse = cartaoClient.solicitaCartao(new SolicitacaoAnalise(proposta));
		
		Cartao cartao = cartaoResponse.converter();
		proposta.associarCartao(cartao);
		
		executorTransacao.atualizaEComita(proposta);
	}
}
