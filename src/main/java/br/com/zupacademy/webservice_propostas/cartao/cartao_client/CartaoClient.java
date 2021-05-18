package br.com.zupacademy.webservice_propostas.cartao.cartao_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cartao", url = "${cartoes.url}")
public interface CartaoClient {
	
	@RequestMapping(method = RequestMethod.GET, path = "/api/cartoes", consumes = "application/json")
	CartaoResponse buscaCartaoGerado(@RequestParam(value = "idProposta") String idProposta);
	
	@RequestMapping(method = RequestMethod.POST, path = "/api/cartoes/{id}/bloqueios", consumes = "application/json", produces = "application/json")
	void bloqueiaCartao(SistemaResponsavel sistemaResponsavel, @PathVariable("id") String id);
	
	
	public class SistemaResponsavel {
		
		private String sistemaResponsavel;

		public SistemaResponsavel(String sistemaResponsavel) {
			super();
			this.sistemaResponsavel = sistemaResponsavel;
		}
		
		public String getSistemaResponsavel() {
			return sistemaResponsavel;
		}
		
		public void setSistemaResponsavel(String sistemaResponsavel) {
			this.sistemaResponsavel = sistemaResponsavel;
		}
	}
}
