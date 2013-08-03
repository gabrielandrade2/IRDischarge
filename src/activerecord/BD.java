package activerecord;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import br.gpri.view.DropDownInfo;

import com.mysql.jdbc.PreparedStatement;

public class BD extends ActiveRecord {
	
	public Login selectLogin(String usuario){
		Login Login = new Login();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT idUsuario, usuario, senha from usuarios WHERE usuario = '"+usuario+"';");
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
	
	public Stack<Arquivo> selectArquivos(int idUsuario){
		
		Stack<Arquivo> arquivosRecentes = new Stack();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT nomeArquivo, absolutePath from arquivos WHERE idUsuario = '"+idUsuario+"';");
		    ResultSet res = ps.executeQuery();
		    while(res.next()){
		    	Arquivo a = new Arquivo();
		    	a.setNome(res.getString("nomeArquivo"));
		    	a.setCaminho(res.getString("absolutePath"));
		    	arquivosRecentes.push(a);
		    }
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return arquivosRecentes;
	}
	
	private void limpaArquivosUsuario(int idUsuario){
		try{
				PreparedStatement ps = (PreparedStatement) con.prepareStatement("DELETE FROM arquivos where idUsuario = "+idUsuario+";");
				ps.execute();
	    }
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public boolean insertArquivos(int idUsuario,Stack<Arquivo> arquivosRecentes){
		limpaArquivosUsuario(idUsuario);
		try{
			for(int i=0; i<arquivosRecentes.size(); i++){
				Arquivo a = arquivosRecentes.elementAt(i);
				PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO arquivos values("+idUsuario+","+i+",'"+a.getCaminho()+"','"+a.getNome()+"');");
				ps.execute();
		    }
		    return false;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	public List<Elemento> selectElemento(){
		List<Elemento> Lista = new ArrayList();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM elementos;"); //Talvez separar por usuario
			ResultSet res = ps.executeQuery();
			while(res.next()){
				Elemento Elemento = new Elemento();
				Elemento.setId(res.getInt("idElemento"));
				Elemento.setNome(res.getString("nomeElemento"));
				Elemento.setDescricao(res.getString("descricaoElemento"));
				Lista.add(Elemento);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return Lista;
	}
	
	public List<Regra> selectRegraCadastro(int idTexto, String arquivo, int idUsuario){
		List<Regra> Lista = new ArrayList();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT idRegra,previa,texto FROM regras WHERE (idTexto="+idTexto+" AND arquivo='"+arquivo+"' AND idUsuario="+idUsuario+");");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				Regra r = new Regra();
				r.setId(res.getInt("idRegra"));
				r.setPrevia(res.getString("previa"));
				r.setTexto(res.getString("texto"));
				Lista.add(r);
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		return Lista;
}
	
	public List<Subregra> selectSubRegra(int idRegra){
		List<Subregra> Lista = new ArrayList<Subregra>();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM subregras WHERE idRegra="+idRegra+";");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				Subregra s = new Subregra();
				s.setIdRegra(idRegra);
				s.setId(res.getInt("idSubregra"));
				s.setPrevia(res.getString("previa"));
				s.setTexto(res.getString("texto"));
				Lista.add(s);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return Lista;
	}
	
	public boolean insertRegra(int idUsuario, Regra r, int idTexto, int caminhoArquivo){
		boolean erro = true;
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO regras (idUsuario,idRegra,idConjunto,idElemento,previa,texto,idTexto,arquivo) VALUES ("+idUsuario+","+r.getId()+","+r.getConjunto()+","+r.getElemento()+",'"+r.getPrevia()+"','"+r.getTexto()+"',"+idTexto+",'"+caminhoArquivo+"');");		
			 erro = ps.execute();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		erro = insertTermosRegras(r.getTermos()); 
		return erro;
		
	}
	
	public boolean insertTermosRegras(List<Termo> termosregras){
		boolean erro = true;
		for(int i=0; i<termosregras.size(); i++){
			try{
				Termo t = termosregras.get(i);
				PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO termosregras VALUES ("+t.getIdRegra()+","+t.getIdTermo()+","+t.getOrdem()+",'"+t.getTermo()+"');");		
				 erro = ps.execute();
				 if(erro == true)
					 break;
			}
			catch(SQLException e){
				e.printStackTrace();
			}
		}
		 
		return erro;
	}
	
	public int selectMaxIdRegra(){
		int maxId = 0;
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT MAX(idRegra)+1 FROM regras;");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				 maxId = res.getInt(0);
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		return maxId;
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
