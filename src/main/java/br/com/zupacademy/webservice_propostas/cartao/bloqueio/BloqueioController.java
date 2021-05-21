package br.com.zupacademy.webservice_propostas.cartao.bloqueio;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;
import br.com.zupacademy.webservice_propostas.cartao.StatusCartao;
import br.com.zupacademy.webservice_propostas.cartao.cartao_client.CartaoClient;
import br.com.zupacademy.webservice_propostas.cartao.cartao_client.CartaoClient.SistemaResponsavel;
import br.com.zupacademy.webservice_propostas.shared.ExecutorTransacao;
import br.com.zupacademy.webservice_propostas.shared.exceptionhandler.Erro;
import feign.FeignException.FeignClientException;
import feign.FeignException.FeignServerException;
import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping("/propostas/cartoes")
@Validated
@Timed(value = "proposta_cartao_bloqueio")
public class BloqueioController {
	
	@Value("${application.name}")
	private String sistemaResponsavel;
	
	@PersistenceContext EntityManager manager;
	@Autowired CartaoClient cartaoClient;
	@Autowired ExecutorTransacao transacao;
	
	@PostMapping("/{id}/bloqueios")	
	public ResponseEntity<?> bloquear(
			@PathVariable(name = "id", required = true) @NotBlank String id, 
			@AuthenticationPrincipal Jwt jwt,
			@RequestHeader(name = "User-Agent", required = true) @NotBlank String userAgent,
			HttpServletRequest request) {
	
		List<Cartao> lista = manager
				.createQuery("SELECT p.cartao FROM Proposta p WHERE p.cartao.id = :id AND p.email = :email", Cartao.class)
				.setParameter("id", id)
				.setParameter("email", jwt.getClaimAsString("email"))
				.getResultList();
		
		if(lista.isEmpty()) return ResponseEntity.notFound().build();
		var cartao = lista.get(0);
		
		try {
			cartaoClient.bloqueiaCartao(new SistemaResponsavel(sistemaResponsavel), cartao.getNumero());
		
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
