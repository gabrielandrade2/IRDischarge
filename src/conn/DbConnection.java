package conn;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DbConnection {
	
	private String path = "jdbc:mysql://localhost:3306/irdischarge";
	private String usuario = "root";
	private String password = "root";
	private static DbConnection conexao = null;
	private Connection conn = null;

	private DbConnection() {
	}

	/**
	 * getInstance Controla a instancia da classe fazendo com que o
	 * construtor seja chamado somente uma vez
	 * 
	 * @return conexao instacia da classe
	 */
	public static DbConnection getInstance() {
		if (conexao == null) {
			conexao = new DbConnection();
		}
		return conexao;
	}

	/**
	 * Conecta ao banco de dados
	 * 
	 * @return conn
	 */
	public Connection getConnection() {
		
		if(conn == null){
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(path, usuario, password);
			} catch (ClassNotFoundException ex) {
			} catch (SQLException ex) {
			}
		}
		return conn;
	}

}
