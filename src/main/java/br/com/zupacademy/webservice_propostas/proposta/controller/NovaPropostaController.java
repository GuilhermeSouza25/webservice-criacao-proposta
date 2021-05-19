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
import br.com.zupacademy.webservice_propostas.proposta.analisefinanceira_client.SolicitacaoAnalise;
import br.com.zupacademy.webservice_propostas.proposta.analisefinanceira_client.StatusAnalise;
import br.com.zupacademy.webservice_propostas.shared.ExecutorTransacao;
import br.com.zupacademy.webservice_propostas.shared.Log;
import br.com.zupacademy.webservice_propostas.shared.exceptionhandler.Erro;
import feign.FeignException;

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
		
		logger.info("Proposta criada");
		URI uri = uriBuilder.path("/proposta/{id}").buildAndExpand(proposta.getId()).toUri();
		
		try {
			SolicitacaoAnalise solicitacaoAnalise = new SolicitacaoAnalise(proposta);
			StatusAnalise status = analiseFinanceiraClient.solicitaAnalise(solicitacaoAnalise).getResultadoSolicitacao();
			
			if(status.equals(StatusAnalise.SEM_RESTRICAO)) {
				proposta.alteraEstado(EstadoProposta.ELEGIVEL);
			} else {
				proposta.alteraEstado(EstadoProposta.NAO_ELEGIVEL);
			}
			
			transaction.atualizaEComita(proposta);
			metrics.contadorPropostas();
			
			return ResponseEntity.created(uri).build();
			
		} catch (FeignException e) {
			e.printStackTrace();
			transaction.removeEComita(proposta);
			return ResponseEntity.status(500).body(new Erro("Houve um erro no processamento do sistema. Tente novamente."));
		}
	}
}
