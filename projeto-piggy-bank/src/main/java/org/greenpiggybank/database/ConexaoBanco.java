package org.greenpiggybank.database;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;


public class ConexaoBanco {

    private static final String local = "jdbc:h2:./cofrinho_verde";
    private static final String usuario = "root";
    private static final String senha = "";

    public Connection getConexao(){
        try {
            return DriverManager.getConnection(local, usuario, senha);

        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível conectar com o banco de dados", e);
        }

    }

}
