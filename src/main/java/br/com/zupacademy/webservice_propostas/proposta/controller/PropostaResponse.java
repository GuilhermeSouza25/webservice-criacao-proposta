package br.com.zupacademy.webservice_propostas.proposta.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import br.com.zupacademy.webservice_propostas.proposta.EstadoProposta;
import br.com.zupacademy.webservice_propostas.proposta.Proposta;

public class PropostaResponse {
	
	private String id;
	
	private String documento;
	
	private String email;
	
	private String nome;
	
	private String endereco;
	
	private BigDecimal salario;
	
	private EstadoProposta estado;
	
	private String cartao;
	
	public PropostaResponse(Proposta proposta) {
		this.id = proposta.getId().toString();
		this.documento = proposta.getDocumento();
		this.email = proposta.getEmail();
		this.nome = proposta.getNome();
		this.endereco = proposta.getEndereco();
		this.salario = proposta.getSalario();
		this.estado = proposta.getEstado();
		
		if(proposta.getCartao() == null) {
			cartao = null;
		} else {
			this.cartao = proposta.getCartao().getId();
		}
	}
	
	public String getId() {
		return id;
	}
	
	public String getDocumento() {
		return documento;
	}

	public String getEmail() {
		return email;
	}

	public String getNome() {
		return nome;
	}

	public String getEndereco() {
		return endereco;
	}

	public BigDecimal getSalario() {
		return salario;
	}
	
	public EstadoProposta getEstado() {
		return estado;
	}
	public String getCartao() {
		return cartao;
	}

	public static List<PropostaResponse> toList(List<Proposta> propostas) {
		return propostas.stream().map(PropostaResponse::new).collect(Collectors.toList());
	}

	public static Page<PropostaResponse> toPage(Page<Proposta> propostas) {
		return propostas.map(PropostaResponse::new);
	}
}
