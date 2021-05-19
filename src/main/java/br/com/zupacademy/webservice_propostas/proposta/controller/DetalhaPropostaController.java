package br.com.zupacademy.webservice_propostas.proposta.controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;
import br.com.zupacademy.webservice_propostas.proposta.Proposta;
import io.micrometer.core.annotation.Timed;

@RestController
@Validated
@Timed(value = "proposta_consulta", extraTags = {"emissora", "Mastercard", "banco", "Itaú"})
public class DetalhaPropostaController {
	
	@PersistenceContext private EntityManager manager;
	
	@GetMapping("/propostas/{id}") 
	public	 ResponseEntity<?> detalhar(
			//@Pattern(regexp = "[\\s]*[0-9]*[1-9]+",message="ID da proposta inválido. Informe um valor numérico positivo")
			@PathVariable("id") Long id) {
			
		Proposta proposta = manager.find(Proposta.class, Long.valueOf(id));
		
		if(proposta == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(new PropostaResponse(proposta));
		}
	}
	
	@GetMapping("/proposta/teste")
	public void teste() {
		
		 Cartao cartao = manager
				.createQuery("SELECT c FROM Cartao c JOIN Fetch c.vencimento WHERE c.vencimento.dia = 30", Cartao.class)
				.getSingleResult();
		 
		 System.out.println(cartao);
	}
}
