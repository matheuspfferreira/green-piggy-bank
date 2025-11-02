package org.greenpiggybank.database;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;


public class ConexaoBanco {

    public Connection getConexao(){
        String local = "jdbc:h2:./banco_teste";
        String usuario = "teste";
        String senha = "";

        Connection conexao = null;

        try{
            conexao = DriverManager.getConnection(local, usuario, senha);
        } catch (SQLException e){
            System.out.println("Erro para conectar com o banco de dados!");
        }

        return conexao;
    }

}
