package br.com.zupacademy.webservice_propostas.proposta.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.zupacademy.webservice_propostas.proposta.Proposta;
import br.com.zupacademy.webservice_propostas.proposta.PropostaRepository;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@Validated
@RequestMapping("/propostas")
@Timed(value = "proposta_listar", extraTags = {"emissora", "Mastercard", "banco", "Itaú"})
public class ListaPropostasController {
	
	@PersistenceContext EntityManager manager;
	@Autowired PropostaRepository repository;
	
	/**
	 * Não recomendado usar cache aqui, pois esta tabela é atualizada o tempo todo
	 * o que pode ser pior pois o cache vai der invalidado muitas vezes
	 */
	@GetMapping("/listar_pageable")
	@Cacheable(cacheNames = "listaDePropostas")
	//@PageableAsQueryParam
	public ResponseEntity<Page<PropostaResponse>> listarPropostasPageable(
			@ParameterObject 
			@Parameter(hidden = true) 
			@PageableDefault(page = 0, size = 5) Pageable paginacao) {
		
		//PageRequest paginacao = PageRequest.of(pagina, quantidade, Direction.DESC, ordenacao);
		
		Page<Proposta> propostas = repository.findAll(paginacao);
		
		return ResponseEntity.ok(PropostaResponse.toPage(propostas));
	}
	
	@GetMapping("/listar_manager")
	public ResponseEntity<List<PropostaResponse>> listarPropostasManager(
			@RequestParam(required = true) Integer pagina, 
			@RequestParam(required = true) Integer quantidade) {
		
		Integer posicaoInicial = pagina * quantidade;
		
		List<Proposta> propostas = manager.createQuery("SELECT p FROM Proposta p", Proposta.class)
		.setFirstResult(posicaoInicial)
		.setMaxResults(quantidade)
		.getResultList();
		
		return ResponseEntity.ok(PropostaResponse.toList(propostas));
	}
}
