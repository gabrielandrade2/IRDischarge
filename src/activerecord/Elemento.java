package activerecord;

import java.util.List;

public class Elemento extends ActiveRecord {
	
	public int id;
	public String abreviacao;
	public String nome;
	
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
	
	public Elemento find(int id){
		
		return new Elemento();
		
	}
	
	public List<Elemento> findAll(){
		
		return null;
		
	}
	
	
	
}
