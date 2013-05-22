package activerecord;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.PreparedStatement;

public class BD extends ActiveRecord {
	
	
	public boolean InsertElement(String Insert){
		
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(Insert);		
			boolean sql = ps.execute();
			return sql;}
		
		catch (SQLException e) {
			System.out.println("Erro ao inserir termos");
			e.printStackTrace();}
		return false;
		}

	//Select Genérico
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
	
}
