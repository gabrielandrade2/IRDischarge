package activerecord;

public class TrechoEncontrado {

	private String trechoEncontrado;
	private Regra regra;
	private Subregra subregra;
	private boolean isSubregra = false;
	private boolean hasRegra = false;

	public Subregra getSubregra() {
		return subregra;
	}
	public void setSubregra(Subregra subregra) {
		this.subregra = subregra;
	}
	public boolean getIsSubregra() {
		return isSubregra;
	}
	public void setIsSubregra(boolean isSubregra) {
		this.isSubregra = isSubregra;
	}
	public boolean isHasRegra() {
		return hasRegra;
	}
	public void setHasRegra(boolean hasRegra) {
		this.hasRegra = hasRegra;
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
		this.hasRegra = true;
	}
	
	public boolean hasRegra(){
		return hasRegra;
	}
	
}
