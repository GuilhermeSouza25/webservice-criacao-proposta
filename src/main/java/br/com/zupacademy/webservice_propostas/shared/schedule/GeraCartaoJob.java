package br.com.zupacademy.webservice_propostas.shared.schedule;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;
import br.com.zupacademy.webservice_propostas.cartao.cartao_client.CartaoClient;
import br.com.zupacademy.webservice_propostas.cartao.cartao_client.CartaoResponse;
import br.com.zupacademy.webservice_propostas.proposta.EstadoProposta;
import br.com.zupacademy.webservice_propostas.proposta.Proposta;
import br.com.zupacademy.webservice_propostas.shared.ExecutorTransacao;

@Component
public class GeraCartaoJob {
	
	@PersistenceContext private EntityManager manager;
	@Autowired private CartaoClient cartaoClient;
	@Autowired ExecutorTransacao executorTransacao;
	
	@Scheduled(fixedRateString = "${periodicidade.gera-cartao}")
	public void verifica() {
		
		List<Proposta> propostas = manager
				.createQuery("SELECT p FROM Proposta p WHERE p.cartao IS NULL AND p.estado = :estado", Proposta.class)
				.setParameter("estado", EstadoProposta.ELEGIVEL)
				.getResultList();
		
		if(!propostas.isEmpty()) {
			propostas.forEach(proposta -> {
				associaCartao(proposta);
			});
		}
	}
	
	private void associaCartao(Proposta proposta) {
		CartaoResponse cartaoResponse = cartaoClient.buscaCartaoGerado(proposta.getId().toString());
		Cartao cartao = cartaoResponse.converter();
		
		proposta.associarCartao(cartao);
		executorTransacao.atualizaEComita(proposta);
	}

	@Transactional
	public Map<Long, Proposta> buscaPropostasSemCartao() {
		return manager
				.createQuery("SELECT p FROM Proposta p WHERE p.cartao IS NULL", Proposta.class)
				.getResultStream().collect(Collectors.toMap(Proposta::getId, Function.identity()));
	}
}
