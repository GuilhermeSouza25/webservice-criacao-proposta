package br.com.zupacademy.webservice_propostas.cartao.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zupacademy.webservice_propostas.cartao.Bloqueio;
import br.com.zupacademy.webservice_propostas.cartao.Cartao;
import br.com.zupacademy.webservice_propostas.cartao.StatusCartao;
import br.com.zupacademy.webservice_propostas.cartao.cartao_client.CartaoClient;
import br.com.zupacademy.webservice_propostas.cartao.cartao_client.CartaoClient.SistemaResponsavel;
import br.com.zupacademy.webservice_propostas.proposta.Proposta;
import br.com.zupacademy.webservice_propostas.shared.ExecutorTransacao;
import br.com.zupacademy.webservice_propostas.shared.exceptionhandler.Erro;
import feign.FeignException.FeignClientException;
import feign.FeignException.FeignServerException;

@RestController
@RequestMapping("/propostas/cartoes")
@Validated
public class BloqueioController {
	
	@Value("${application.name}")
	private String sistemaResponsavel;
	
	@PersistenceContext EntityManager manager;
	@Autowired CartaoClient cartaoClient;
	@Autowired ExecutorTransacao transacao;
	
	@PostMapping("/{id}/bloqueios")	
	public ResponseEntity<?> bloquear(
			@PathVariable(name = "id", required = true) @NotBlank String id, 
			@RequestHeader(name = "User-Agent", required = true) @NotBlank String userAgent,
			@AuthenticationPrincipal Jwt jwt,
			HttpServletRequest request) {
	
		List<Proposta> propostas = manager
				.createQuery("SELECT p FROM Proposta p WHERE p.cartao.id = :id", Proposta.class)
				.setParameter("id", id).getResultList();
		
		if(propostas.isEmpty()) return ResponseEntity.notFound().build();
		var proposta = propostas.get(0);
		
		Assert.state(jwt.getClaimAsString("email").equals(proposta.getEmail()), "Cartão não pertence ao usuário");
		
		try {
			cartaoClient.bloqueiaCartao(new SistemaResponsavel(sistemaResponsavel), proposta.getCartao().getNumero());
			
			Cartao cartao = proposta.getCartao();
			cartao.alteraStatus(StatusCartao.BLOQUEADO);
			transacao.atualizaEComita(cartao);
			
			Bloqueio bloqueio = new Bloqueio(request.getLocalAddr(), userAgent, cartao);
			transacao.salvaEComita(bloqueio);
		
			return ResponseEntity.ok().build();
			
		} catch (FeignClientException e) {
			return ResponseEntity.status(e.status()).body(new Erro("O cartão já está bloqueado"));
		} catch (FeignServerException e) {
			return ResponseEntity.status(e.status()).body(new Erro("Houve um erro no sistema"));
		}
	}
}
