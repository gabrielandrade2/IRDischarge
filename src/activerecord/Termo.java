package activerecord;


public class Termo extends ActiveRecord {
	
	public int idRegra;
	public int idSubregra;
	public int idTermo;
	public int ordem;
	public String termo;
	public String texto;
	
	
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public int getIdRegra() {
		return idRegra;
	}
	public void setIdRegra(int idRegra) {
		this.idRegra = idRegra;
	}
	public int getIdTermo() {
		return idTermo;
	}
	public void setIdTermo(int idTermo) {
		this.idTermo = idTermo;
	}
	public int getOrdem() {
		return ordem;
	}
	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}
	public String getTermo() {
		return termo;
	}
	public void setTermo(String termo) {
		this.termo = termo;
	}
	public int getIdSubregra() {
		return idSubregra;
	}
	public void setIdSubregra(int idSubregra) {
		this.idSubregra = idSubregra;
	}
}
