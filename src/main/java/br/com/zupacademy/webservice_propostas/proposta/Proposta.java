package br.com.zupacademy.webservice_propostas.proposta;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import com.sun.istack.NotNull;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;

@Entity
public class Proposta {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty @NotBlank @ValidaCPFOuCNPJ
	private String documento;
	
	@NotEmpty @NotBlank @Email
	private String email;
	
	@NotEmpty @NotBlank
	private String nome;
	
	@NotEmpty @NotBlank
	private String endereco;
	
	@NotNull @Positive
	private BigDecimal salario;
	
	@Enumerated(EnumType.STRING)
	private EstadoProposta estado;
	
	@OneToOne(cascade = CascadeType.MERGE)
	private Cartao cartao;
	
	@Deprecated
	public Proposta() {}
	
	public Proposta(@NotEmpty @NotBlank @ValidaCPFOuCNPJ String documento, @NotEmpty @NotBlank @Email String email,
			@NotEmpty @NotBlank String nome, @NotEmpty @NotBlank String endereco, @Positive BigDecimal salario) {
		this.documento = documento;
		this.email = email;
		this.nome = nome;
		this.endereco = endereco;
		this.salario = salario;
	}
	
	public Long getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	public String getDocumento() {
		return documento;
	}
	public String getEmail() {
		return email;
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
	
	public Cartao getCartao() {
		return cartao;
	}
	
	/**
	 * Para atrelar o cartão à proposta depois do retorno
	 * do sistema de cartão.
	 * @param cartao
	 */
	public void associarCartao(Cartao cartao) {
		this.cartao = cartao;
	}
	
	/**
	 * Para alterar o estado da proposta após a solicitação da
	 * análise fincaneira
	 * @param estado
	 */
	public void alteraEstado(EstadoProposta estado) {
		this.estado = estado;
	}
}
