package activerecord;

public class TrechoEncontrado {

	private String textoPesquisado;
	private String trechoEncontrado;
	private Regra regra;
	
	public String getTextoPesquisado() {
		return textoPesquisado;
	}
	public void setTextoPesquisado(String textoPesquisado) {
		this.textoPesquisado = textoPesquisado;
	}
	public String getTrechoEncontrado() {
		return trechoEncontrado;
	}
	public void setTrechoEncontrado(String trechoEncontrado) {
		this.trechoEncontrado = trechoEncontrado;
	}
	public Regra getRegra() {
		return regra;
	}
	public void setRegra(Regra regra) {
		this.regra = regra;
	}
	
}
