package org.greenpiggybank.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public static void createPlayersTable(Connection conn) {
        String query = "CREATE TABLE IF NOT EXISTS Player (" +
                "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                "nome VARCHAR(50) NOT NULL, " +
                "saldo DECIMAL(9, 2) NOT NULL)";

        try (Statement stmt = conn.createStatement();) {
            stmt.execute(query);
            System.out.println("Oba! A tabela de jogadores foi criada com sucesso");
        } catch (SQLException e) {
            System.out.println("Ops! Não foi possível criar a tabela de jogadores");
        }
    }

    public static void createPhasesTable(Connection conn) {
        String query = "CREATE TABLE IF NOT EXISTS Phase (" +
                "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                "nome_fase VARCHAR(50) NOT NULL, " +
                "descricao VARCHAR(200) NOT NULL)";

        try (Statement stmt = conn.createStatement();) {
            stmt.execute(query);
            System.out.println("Oba! A tabela de fases foi criada com sucesso");
        } catch (SQLException e) {
            System.out.println("Ops! Não foi possível criar a tabela de fases");
        }
    }

    public static void createQuestionsTable(Connection conn) {
        String query = "CREATE TABLE IF NOT EXISTS Question (" +
                "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                "texto_pergunta VARCHAR(200) NOT NULL, " +
                "recompensa DECIMAL(9, 2) NOT NULL," +
                "id_fase INTEGER NOT NULL," +
                "FOREIGN KEY (id_fase) REFERENCES Phase(id))";

        try (Statement stmt = conn.createStatement();) {
            stmt.execute(query);
            System.out.println("Oba! A tabela de perguntas foi criada com sucesso");
        } catch (SQLException e) {
            System.out.println("Ops! Não foi possível criar a tabela de perguntas");
        }
    }

    public static void createAlternativesTable(Connection conn) {
        String query = "CREATE TABLE IF NOT EXISTS Alternative (" +
                "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                "texto_pergunta VARCHAR(100) NOT NULL, " +
                "correta BOOLEAN NOT NULL," +
                "pergunta_id INTEGER NOT NULL," +
                "FOREIGN KEY (pergunta_id) REFERENCES Question(id))";

        try (Statement stmt = conn.createStatement();) {
            stmt.execute(query);
            System.out.println("Oba! A tabela de alternativas foi criada com sucesso");
        } catch (SQLException e) {
            System.out.println("Ops! Não foi possível criar a tabela de alternativas");
        }
    }

}