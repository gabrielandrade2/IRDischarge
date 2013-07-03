package activerecord;

public class Login {
	
	private String usuario;
	private String senha;
	
	public Login(){
		usuario = "";
		senha = "";
	}

	public void setUsuario(String s){
		usuario = s;
	}
	
	public void setSenha(String s){
		senha = s;
	}
	
	public String getUsuario(){
		return usuario;
	}
	
	public String getSenha(){
		return senha;
	}
}