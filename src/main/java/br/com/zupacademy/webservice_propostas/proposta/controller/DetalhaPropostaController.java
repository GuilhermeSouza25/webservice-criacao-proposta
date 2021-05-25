package br.com.zupacademy.webservice_propostas.proposta.controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zupacademy.webservice_propostas.cartao.Vencimento;
import br.com.zupacademy.webservice_propostas.proposta.Proposta;
import io.micrometer.core.annotation.Timed;

@RestController
@Validated
@RequestMapping("/propostas")
@Timed(value = "proposta_consulta", extraTags = {"emissora", "Mastercard", "banco", "Itaú"})
public class DetalhaPropostaController {
	
	@PersistenceContext private EntityManager manager;
	
	@GetMapping("/{id}") 
	public ResponseEntity<?> detalhar(
			@Pattern(regexp = "[\\s]*[0-9]*[1-9]+",message="ID da proposta inválido. Informe um valor numérico positivo")
			@PathVariable("id") String id) {
			
		Proposta proposta = manager.find(Proposta.class, Long.valueOf(id));
		
		if(proposta == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(new PropostaResponse(proposta));
		}
	}
	
	@GetMapping("/proposta/teste")
	public void teste() {
		
		 Vencimento proposta = manager
				.createQuery("SELECT v FROM Vencimento v WHERE v.id = '2e8fed59-6017-4e22-9ab0-780c1e491564'", Vencimento.class)
				.getSingleResult();
		 
		 System.out.println(proposta);
	}
}
