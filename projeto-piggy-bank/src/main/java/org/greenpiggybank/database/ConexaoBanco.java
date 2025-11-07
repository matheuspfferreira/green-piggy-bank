package org.greenpiggybank.database;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;


public class ConexaoBanco {

    private static final String local = "jdbc:mysql://localhost:3306/cofrinho_verde";
    private static final String usuario = "root";
    private static final String senha = "";

    public Connection getConexao(){
        Connection conexao = null;

        try{

            conexao = DriverManager.getConnection(local, usuario, senha);
        } catch (SQLException e){
            System.out.println("Erro para conectar com o banco de dados!");
        }

        return conexao;
    }

}
