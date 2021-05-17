package br.com.zupacademy.webservice_propostas.cartao.cartao_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cartao", url = "${cartoes.url}")
public interface CartaoClient {
	
	@RequestMapping(method = RequestMethod.GET, path = "/api/cartoes", consumes = "application/json")
	CartaoResponse buscaCartaoGerado(@RequestParam(value = "idProposta") String idProposta);
}
