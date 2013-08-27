package activerecord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
		
		Stack<Arquivo> arquivosRecentes = new Stack<Arquivo>();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT idArquivo, ordem,nomeArquivo, absolutePath from arquivos WHERE idUsuario = '"+idUsuario+" ORDER BY ordem ASC';");
		    ResultSet res = ps.executeQuery();
		    while(res.next()){
		    	Arquivo a = new Arquivo();
		    	a.setId(res.getInt("idArquivo"));
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
				PreparedStatement ps;
				ps = (PreparedStatement) con.prepareStatement("INSERT INTO arquivos values("+idUsuario+","+a.getId()+","+i+",'"+a.getCaminho()+"','"+a.getNome()+"');");
				ps.execute();
		    }
		    return false;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	public int selectMaxIdArquivo(int idUsuario){
		int maxId = 0;
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT MAX(idArquivo)+1 FROM arquivos where idUsuario ="+idUsuario+";");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				 maxId = res.getInt(1);
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		return maxId;
	}
	
	public List<Elemento> selectElemento(){
		List<Elemento> Lista = new ArrayList<Elemento>();
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
	
	public List<Conjunto> selectConjunto(){
		List<Conjunto> Lista = new ArrayList<Conjunto>();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM conjuntos;"); 
			ResultSet res = ps.executeQuery();
			while(res.next()){
				Conjunto Conjunto= new Conjunto();
				Conjunto.setId(res.getInt("idConjunto"));
				Conjunto.setNome(res.getString("nomeConjunto"));
				Lista.add(Conjunto);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return Lista;
	}
	
	public List<Regra> selectRegraCadastro(int idTexto, int idArquivo, int idUsuario){
		List<Regra> Lista = new ArrayList<Regra>();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT idRegra,previa,texto FROM regras WHERE (idTexto="+idTexto+" AND idArquivo='"+idArquivo+"' AND idUsuario="+idUsuario+");");
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
	
	public List<Regra> selectRegraExecucao(int idUsuario, int idConjunto, int idElemento){
		List<Regra> Lista = new ArrayList<Regra>();
		PreparedStatement ps = null;
		try{
			if(idConjunto == 0 && idElemento == 0)
				ps = (PreparedStatement) con.prepareStatement("SELECT idRegra,previa,texto,idElemento FROM regras WHERE (idUsuario="+idUsuario+");");
			else if(idConjunto == 0)
				ps = (PreparedStatement) con.prepareStatement("SELECT idRegra,previa,texto,idElemento FROM regras WHERE (idUsuario="+idUsuario+" AND idElemento="+idElemento+");");
			else if(idElemento == 0)
				ps = (PreparedStatement) con.prepareStatement("SELECT idRegra,previa,texto,idElemento FROM regras WHERE (idUsuario="+idUsuario+" AND idConjunto="+idConjunto+");");
			else
				ps = (PreparedStatement) con.prepareStatement("SELECT idRegra,previa,texto,idElemento FROM regras WHERE (idUsuario="+idUsuario+" AND idElemento="+idElemento+" AND idConjunto="+idConjunto+");");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				Regra r = new Regra();
				r.setId(res.getInt("idRegra"));
				r.setPrevia(res.getString("previa"));
				r.setTexto(res.getString("texto"));
				r.setElemento(res.getInt("idElemento"));
				Lista.add(r);
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		return Lista;
}
	
	public List<Subregra> selectSubRegra(Regra r){
		int idRegra = r.getId();
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
	
	public Regra selectTermoRegra(Regra r){
		List<Termo> Lista = new ArrayList<Termo>();
		int idRegra = r.getId();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM termosregras WHERE idRegra="+idRegra+";");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				Termo t = new Termo();
				t.setIdRegra(idRegra);
				t.setIdTermo(res.getInt("idTermo"));
				t.setOrdem(res.getInt("ordem"));
				t.setTermo(res.getString("termo"));
				Lista.add(t);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		r.setTermos(Lista);
		return r;
	}
	
	public Subregra selectTermoSubregra(Subregra s){
		List<Termo> Lista = new ArrayList<Termo>();
		int idRegra = s.getIdRegra();
		int idSubregra = s.getId();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM termossubregras WHERE idRegra="+idRegra+" AND idSubRegra="+idSubregra+";");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				Termo t = new Termo();
				t.setIdRegra(idRegra);
				t.setIdSubregra(idSubregra);
				t.setIdTermo(res.getInt("idTermo"));
				t.setOrdem(res.getInt("ordem"));
				t.setTermo(res.getString("termo"));
				Lista.add(t);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		s.setTermos(Lista);
		return s;
	}
	
	public boolean insertRegra(int idUsuario, Regra r){
		boolean erro = true;
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO regras (idUsuario,idRegra,idConjunto,idElemento,previa,texto,idTexto,idArquivo) VALUES ("+idUsuario+","+r.getId()+","+r.getConjunto()+","+r.getElemento()+",'"+r.getPrevia()+"','"+r.getTexto()+"',"+r.getIdTexto()+",'"+r.getIdArquivo()+"');");		
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
				 maxId = res.getInt(1);
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		return maxId;
	}
	
	public boolean insertSubRegra(Subregra s){
		boolean erro = true;
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO subregras (idRegra, idSubregra, previa, texto) VALUES ("+s.getIdRegra()+","+s.getId()+",'"+s.getPrevia()+"','"+s.getTexto()+"');");		
			 erro = ps.execute();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
				
		erro = insertTermosSubRegras(s.getTermos()); 
		return erro;
		
	}
	
	public boolean insertTermosSubRegras(List<Termo> termossubregras){
		boolean erro = true;
		for(int i=0; i<termossubregras.size(); i++){
			try{
				Termo t = termossubregras.get(i);
				PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO termossubregras VALUES ("+t.getIdRegra()+","+t.getIdSubregra()+","+t.getIdTermo()+","+t.getOrdem()+",'"+t.getTermo()+"');");		
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
	
	public int selectMaxIdSubRegra(int idRegra){
		int maxId = 0;
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT MAX(idSubRegra)+1 FROM subregras WHERE idRegra = "+idRegra+";");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				 maxId = res.getInt(1);
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		return maxId;
	}
	
	public boolean insertUsuario(String usuario, String senha, String nome, String email){
		
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO usuarios (Usuario,Senha,Nome,Email) VALUES('"+usuario+"','"+senha+"','"+nome+"','"+email+"');");		
			boolean erro = ps.execute();
			return erro;}
		
		catch (SQLException e) {
			System.out.println("Erro ao efetuar Insert");
			e.printStackTrace();}
		return false;
		}
	
	public String selectTexto(int idUsuario, int idArquivo, int idTexto){
		String texto = new String();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT texto from textos WHERE idUsuario="+idUsuario+" AND idArquivo="+idArquivo+" AND idTexto="+idTexto+";");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				 texto = res.getString("texto");
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
			texto="Erro Banco de Dados";
		}
		return texto;
	}
	
	public int getNumTextos(int idUsuario, int idArquivo){
		int numTextos=0;
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT Count(*) from textos WHERE idUsuario="+idUsuario+" AND idArquivo="+idArquivo+";");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				 numTextos = (res.getInt(1));
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		return numTextos;
	}
	
	public boolean importaTexto(int idUsuario, int idArquivo, int idTexto, String texto){
		boolean erro = true;
		if(!texto.equals("")){
			try{
				PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO textos VALUES ("+idUsuario+","+idArquivo+","+idTexto+",'"+texto+"');");		
				 erro = ps.execute();
				 erro = false;
			}
			catch(SQLException e){
				e.printStackTrace();
			}
		}
		return erro;
	}
	
	public int selectIdArquivo(String caminhoArquivo){
		int id = -1;
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT idArquivo from arquivos where absolutePath='"+caminhoArquivo+"';");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				 id = res.getInt("idArquivo");
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		return id;
	}
	
	public boolean insertResultados(int idUsuario, int idArquivo, int idTexto, List<TrechoEncontrado> encontrados){
		boolean erro = true;
		for(int i=0; i<encontrados.size(); i++){
			TrechoEncontrado t = encontrados.get(i);
			String trecho = t.getTrechoEncontrado();
			
			int id = selectMaxIdResultados(idUsuario, idArquivo, idTexto);
			
			try{
				PreparedStatement ps = null;
				if(t.hasRegra()){
					Regra r = t.getRegra();
					int idRegra = r.getId();
					ps = (PreparedStatement) con.prepareStatement("INSERT into resultados(idUsuario, idArquivo, idTexto, id, trechoEncontrado, idRegra) values ("+idUsuario+","+idArquivo+","+idTexto+","+id+",'"+trecho+"',"+idRegra+");");
				}
				else
					ps = (PreparedStatement) con.prepareStatement("INSERT into resultados(idUsuario, idArquivo, idTexto, id, trechoEncontrado) values ("+idUsuario+","+idArquivo+","+idTexto+","+id+",'"+trecho+"');");
				 erro = ps.execute();
				 erro = false;
			}
			catch(SQLException e){
				e.printStackTrace();
				erro = true;
			}
			
		}
		return erro;
	}
	
	private int selectMaxIdResultados(int idUsuario, int idArquivo, int idTexto){
		int maxId = 0;
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT MAX(id)+1 from resultados WHERE idUsuario="+idUsuario+" AND idArquivo="+idArquivo+" and idTexto="+idTexto+";");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				 maxId = res.getInt(1);
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		return maxId;
	}
	
}
