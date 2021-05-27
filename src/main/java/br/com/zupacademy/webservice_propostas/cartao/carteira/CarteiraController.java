package br.com.zupacademy.webservice_propostas.cartao.carteira;

import java.net.URI;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;
import br.com.zupacademy.webservice_propostas.cartao.cartao_client.CartaoClient;
import br.com.zupacademy.webservice_propostas.shared.ExecutorTransacao;
import br.com.zupacademy.webservice_propostas.shared.exceptionhandler.Erro;
import br.com.zupacademy.webservice_propostas.shared.validators.ValorDoEnum;
import feign.FeignException.FeignClientException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Validated
@RestController
@RequestMapping("/propostas")
public class CarteiraController {
	
	@PersistenceContext EntityManager manager;
	@Autowired CartaoClient cartaoClient;
	@Autowired ExecutorTransacao transacao;
	@Autowired Tracer tracer;
	
	@ApiResponse(responseCode = "422", description = "Cartão já associado à carteira", 
			content = { @Content(schema = @Schema(implementation = Erro.class)) })
	@PostMapping(path = "/cartoes/{id}/carteiras")
	public ResponseEntity<?> solicitaInclusao(
			@PathVariable(name = "id") String id,
			@Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt,
			@RequestParam @ValorDoEnum(enumClass = CarteirasEnum.class) @NotNull String carteira,
			UriComponentsBuilder uriBuilder) {
		
		String email = jwt.getClaimAsString("email");
		Assert.state(email != null, "Email não está presente no token de autenticação");
		
		Span activeSpan = tracer.activeSpan();
		activeSpan.setTag("user.email", email);
		
		List<Cartao> lista = manager
			.createQuery("SELECT p.cartao FROM Proposta p WHERE p.cartao.id = :id AND p.email = :email", Cartao.class)
			.setParameter("id", id)
			.setParameter("email", email)
			.getResultList();	
				
		if(lista.isEmpty()) return ResponseEntity.notFound().build();
		var cartao = lista.get(0);
		
		InclusaoCarteiraRequest inclusaoCarteiraRequest = new InclusaoCarteiraRequest(jwt.getClaimAsString("email"), carteira);
		
		try {
			cartaoClient.solicitaInclusaoCarteira(inclusaoCarteiraRequest, cartao.getNumero());
		} catch (FeignClientException e) {
			return ResponseEntity.status(e.status()).body(new Erro("Cartão já está associado a esta carteira"));
		}
		
		CarteiraDigital carteiraDigital = inclusaoCarteiraRequest.converter(cartao);
		transacao.salvaEComita(carteiraDigital);
		
		URI uri = uriBuilder.path("/propostas/cartoes/{id}/carteiras/{id}")
				.buildAndExpand(cartao.getId(), carteiraDigital.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping("/cartoes/{cartaoId}/carteiras/{carteiraId}")
	public ResponseEntity<CarteiraDigitalResponse> detalhaCarteira(
			@PathVariable String cartaoId,
			@PathVariable String carteiraId,
			@Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt) {
		
		String query = "SELECT c FROM CarteiraDigital c "
				+ "WHERE c.id = :carteiraId "
				+ "AND c.cartao.id = :cartaoId "
				+ "AND c.email = :email";
		try {
			CarteiraDigital carteiraDigital = manager
				.createQuery(query, CarteiraDigital.class)
				.setParameter("carteiraId", carteiraId)
				.setParameter("cartaoId", cartaoId)
				.setParameter("email", jwt.getClaimAsString("email"))
				.getSingleResult();
			
			return ResponseEntity.ok(new CarteiraDigitalResponse(carteiraDigital));
			
		} catch (NoResultException e) {
			return ResponseEntity.notFound().build();
		}
		
	}
}
