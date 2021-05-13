package br.com.zupacademy.webservice_propostas.biometria;

import java.net.URI;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;


@RestController
public class BiometriaController {
	
	@Autowired SmartValidator validator;
	@PersistenceContext EntityManager manager;
	
	@PostMapping("/biometria/{cartaoId}")
	@Transactional
	public ResponseEntity<?> cadastrar(
			@RequestBody @Valid BiometriaRequest biometriaRequest,
			@PathVariable("cartaoId") String cartaoId,
			UriComponentsBuilder uriBuilder,
			BindingResult result) throws MethodArgumentNotValidException {
		
			if(! Base64.isBase64(biometriaRequest.getBiometria())) {
				result.rejectValue("biometria", null, "Não é um formato base64 válido");
				throw new MethodArgumentNotValidException(new MethodParameter(this.getClass().getMethods()[0], 0), result);
			}
			
			Cartao cartao = manager.find(Cartao.class, cartaoId);
			
			if(cartao == null) {
				return ResponseEntity.notFound().build();
			}
			
			Biometria biometria = biometriaRequest.converter(cartao);
			manager.persist(biometria);
			
			URI uri = uriBuilder.path("/biometria/{id}").buildAndExpand(biometria.getId()).toUri();
			
			return ResponseEntity.created(uri).build();
	} 
}
