package br.com.zupacademy.webservice_propostas.proposta.analisefinanceira_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "analiseFinanceira", url = "${analisefinanceira.url}")
public interface AnaliseFinanceiraClient {
	
	@RequestMapping(method = RequestMethod.POST, path = "/api/solicitacao", consumes = "application/json")
	ResultadoAnalise solicitaAnalise(@RequestBody SolicitacaoAnalise solicitacaoAnalise);
}
