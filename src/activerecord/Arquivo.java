package activerecord;

public class Arquivo {

	private int id = -1;
	private String nome;
	private String caminho;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
