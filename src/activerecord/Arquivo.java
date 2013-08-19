package activerecord;

public class Arquivo {

	private String nome;
	private String caminho;
	
	public void setNome(String s){
		nome = s;
	}
	
	public void setCaminho(String s){
		s = s.replace("\\", "/");
		caminho = s;
	}
	
	public String getNome(){
		return nome;
	}
	
	public String getCaminho(){
		return caminho;
	}
}
