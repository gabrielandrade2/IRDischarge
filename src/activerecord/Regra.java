package activerecord;

import java.util.List;

public class Regra{
	
	private int id;
	private int conjunto;
	private int elemento;
	private String previa;
	private String texto;
	private List<Termo> termos;
	private List<Subregra> subregras;
	private int idTexto;
	private int idArquivo;
	
	public int getIdTexto() {
		return idTexto;
	}
	public void setIdTexto(int idTexto) {
		this.idTexto = idTexto;
	}
	public int getIdArquivo() {
		return idArquivo;
	}
	public void setIdArquivo(int idArquivo) {
		this.idArquivo = idArquivo;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getConjunto() {
		return conjunto;
	}
	public void setConjunto(int conjunto) {
		this.conjunto = conjunto;
	}
	public int getElemento() {
		return elemento;
	}
	public void setElemento(int elemento) {
		this.elemento = elemento;
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
	public List<Subregra> getSubregras() {
		return subregras;
	}
	public void setSubregras(List<Subregra> subregras) {
		this.subregras = subregras;
	}
	public boolean hasSubregra(){
		return !(this.subregras.isEmpty());
	}
}