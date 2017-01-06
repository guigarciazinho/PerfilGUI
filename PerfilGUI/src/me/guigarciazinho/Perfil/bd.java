package me.guigarciazinho.Perfil;

import java.sql.*;
import java.util.UUID;



public class bd{
	public Connection con = null;
	public Statement s;
	public PreparedStatement p;
	public String user = Principal.getInstance().getConfig().getString("user");
	public String senha = Principal.getInstance().getConfig().getString("senha");
	public String urlconf = Principal.getInstance().getConfig().getString("url");
	public String dbname = Principal.getInstance().getConfig().getString("dbname");
	
	public void conectar(){
		final String driver = "com.mysql.jdbc.Driver";
		final String url = "jdbc:mysql://"+urlconf +":3306/" + dbname;
		
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, senha);
		}catch(ClassNotFoundException erro){
			System.out.println("Driver não encontrado." + erro.toString());
		}catch(SQLException erro){
			System.out.println("Ocorreu um erro ao tentar se conectar com o banco de dados." + erro.toString());
		}
	}
		
	
	
	public void criar(){
		final String driver = "com.mysql.jdbc.Driver";
		final String url = "jdbc:mysql://"+urlconf +":3306/" + dbname;
		
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, senha);
			s = con.createStatement();
			s.executeUpdate("create table if not exists perfil("
                          +" id varchar(36) not null,"
                          +" nick varchar(16) not null,"
                          +" skype varchar(15),"
                          +" youtube varchar(40),"
                          +" nome varchar(30),"
                          +" pensamento varchar(30),"
                          +" primary key(id)"
						  +")default charset = utf8;");
			s.close();
			con.close();
		}catch(ClassNotFoundException erro){
			System.out.println("Driver não encontrado." + erro.toString());
		}catch(SQLException erro){
			System.out.println("Ocorreu um erro ao tentar se conectar com o banco de dados." + erro.toString());
		}
	}
	
	public boolean criaPerfil(UUID id, String nick){
		try{
		conectar();
	    s = con.createStatement();
	    ResultSet rs = s.executeQuery("SELECT * FROM perfil WHERE id = '"+id+"'");
	    if(rs.next()){
	        s.close();
	   	    rs.close();
	   	    con.close();
	    	return false;
	    }else{
	    String sql ="insert into perfil"
	    		       +" (id, nick)"
	    		       +" values"
	    		       +" ('"+id+"', '"+nick+"');";
	    s.executeUpdate(sql);
	    System.out.println("Sucesso");
	    s.close();
	    rs.close();
	    con.close();
	    return true;
	    }
		}catch(SQLException e){
			System.out.println(e.toString());
			return false;
		}
	}
		
	
	public void add(String cmd, String valor, UUID id){
		try{
		conectar();
	    s = con.createStatement();
	    String sql ="UPDATE perfil"
	    		       +" SET "+ cmd +"= '"+ valor +"'"
	    		       +"WHERE id = '"+ id+"';";
	    s.executeUpdate(sql);
	    s.close();
	    con.close();
	    System.out.println("Sucesso");
		  
		}catch(SQLException e){
			System.out.println(e.toString());
			
		}
		
	}
	
	public boolean checar(UUID id){
		try{
			conectar();
		    String sql ="SELECT * FROM perfil"
		    		    +" WHERE id = '" +id +"';";
		    p = con.prepareStatement(sql);
		    ResultSet resultset = p.executeQuery();
		    if(resultset.next()){
		     p.close();
		   	 con.close();
		   	 resultset.close();
		   	 return true;
		     
		    }else{
		    	p.close();
		   	    con.close();
		   	    resultset.close();
			    return false;
			    }
			}catch(SQLException e){
				System.out.println(e.toString());
				return false;
			}
	}
	
	public String check(String value, UUID id){
		try{
		conectar();
	    String sql ="SELECT "+ value+" FROM perfil"
	    		    +" WHERE id = '" +id +"';";
	    p = con.prepareStatement(sql);
	    ResultSet resultset = p.executeQuery();
	    if(resultset.next()){
	    	String result = resultset.getString(value);
	        p.close();
	   	    con.close();
	   	    resultset.close();
	   	 return result;
	    	 
	    	 
	     
	    }else{
	    	   p.close();
	   	    con.close();
	   	    resultset.close();
		    return null;
		    }
		}catch(SQLException e){
			System.out.println(e.toString());
			return null;
		}
			
	}
		
	
		
		
	
	public void close(){
		try{
			con.close();
			System.out.println("Conexão com a base de dados encerrada.");
		}catch(SQLException e){
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
