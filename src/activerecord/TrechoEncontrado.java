package activerecord;

public class TrechoEncontrado {

	private String trechoEncontrado;
	private Regra regra;
	private boolean hasRegra = false;

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
		this.hasRegra = true;
	}
	
	public boolean hasRegra(){
		return hasRegra;
	}
	
}
