package br.com.zupacademy.proposta.proposta.controller;

import java.net.URI;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zupacademy.proposta.proposta.EstadoProposta;
import br.com.zupacademy.proposta.proposta.Proposta;
import br.com.zupacademy.proposta.proposta.analisefinanceira.AnaliseFinanceira;
import br.com.zupacademy.proposta.proposta.analisefinanceira.ResultadoAnalise;
import br.com.zupacademy.proposta.proposta.analisefinanceira.SolicitacaoAnaliseRequest;
import br.com.zupacademy.proposta.shared.ExecutorTransacao;
import br.com.zupacademy.proposta.shared.Log;

@RestController
@Validated	
public class PropostaController {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private ExecutorTransacao transaction;
	
	@Autowired
	private AnaliseFinanceira analiseFinanceira;
	
	private final Logger logger = LoggerFactory.getLogger(Log.class);
	
	@PostMapping("/proposta/nova")
	public ResponseEntity<?> cadastrar(
			@RequestBody @Valid PropostaRequest propostaRequest,
			UriComponentsBuilder uriBuilder) {
		
		Proposta proposta = propostaRequest.converter();
		transaction.salvaEComita(proposta);
		
		logger.info("Proposta crida");
		URI uri = uriBuilder.path("/proposta/{id}").buildAndExpand(proposta.getId()).toUri();
		
		ResultadoAnalise resultado = analiseFinanceira.solicitaAnalise(new SolicitacaoAnaliseRequest(proposta)).getResultadoSolicitacao();
		
		if(resultado.equals(ResultadoAnalise.SEM_RESTRICAO)) {
			proposta.setEstado(EstadoProposta.ELEGIVEL);
		} else {
			proposta.setEstado(EstadoProposta.NAO_ELEGIVEL);
		}
		
		transaction.atualizaEComita(proposta);
		
		return ResponseEntity.created(uri).body(resultado);
	}
	
	@GetMapping("/proposta/{id}") 
	public	 ResponseEntity<?> detalhar(
			@Pattern(regexp = "[\\s]*[0-9]*[1-9]+",message="ID da proposta inválido. Informe um valor numérico positivo")
			@PathVariable("id") 
			String id) {
			
		Proposta proposta = manager.find(Proposta.class, Long.valueOf(id));
		
		if(proposta == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(new PropostaResponse(proposta));
		}
	}
}
