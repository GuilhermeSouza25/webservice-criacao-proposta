package br.com.zupacademy.proposta.proposta.analisefinanceira;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "analiseFinanceira", url = "http://localhost:9999/api/solicitacao")
public interface AnaliseFinanceira {
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	ResultadoAnaliseResponse solicitaAnalise(@RequestBody SolicitacaoAnaliseRequest solicitacaoAnalise);
}
