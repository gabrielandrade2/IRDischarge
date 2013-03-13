package util;

import java.sql.*;

public class ConnectTest {
    
	public static void main(String[] args) {
		
        String url = "jdbc:mysql://localhost:3306/irdischarge";
        String login = "root";
        String senha = "";

        try 
        {
        	Class.forName("com.mysql.jdbc.Driver").newInstance();
        	System.out.println("\nDriver carregado com sucesso!\n");
	        try
	        {
	        	Connection conn = DriverManager.getConnection(url, login, senha);
                try
                {
                	String sql = "SELECT * FROM elementos";
                    Statement stm = conn.createStatement();
                    try
                    {
                    	ResultSet rs = stm.executeQuery(sql);
                    	while (rs.next()) 
                    	{
                    		String nome = rs.getString("nome");
                    		String id = rs.getString("id");
                    		System.out.println("Codigo: " + id + "\nNome: " +nome);
                    		System.out.println("---------------------------------------");
                    	}
                    	System.out.println("\nConsulta realizada com sucesso!!!\n");                     
                    }
                    catch (Exception ex) 
                    {
                    	System.out.println("\nErro no resultset!");
                    }
                }
                catch (Exception ex) 
                {
                	System.out.println("\nErro no statement!");
                }
	        }
	        catch (Exception ex) 
	        {
	        	System.out.println("\nErro no connection!");
	        }   
        }
        catch (Exception ex) 
        {
            System.out.println("\nDriver nao pode ser carregado!");
        }
    }
}
