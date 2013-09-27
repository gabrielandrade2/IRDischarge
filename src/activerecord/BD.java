package activerecord;
import br.gpri.nlp.Tagger;
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
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT idArquivo, ordem,nomeArquivo, absolutePath from arquivos WHERE idUsuario = "+idUsuario+" ORDER BY ordem ASC;");
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
				PreparedStatement ps = (PreparedStatement) con.prepareStatement("UPDATE arquivos set ordem = null where idUsuario ="+idUsuario+";");
				ps.execute();
	    }
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public boolean insertArquivos(int idUsuario,Stack<Arquivo> arquivosRecentes){
		try{
			Arquivo a = arquivosRecentes.elementAt(0);
			PreparedStatement ps;
			ps = (PreparedStatement) con.prepareStatement("INSERT INTO arquivos values("+idUsuario+","+a.getId()+","+null+",'"+a.getCaminho()+"','"+a.getNome()+"');");
			ps.execute();
			atualizaArquivos(idUsuario, arquivosRecentes);
		    return false;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return true;
	}

	public boolean atualizaArquivos(int idUsuario,Stack<Arquivo> arquivosRecentes){
		limpaArquivosUsuario(idUsuario);
		try{
			for(int i=0; i<arquivosRecentes.size(); i++){
				Arquivo a = arquivosRecentes.elementAt(i);
				PreparedStatement ps;
				ps = (PreparedStatement) con.prepareStatement("UPDATE arquivos set ordem ="+i+" where idUsuario ="+idUsuario+" AND idArquivo="+a.getId()+";");
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
	
	public Regra selectRegra(int idUsuario, int idRegra){
		Regra r = new Regra();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM regras WHERE (idUsuario="+idUsuario+" AND idRegra="+idRegra+");");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				r.setId(idRegra);
				r.setPrevia(res.getString("previa"));
				r.setTexto(res.getString("texto"));
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		return r;
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
				ps = (PreparedStatement) con.prepareStatement("SELECT idRegra,previa,texto,idElemento,idConjunto FROM regras WHERE (idUsuario="+idUsuario+");");
			else if(idConjunto == 0)
				ps = (PreparedStatement) con.prepareStatement("SELECT idRegra,previa,texto,idElemento,idConjunto FROM regras WHERE (idUsuario="+idUsuario+" AND idElemento="+idElemento+");");
			else if(idElemento == 0)
				ps = (PreparedStatement) con.prepareStatement("SELECT idRegra,previa,texto,idElemento,idConjunto FROM regras WHERE (idUsuario="+idUsuario+" AND idConjunto="+idConjunto+");");
			else
				ps = (PreparedStatement) con.prepareStatement("SELECT idRegra,previa,texto,idElemento,idConjunto FROM regras WHERE (idUsuario="+idUsuario+" AND idElemento="+idElemento+" AND idConjunto="+idConjunto+");");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				Regra r = new Regra();
				r.setId(res.getInt("idRegra"));
				r.setPrevia(res.getString("previa"));
				r.setTexto(res.getString("texto"));
				r.setElemento(res.getInt("idElemento"));
				r.setConjunto(res.getInt("idConjunto"));
				Lista.add(r);
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		return Lista;
}
	
	public List<Subregra> selectSubRegras(Regra r){
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
	
	
	public Subregra selectSubRegra(int idRegra, int idSubregra){
		Subregra s = new Subregra();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT previa,texto FROM subregras WHERE idRegra="+idRegra+" AND idSubregra="+idSubregra+";");
			ResultSet res = ps.executeQuery();
			while(res.next()){
					s.setIdRegra(idRegra);
					s.setId(idSubregra);
					s.setPrevia(res.getString("previa"));
					s.setTexto(res.getString("texto"));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return s;
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
		int maxId = 1;
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT MAX(idRegra)+1 FROM regras;");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				 maxId = res.getInt(1)+1;
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
		int maxId = 1;
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT MAX(idSubRegra) FROM subregras WHERE idRegra = "+idRegra+";");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				 maxId = res.getInt(1)+1;
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
	
	public List<String> selectTextos(int idUsuario, int idArquivo){
		List<String> textos = new ArrayList<String>();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT texto from textos WHERE idUsuario="+idUsuario+" AND idArquivo="+idArquivo+";");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				String texto;
				texto = res.getString("texto");
				textos.add(texto);
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return textos;
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
	
	private int countTextos(int idArquivo){
		int countTextos = 0;
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT Count(*) from textos where idArquivo="+idArquivo+";");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				countTextos = res.getInt("Count(*)");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return countTextos;
	}
	
	public List<List<TrechoEncontrado>> selectResultados(int idExecucao, int idArquivo, int idUsuario){
		List<String> trechosDistintos = selectDistinctTrechoEncontrado(idExecucao, idArquivo, idUsuario);
		List<List<TrechoEncontrado>> lista = new ArrayList<List<TrechoEncontrado>>();
		int idTexto =-1;
		int idTextoAnt=-1;
		boolean once = true;
		try{
		PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM resultados WHERE idExecucao="+idExecucao+" ORDER BY idTexto;");
		ResultSet res = ps.executeQuery();
		List<TrechoEncontrado> l = new ArrayList<TrechoEncontrado>();
				while(res.next()){
					if(once){
						idTexto = res.getInt("idTexto");
						once = false;
					}
					if(!(res.getInt("idTexto") == idTexto)){
						lista.add(l);
						l = new ArrayList<TrechoEncontrado>();
						idTexto = res.getInt("idTexto");
						}
						
					TrechoEncontrado t = new TrechoEncontrado();
					if(res.getInt("isSubregra") == 1){
						Subregra s = selectSubRegra(res.getInt("idRegra"), res.getInt("idSubregra"));
						t.setSubregra(s);
						t.setIsSubregra(true);
					}
					Regra r = selectRegra(idUsuario, res.getInt("idRegra"));
					t.setRegra(r);
					t.setTrechoEncontrado(res.getString("trechoEncontrado"));
//executar médoto passando a lista e o idtexto para que este método faça os edits e inserts
					int idTexto2=res.getInt("idTexto");
					if(idTextoAnt!=idTexto2)
					{
//						insereRapidMiner(trechosDistintos, idExecucao, idArquivo, idUsuario, idTexto);
						idTextoAnt=idTexto2;
					}
					l.add(t);
				}
				
			}
			
			catch(SQLException e){
				e.printStackTrace();
			}
			
		
		return lista;
	}

	public List<String> selectDistinctTrechoEncontrado(int idExecucao, int idArquivo, int idUsuario){
			List<String> lista = new ArrayList<String>();
			try{
				PreparedStatement psResultado = (PreparedStatement) con.prepareStatement(
				"SELECT distinct(trechoencontrado) FROM resultados WHERE idExecucao="+idExecucao+";");
				ResultSet res = psResultado.executeQuery();
				while(res.next()){
					lista.add(res.getString("trechoEncontrado"));
					}
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			
			
		
		return lista;
	}
	
	public List<String> selectDistinctTrechoEncontradoDeUmResultado(int idExecucao, int idArquivo, int idUsuario, int idTexto){
		List<String> lista = new ArrayList<String>();
		try{
			PreparedStatement psResultado = (PreparedStatement) con.prepareStatement(
			"SELECT distinct(trechoencontrado) FROM resultados WHERE idExecucao="+idExecucao+" and idTexto="+idTexto+";");
			ResultSet res = psResultado.executeQuery();
			while(res.next()){
				lista.add(res.getString("trechoEncontrado"));
				}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		
	
	return lista;
}

	public boolean insereRapidMiner(List<String> trechosDistintos,int idExecucao, int idArquivo, int idUsuario, int idTexto)
	{
		try{
			List<String> trechosDistintosDeUmResultado = 
					selectDistinctTrechoEncontradoDeUmResultado(idExecucao, idArquivo, idUsuario,idTexto);
			PreparedStatement psResultado = (PreparedStatement) con.prepareStatement(
"SELECT texto FROM textos WHERE idTexto="+idTexto+";");
			ResultSet res = psResultado.executeQuery();
			while(res.next()){
				String td="";

				Tagger tg=new Tagger(this);
				String textoAuxiliar=tg.sentencaRapidMiner(res.getString("texto"));
				
				for(int i=0;i<trechosDistintosDeUmResultado.size();i++)
				{
					td = trechosDistintosDeUmResultado.get(i);
					td=td.trim();
					textoAuxiliar=textoAuxiliar.replace(td, td.replace(" ", "_"));
				}
				for(int i=0;i<trechosDistintos.size();i++)
				{
					td = trechosDistintos.get(i);
					td=td.trim();
					if(!textoAuxiliar.contains(td.replace(" ", "_")))
					{
						textoAuxiliar=textoAuxiliar.concat(" NAO_"+td.replace(" ", "_"));
					}
				}
				PreparedStatement ps2 = null;
				ps2 = (PreparedStatement) con.prepareStatement(
				"INSERT into textoRapidMiner(idtexto,texto) values ("+idTexto+", '"+textoAuxiliar+"');");
				boolean erro = ps2.execute();
				erro = false;
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return true;
	}

	public boolean insertResultados(int idTexto, List<TrechoEncontrado> encontrados, int idExecucao){
		boolean erro = true;
		
		for(int i=0; i<encontrados.size(); i++){
			TrechoEncontrado t = encontrados.get(i);
			String trecho = t.getTrechoEncontrado();
			
			try{
				PreparedStatement ps = null;
				if(t.isSubregra())
				{
						Subregra sr = t.getSubregra();
						int idSubregra = sr.getId();
						int idRegra = sr.getIdRegra();
						ps = (PreparedStatement) con.prepareStatement("INSERT into resultados(idTexto, idExecucao, trechoEncontrado, idRegra, idSubregra,isSubregra) values ("+idTexto+","+idExecucao+",'"+trecho+"',"+idRegra+","+idSubregra+",1);");
				}
				else
				{
				if(t.hasRegra()){
					Regra r = t.getRegra();
					int idRegra = r.getId();
					ps = (PreparedStatement) con.prepareStatement("INSERT into resultados(idTexto, idExecucao, trechoEncontrado, idRegra,isSubregra) values ("+idTexto+","+idExecucao+",'"+trecho+"',"+idRegra+",0);");
				}
				
				else
					ps = (PreparedStatement) con.prepareStatement("INSERT into resultados(idTexto, idExecucao, trechoEncontrado) values ("+idTexto+","+idExecucao+",'Nada Encontrado');");
				}
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
	
	
	public int insertExecucao(int idUsuario, int idArquivo){
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT into execucoes(idUsuario,idArquivo) values("+idUsuario+","+idArquivo+");");
			ps.execute();
		}
		catch(SQLException e){
			e.printStackTrace();
			System.out.println("Falha ao inserir execução");
		}
		int id = -1;
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("Select MAX(id) from execucoes;");
			ResultSet res = ps.executeQuery();
			while(res.next())
				 id = res.getInt(1);
		}
		catch(SQLException e){
			e.printStackTrace();
			System.out.println("Erro ao recuperar id execução");
		}
		return id;
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
	
	public List<Execucao> selectExecucoes(int idUsuario){
		List<Execucao> execucoes = new ArrayList<Execucao>();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT id,dataExecucao,nomeArquivo,execucoes.idArquivo from execucoes JOIN arquivos ON execucoes.idArquivo = arquivos.idArquivo WHERE execucoes.idUsuario="+idUsuario+";");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				Execucao e = new Execucao();
				e.setId(res.getInt("id"));
				e.setData(res.getString("dataExecucao"));
				e.setArquivo(res.getString("nomeArquivo"));
				e.setIdArquivo(res.getInt("idArquivo"));
				execucoes.add(e);
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
	
		return execucoes;
	}
	
	public List<Execucao> selectExecucoes(int idUsuario, int idArquivo){
	List<Execucao> execucoes = new ArrayList<Execucao>();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT id,dataExecucao,nomeArquivo,execucoes.idArquivo from execucoes JOIN arquivos ON execucoes.idArquivo = arquivos.idArquivo WHERE execucoes.idUsuario="+idUsuario+" AND execucoes.idArquivo="+idArquivo+";");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				Execucao e = new Execucao();
				e.setId(res.getInt("id"));
				e.setData(res.getString("dataExecucao"));
				e.setArquivo(res.getString("nomeArquivo"));
				e.setIdArquivo(res.getInt("idArquivo"));
				execucoes.add(e);
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
	
		return execucoes;
	}
	
	public List<Acronimo> selectAcronimos(){
		List<Acronimo> acronimos = new ArrayList<Acronimo>();
		try{
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * from acronimos");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				Acronimo a = new Acronimo();
				a.setId(res.getInt("id"));
				a.setAcronimo(res.getString("acronimo"));
				a.setExpansao(res.getString("expansao"));
				acronimos.add(a);
			}
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
	
		return acronimos;
	}

}
