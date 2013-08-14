package activerecord;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

public class Regra extends ActiveRecord {
	
	private int id;
	private int conjunto;
	private int elemento;
	private String previa;
	private String texto;
	private List<Termo> termos;
	private int idTexto;
	private String caminhoArquivo;
	
	public int getIdTexto() {
		return idTexto;
	}
	public void setIdTexto(int idTexto) {
		this.idTexto = idTexto;
	}
	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}
	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
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
	
	
}