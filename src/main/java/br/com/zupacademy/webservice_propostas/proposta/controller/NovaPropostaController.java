package br.com.zupacademy.webservice_propostas.proposta.controller;

import java.net.URI;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zupacademy.webservice_propostas.config.metrics.CustomMetrics;
import br.com.zupacademy.webservice_propostas.proposta.EstadoProposta;
import br.com.zupacademy.webservice_propostas.proposta.Proposta;
import br.com.zupacademy.webservice_propostas.proposta.analisefinanceira_client.AnaliseFinanceiraClient;
import br.com.zupacademy.webservice_propostas.proposta.analisefinanceira_client.ResultadoAnalise;
import br.com.zupacademy.webservice_propostas.proposta.analisefinanceira_client.SolicitacaoAnalise;
import br.com.zupacademy.webservice_propostas.shared.ExecutorTransacao;
import br.com.zupacademy.webservice_propostas.shared.Log;
import br.com.zupacademy.webservice_propostas.shared.exceptionhandler.Erro;
import feign.FeignException.FeignClientException;
import feign.FeignException.FeignServerException;

@RestController
@Validated	
public class NovaPropostaController {
	
	@PersistenceContext private EntityManager manager;
	@Autowired private ExecutorTransacao transaction;
	@Autowired private AnaliseFinanceiraClient analiseFinanceiraClient;
	@Autowired private CustomMetrics metrics;
	
	private final Logger logger = LoggerFactory.getLogger(Log.class);
	
	@PostMapping("/propostas")
	public ResponseEntity<?> cadastrar(
			@RequestBody @Valid PropostaRequest propostaRequest,
			UriComponentsBuilder uriBuilder) {
		
		Proposta proposta = propostaRequest.converter();
		transaction.salvaEComita(proposta);
		
		try {
			ResultadoAnalise resultadoAnalise = analiseFinanceiraClient.solicitaAnalise(new SolicitacaoAnalise(proposta));
			EstadoProposta estadoProposta = resultadoAnalise.getEstadoProposta();
			
			proposta.alteraEstado(estadoProposta);
			
		} catch (FeignClientException e) {
			proposta.alteraEstado(EstadoProposta.NAO_ELEGIVEL);
			
		} catch (FeignServerException e) {
			transaction.removeEComita(proposta);
			return ResponseEntity.status(e.status()).body(new Erro("Houve um erro no processamento do sistema. Tente novamente."));
		}
		transaction.atualizaEComita(proposta);
		logger.info("Proposta criada");
		metrics.contadorPropostas();
		
		URI uri = uriBuilder.path("/proposta/{id}").buildAndExpand(proposta.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
}
