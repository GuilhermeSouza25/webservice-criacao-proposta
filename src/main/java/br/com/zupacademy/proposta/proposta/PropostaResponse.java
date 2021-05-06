package br.com.zupacademy.proposta.proposta;

import java.math.BigDecimal;

public class PropostaResponse {
	
	private String documento;
	
	private String email;
	
	private String nome;
	
	private String endereco;
	
	private BigDecimal salario;
	
	public PropostaResponse(Proposta proposta) {
		this.documento = proposta.getDocumento();
		this.email = proposta.getEmail();
		this.nome = proposta.getNome();
		this.endereco = proposta.getEndereco();
		this.salario = proposta.getSalario();
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
	
	
}
