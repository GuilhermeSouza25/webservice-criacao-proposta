package br.com.zupacademy.webservice_proposta.cartao.cartao_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.zupacademy.webservice_proposta.proposta.analisefinanceira_client.SolicitacaoAnalise;

@FeignClient(name = "cartao", url = "${cartoes.url}")
public interface CartaoClient {
	
	@RequestMapping(method = RequestMethod.POST, path = "/api/cartoes", consumes = "application/json")
	CartaoResponse solicitaCartao(@RequestBody SolicitacaoAnalise solicitacaoAnalise);
}
