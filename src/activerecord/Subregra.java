package activerecord;

import java.util.List;

public class Subregra {

	private int idRegra;
	private int id;
	private String previa;
	private String texto;
	private List<Termo> termos;
	
	public int getIdRegra() {
		return idRegra;
	}
	public void setIdRegra(int idRegra) {
		this.idRegra = idRegra;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPrevia() {
		return previa;
	}
	public void setPrevia(String previa) {
		this.previa = previa;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public List<Termo> getTermos() {
		return termos;
	}
	public Termo getTermo(int index) {
		return termos.get(index);
	}
	public int getNumTermos(){
		return termos.size();
	}
	public void setTermos(List<Termo> termos) {
		this.termos = termos;
	}
	
	
	
}
