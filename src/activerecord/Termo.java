package activerecord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

public class Termo extends ActiveRecord {
	
	public int id;
	public int regra_id;
	public int tipotermo_id;
	public int ordem;
	public String termo;
	
	public int insert(){
		
		return 0;
		
	}
	
	public boolean update(){
		
		return false;
		
	}
	
	public boolean delete(){
		
		return false;
		
	}
	
	public boolean delete(int id){
		
		return false;
		
	}
	
	public Termo find(int id){
		
		return new Termo();
		
	}
	
	public List<Termo> findAll(){
		
		return null;
		
	}
	
	public List<Termo> findByRegra(int regra_id){
		
		List<Termo> termos = new ArrayList<Termo>();
		
		try{
		
			//Busca termos da regra
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM termosregras WHERE regra_id = ?");
			ps.setInt(1, regra_id);
			ResultSet res = ps.executeQuery();
			 
			while (res.next()) {
				 
				Termo t = new Termo();
				 
				t.id = res.getInt("id");
				t.regra_id = res.getInt("regra_id");
				t.tipotermo_id = res.getInt("tipotermo_id");
				t.ordem = res.getInt("ordem");
				t.termo = res.getString("termo");
				 
				termos.add(t);
				 
			 }
		 
		}
		catch (SQLException e) {
			System.out.println("Erro ao buscar termos das regras!");
			e.printStackTrace();
		}
		
		return termos;
		
	}
	
	
	
}
