package activerecord;

public class Login {
	
	private int idUsuario;
	private String usuario;
	private String senha;
	
	public Login(){
		idUsuario = 0;
		usuario = "";
		senha = "";
	}

	public void setId(int i){
		idUsuario = i;
	}
	
	public void setUsuario(String s){
		usuario = s;
	}
	
	public void setSenha(String s){
		senha = s;
	}
	
	public int getId(){
		return idUsuario;
	}
	
	public String getUsuario(){
		return usuario;
	}
	
	public String getSenha(){
		return senha;
	}
}