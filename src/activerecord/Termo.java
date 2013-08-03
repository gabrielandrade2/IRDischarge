package activerecord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

public class Termo extends ActiveRecord {
	
	public int idRegra;
	public int idTermo;
	public int ordem;
	public String termo;
	
	
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
		
}
