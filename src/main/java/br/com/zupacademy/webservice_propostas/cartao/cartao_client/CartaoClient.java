package br.com.zupacademy.webservice_propostas.cartao.cartao_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.zupacademy.webservice_propostas.cartao.aviso_viagem.AvisoViagemRequest;

@FeignClient(name = "cartao", url = "${cartoes.url}")
public interface CartaoClient {
	
	@RequestMapping(method = RequestMethod.GET, path = "/api/cartoes", consumes = "application/json")
	CartaoResponse buscaCartaoGerado(@RequestParam(value = "idProposta") String idProposta);
	
	@RequestMapping(method = RequestMethod.POST, path = "/api/cartoes/{id}/bloqueios", consumes = "application/json", produces = "application/json")
	void bloqueiaCartao(SistemaResponsavel sistemaResponsavel, @PathVariable("id") String id);
	
	@RequestMapping(method = RequestMethod.POST, path = "/api/cartoes/{id}/avisos", consumes = "application/json", produces = "application/json")
	void avisarViagem(@RequestBody AvisoViagemRequest aviso,  @PathVariable("id") String id);
	
	/**
	 * Classe auxiliar para poder enviar o sistema reponsável para o sistema de 
	 * bloqueio de cartões no formado JSON
	 * @author Jose
	 */
	public class SistemaResponsavel {
		
		private String sistemaResponsavel;

		public SistemaResponsavel(String sistemaResponsavel) {
			this.sistemaResponsavel = sistemaResponsavel;
		}

		public String getSistemaResponsavel() {
			return sistemaResponsavel;
		}
	}
}
