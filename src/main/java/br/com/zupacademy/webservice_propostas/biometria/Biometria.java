package br.com.zupacademy.webservice_propostas.biometria;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import com.sun.istack.NotNull;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;

@Entity
public class Biometria {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY	)
	private Long id;
	
	@NotEmpty
	private String biometria;
	
	@SuppressWarnings("unused")
	private LocalDateTime dataCriacao = LocalDateTime.now();
	
	@NotNull
	@ManyToOne
	private Cartao cartao;
	
	
	public Biometria(@NotEmpty String biometria, @NotNull Cartao cartao) {
		super();
		this.biometria = biometria;
		this.cartao = cartao;
	}
	
	public Long getId() {
		return id;
	}
	
}
