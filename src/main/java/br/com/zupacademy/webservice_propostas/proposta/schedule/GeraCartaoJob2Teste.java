//package br.com.zupacademy.webservice_propostas.proposta.schedule;
//
//import java.util.Collection;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.transaction.Transactional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import br.com.zupacademy.webservice_propostas.cartao.Cartao;
//import br.com.zupacademy.webservice_propostas.cartao.cartao_client.CartaoClient;
//import br.com.zupacademy.webservice_propostas.cartao.cartao_client.CartaoResponse;
//import br.com.zupacademy.webservice_propostas.proposta.Proposta;
//import br.com.zupacademy.webservice_propostas.shared.ExecutorTransacao;
//
////@Component
//public class GeraCartaoJob2Teste {
//	
//	@PersistenceContext private EntityManager manager;
//	@Autowired private CartaoClient cartaoClient;
//	@Autowired ExecutorTransacao executorTransacao;
//	
//	//@Scheduled(fixedRateString = "${periodicidade.gera-cartao}")
//	public void verifica() {
//		
//		Map<Long, Proposta> propostas = buscaPropostasSemCartao();
//		
//		if(!propostas.isEmpty()) {
//			Collection<CartaoResponse> cartoesResponse = cartaoClient.buscaCartoesGerados();
//			cartoesResponse.forEach(cartaoResponse -> {
//				Proposta proposta = propostas.get(Long.valueOf(cartaoResponse.getIdProposta()));
//				associaCartoes(cartaoResponse, proposta);
//			});
//		}
//	}
//	
//	private void associaCartoes(CartaoResponse cartaoResponse,  Proposta proposta) {
//		Cartao cartao = cartaoResponse.converter();
//		proposta.associarCartao(cartao);
//		executorTransacao.atualizaEComita(proposta);
//	}
//
//	@Transactional
//	public Map<Long, Proposta> buscaPropostasSemCartao() {
//		return manager
//				.createQuery("SELECT p FROM Proposta p WHERE p.cartao IS NULL", Proposta.class)
//				.getResultStream().collect(Collectors.toMap(Proposta::getId, Function.identity()));
//	}
//}
