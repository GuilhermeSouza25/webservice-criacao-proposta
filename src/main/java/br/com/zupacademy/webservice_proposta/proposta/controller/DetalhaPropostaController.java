package br.com.zupacademy.webservice_proposta.proposta.controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.zupacademy.webservice_proposta.cartao.Cartao;
import br.com.zupacademy.webservice_proposta.proposta.Proposta;

@RestController
@Validated	
public class DetalhaPropostaController {
	
	@PersistenceContext private EntityManager manager;
	
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
	
	@GetMapping("/proposta/teste")
	public void teste() {
		
		 Cartao cartao = manager
				.createQuery("SELECT c FROM Cartao c JOIN Fetch c.vencimento WHERE c.vencimento.dia = 30", Cartao.class)
				.getSingleResult();
		 
		 System.out.println(cartao);
	}
}
