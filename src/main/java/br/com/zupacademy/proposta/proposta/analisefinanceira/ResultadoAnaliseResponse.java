package br.com.zupacademy.proposta.proposta.analisefinanceira;

public class ResultadoAnaliseResponse {
	
	private String documento;
	private String nome;
	private ResultadoAnalise resultadoSolicitacao;
	private String idProposta;
	
	public String getDocumento() {
		return documento;
	}

	public String getNome() {
		return nome;
	}

	public ResultadoAnalise getResultadoSolicitacao() {
		return resultadoSolicitacao;
	}

	public String getIdProposta() {
		return idProposta;
	}
}
