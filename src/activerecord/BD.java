package activerecord;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gpri.view.DropDownInfo;

import com.mysql.jdbc.PreparedStatement;

public class BD extends ActiveRecord {
	
	public Login selectLogin(String usuario){
		
		Login Login = new Login();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT usuario, senha from usuarios WHERE usuario = '"+usuario+"';");
		    ResultSet res = ps.executeQuery();
		    while(res.next()){
		    	Login.setId(res.getInt("idUsuario"));
		    	Login.setUsuario(res.getString("Usuario"));
		    	Login.setSenha(res.getString("Senha"));
		    }
		    
		    return Login;
		    
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return Login;
			
	}
	
	//Não utilizado
	
	public boolean insertBD(String Insert){
		
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(Insert);		
			boolean erro = ps.execute();
			return erro;}
		
		catch (SQLException e) {
			System.out.println("Erro ao efetuar Insert");
			e.printStackTrace();}
		return false;
		}
	
	//Select Genérico
	public ResultSet selectBD(String s){
	
		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(s);
		    ResultSet res = ps.executeQuery();
		  	System.out.println(res.getString("usuario"));
		    return res;
		} 
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    
	}
	
	public List<Regra> findByElement(int elemento_id, int conjunto_id, boolean withTerms){
		
		List<Regra> list = new ArrayList<Regra>();
		
		 try {
			 
             PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM regras WHERE conjunto_id = ? AND elemento_id = ? AND isnull(regrapai_id) ORDER BY ordem");
             ps.setInt(1, conjunto_id);
             ps.setInt(2, elemento_id);
             ResultSet res = ps.executeQuery();
             
			 while (res.next()) {
				 
				 Regra r = new Regra();
				 
				 r.id = res.getInt("id");
				 r.regrapai_id = res.getInt("regrapai_id");
				 r.elemento_id = res.getInt("elemento_id");
				 r.ordem = res.getInt("ordem");
				 r.dataregra = res.getDate("dataregra");
				 r.previa = res.getString("previa");
				 r.observacao = res.getString("observacao");
				 
				 //Busca SUB-REGRAS
				 try{
					 
					 PreparedStatement pss = (PreparedStatement) con.prepareStatement("SELECT * FROM regras WHERE conjunto_id = ? AND elemento_id = ? AND regrapai_id = ? ORDER BY ordem");
					 pss.setInt(1, conjunto_id);
					 pss.setInt(2, elemento_id);
		             pss.setInt(3, r.id);
		             ResultSet ress = pss.executeQuery();
		             
					 while (ress.next()) {
						 
						 Regra rs = new Regra();
						 
						 rs.id = ress.getInt("id");
						 rs.regrapai_id = ress.getInt("regrapai_id");
						 rs.elemento_id = ress.getInt("elemento_id");
						 rs.ordem = ress.getInt("ordem");
						 rs.dataregra = ress.getDate("dataregra");
						 rs.previa = ress.getString("previa");
						 rs.observacao = ress.getString("observacao");
						 
						 //Se é para buscar termos das regras
						 if(withTerms){
						 
							 //Busca termos
							 rs.termos = new Termo().findByRegra(rs.id);
						 
						 }
						 
						 //Adiciona a lista de SUB-REGRAS
						 r.subregras.add(rs);
						 
					 } 
					
					 
				 }
				 catch (SQLException e) {
					 System.out.println("Erro ao buscar sub-regras!");
					 e.printStackTrace();
				 }
				 
				 //Se é para buscar termos das regras
				 if(withTerms){
				 
					 //Busca termos
					 r.termos = new Termo().findByRegra(r.id);
				 
				 }
				 
				 list.add(r);
				 
			 }
		 } catch (SQLException e) {
			 System.out.println("Erro ao buscar regras!");
			 e.printStackTrace();
		 }

		return list;
		
	}
	
	//Select para dropdownlistbox
public void selectDropDownListBox(String Tabela, DropDownInfo d){
		
		 try {
			 
			 PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * from "+Tabela);
             //ps.setString(1, Tabela);
             System.out.println(ps.toString());
             ResultSet res = ps.executeQuery();

			 while (res.next()) {
				 d.adiciona(res.getInt("id"), res.getString("abreviacao"), res.getString("nome"));
				 
				 
			 }
		 } catch (SQLException e) {
			 System.out.println("Erro ao buscar itens DropDownListBox");
			 System.out.println("Provavelmente coluna da tabela errada no método xD");
			 //e.printStackTrace();
		 }

		
	}

}
