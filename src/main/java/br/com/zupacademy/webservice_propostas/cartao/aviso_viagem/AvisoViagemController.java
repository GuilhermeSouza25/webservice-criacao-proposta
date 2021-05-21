package br.com.zupacademy.webservice_propostas.cartao.aviso_viagem;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;
import br.com.zupacademy.webservice_propostas.cartao.cartao_client.CartaoClient;
import br.com.zupacademy.webservice_propostas.shared.ExecutorTransacao;
import br.com.zupacademy.webservice_propostas.shared.exceptionhandler.Erro;
import feign.FeignException.FeignClientException;

@RestController
@RequestMapping("propostas/cartoes/")
public class AvisoViagemController {
	
	@PersistenceContext EntityManager manager;
	@Autowired CartaoClient cartaoClient;
	@Autowired ExecutorTransacao transacao;
	
	@PostMapping("{id}/avisos")
	public ResponseEntity<?> cadastrarAviso(
			@RequestBody @Valid AvisoViagemRequest avisoRequest,
			@PathVariable(name = "id", required = true) @NotBlank String id,
			@AuthenticationPrincipal Jwt jwt,
			@RequestHeader(name = "User-Agent", required = true) @NotBlank String userAgent,
			@RequestHeader(name = "X-Forwarded-For", required = true) @NotBlank String ip) {
	
		
		List<Cartao> lista = manager
				.createQuery("SELECT p.cartao FROM Proposta p WHERE p.cartao.id = :id AND p.email = :email", Cartao.class)
				.setParameter("id", id)
				.setParameter("email", jwt.getClaimAsString("email"))
				.getResultList();
		
		if(lista.isEmpty()) return ResponseEntity.notFound().build();
		var cartao = lista.get(0);
		
		try {
			cartaoClient.avisarViagem(avisoRequest, cartao.getNumero());
		} catch (FeignClientException e) {
			return ResponseEntity.status(e.status()).body(new Erro("Já existe um aviso para esta data"));
		}
		
		AvisoViagem aviso = new AvisoViagem(ip, userAgent, cartao);
		transacao.salvaEComita(aviso);
		
		return ResponseEntity.ok().build();
	}
}
