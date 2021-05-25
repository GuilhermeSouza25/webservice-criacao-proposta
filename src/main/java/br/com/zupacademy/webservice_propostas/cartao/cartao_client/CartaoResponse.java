  
package br.com.zupacademy.webservice_propostas.cartao.cartao_client;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;
import br.com.zupacademy.webservice_propostas.proposta.Proposta;
import br.com.zupacademy.webservice_propostas.shared.validators.ExistsId;

public class CartaoResponse {
	
	@NotBlank
	private String id;
	@NotNull
	private LocalDateTime emitidoEm;
	@NotBlank
	private String titular;
	@Positive
	private BigDecimal limite;
	
	@Pattern(regexp = "[\\s]*[0-9]*[1-9]+")
	@ExistsId(domainClass = Proposta.class, fieldName = "id")
	private String idProposta;
	
	@NotNull
	@Valid
	private VencimentoResponse vencimento;

	public CartaoResponse(@NotBlank String id, @NotNull LocalDateTime emitidoEm, @NotBlank String titular,
			@Positive BigDecimal limite,
			@Pattern(regexp = "[\\s]*[0-9]*[1-9]+") @ExistsId(domainClass = Proposta.class, fieldName = "id") String idProposta,
			@NotNull @Valid VencimentoResponse vencimento) {
		this.id = id;
		this.emitidoEm = emitidoEm;
		this.titular = titular;
		this.limite = limite;
		this.idProposta = idProposta;
		this.vencimento = vencimento;
	}
	
	public String getId() {
		return id;
	}
	
	public String getIdProposta() {
		return idProposta;
	}
	
	public Cartao converter() {
		return new Cartao(id, emitidoEm, titular, limite, vencimento);
	}
}