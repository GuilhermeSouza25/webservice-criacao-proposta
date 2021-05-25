package br.com.zupacademy.webservice_propostas.cartao.carteira;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;

@Entity
public class CarteiraDigital {
	
	@Id 
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
	
	@NotBlank
	private String email;
	
	private LocalDateTime associadaEm = LocalDateTime.now();
	
	@NotBlank
	private String emissor;
	
	@ManyToOne
	private Cartao cartao;
	
	@Deprecated
	public CarteiraDigital() {}
	
	public CarteiraDigital(@NotBlank String email, @NotBlank String emissor, Cartao cartao) {
		super();
		this.email = email;
		this.emissor = emissor;
		this.cartao = cartao;
	}
	
	public String getId() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getEmissor() {
		return emissor;
	}
	
	public LocalDateTime getAssociadaEm() {
		return associadaEm;
	}

	public String formataData(String pattern) {
		return this.associadaEm.format(DateTimeFormatter.ofPattern(pattern));
	}
}
