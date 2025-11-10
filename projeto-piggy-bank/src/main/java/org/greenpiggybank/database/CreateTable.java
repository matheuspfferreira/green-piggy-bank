package org.greenpiggybank.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                "descricao VARCHAR(200) NOT NULL, " +
                "narrativa VARCHAR(500) NOT NULL)";

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
                "texto_alternativa VARCHAR(100) NOT NULL, " +
                "correta BOOLEAN NOT NULL," +
                "id_pergunta INTEGER NOT NULL," +
                "FOREIGN KEY (id_pergunta) REFERENCES Question(id))";

        try (Statement stmt = conn.createStatement();) {
            stmt.execute(query);
            System.out.println("Oba! A tabela de alternativas foi criada com sucesso");
        } catch (SQLException e) {
            System.out.println("Ops! Não foi possível criar a tabela de alternativas");
        }
    }

    public static void populateInitialData(Connection conn) {
        if (conn == null) {
            System.out.println("Ops! Conexão inválida para popular os dados iniciais.");
            return;
        }

        if (hasPhases(conn)) {
            return;
        }

        try {
            conn.setAutoCommit(false);

            int faseFundamentos = inserirFase(conn,
                    "Fundamentos do Cofrinho",
                    "Entenda por que guardar dinheiro faz diferença no seu dia a dia.",
                    "É o início do mês e você acaba de receber uma mesada especial. Antes de gastar, você decide entender melhor como o cofrinho pode te ajudar a chegar aos seus sonhos. Cada pergunta desta fase representa uma etapa da sua primeira conversa com o Cofrinho Verde.");

            int fasePlanejamento = inserirFase(conn,
                    "Planejamento Inteligente",
                    "Aprenda a organizar suas metas e usar o dinheiro com mais consciência.",
                    "Depois de encher o cofrinho por algumas semanas, você percebe que precisa de um plano para alcançar objetivos maiores. Nesta fase, você vai conversar com mentores que mostram como transformar vontade em metas reais e sustentáveis.");

            int pergunta1Fase1 = inserirPergunta(conn, faseFundamentos,
                    "Qual é o primeiro passo para começar a poupar dinheiro?",
                    10.0);
            inserirAlternativa(conn, pergunta1Fase1, "Definir um objetivo claro para o dinheiro guardado.", true);
            inserirAlternativa(conn, pergunta1Fase1, "Esperar sobrar dinheiro no final do mês.", false);
            inserirAlternativa(conn, pergunta1Fase1, "Comprar itens em promoção sempre que possível.", false);
            inserirAlternativa(conn, pergunta1Fase1, "Pedir dinheiro emprestado para familiares.", false);

            int pergunta2Fase1 = inserirPergunta(conn, faseFundamentos,
                    "Por que é importante acompanhar seus gastos?",
                    10.0);
            inserirAlternativa(conn, pergunta2Fase1, "Para identificar onde é possível economizar.", true);
            inserirAlternativa(conn, pergunta2Fase1, "Para gastar mais em lazer.", false);
            inserirAlternativa(conn, pergunta2Fase1, "Para saber quanto posso pedir emprestado.", false);
            inserirAlternativa(conn, pergunta2Fase1, "Para comprar tudo no cartão de crédito.", false);

            int pergunta1Fase2 = inserirPergunta(conn, fasePlanejamento,
                    "O que é uma meta financeira SMART?",
                    15.0);
            inserirAlternativa(conn, pergunta1Fase2, "Uma meta específica, mensurável, alcançável, relevante e com prazo.", true);
            inserirAlternativa(conn, pergunta1Fase2, "Uma meta que depende de sorte e de promoções.", false);
            inserirAlternativa(conn, pergunta1Fase2, "Uma meta que só empresas usam.", false);
            inserirAlternativa(conn, pergunta1Fase2, "Uma meta que foca apenas em reduzir gastos.", false);

            int pergunta2Fase2 = inserirPergunta(conn, fasePlanejamento,
                    "Qual é a vantagem de separar despesas fixas e variáveis?",
                    15.0);
            inserirAlternativa(conn, pergunta2Fase2, "Facilita visualizar o que pode ser ajustado no orçamento.", true);
            inserirAlternativa(conn, pergunta2Fase2, "Ajuda a gastar mais com compras impulsivas.", false);
            inserirAlternativa(conn, pergunta2Fase2, "Permite usar todo o limite do cartão de crédito.", false);
            inserirAlternativa(conn, pergunta2Fase2, "Serve apenas para quem tem renda alta.", false);

            conn.commit();
            System.out.println("Oba! Dados iniciais inseridos com sucesso");
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Ops! Erro ao desfazer inserções iniciais: " + rollbackEx.getMessage());
            }
            System.out.println("Ops! Não foi possível inserir os dados iniciais: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Ops! Não foi possível restaurar o estado da conexão: " + e.getMessage());
            }
        }
    }

    private static boolean hasPhases(Connection conn) {
        String sql = "SELECT COUNT(*) AS total FROM Phase";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Ops! Não foi possível verificar as fases existentes: " + e.getMessage());
        }
        return true;
    }

    private static int inserirFase(Connection conn, String nome, String descricao, String narrativa) throws SQLException {
        String sql = "INSERT INTO Phase (nome_fase, descricao, narrativa) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, descricao);
            pstmt.setString(3, narrativa);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Não foi possível obter o ID da fase recém-inserida.");
    }

    private static int inserirPergunta(Connection conn, int idFase, String texto, double recompensa) throws SQLException {
        String sql = "INSERT INTO Question (texto_pergunta, recompensa, id_fase) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, texto);
            pstmt.setDouble(2, recompensa);
            pstmt.setInt(3, idFase);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Não foi possível obter o ID da pergunta recém-inserida.");
    }

    private static void inserirAlternativa(Connection conn, int idPergunta, String texto, boolean correta) throws SQLException {
        String sql = "INSERT INTO Alternative (texto_alternativa, correta, id_pergunta) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, texto);
            pstmt.setBoolean(2, correta);
            pstmt.setInt(3, idPergunta);
            pstmt.executeUpdate();
        }
    }
}