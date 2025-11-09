package org.greenpiggybank.database;

import org.greenpiggybank.database.ConexaoBanco;
import org.greenpiggybank.classes.gameLogic.Alternative;
import org.greenpiggybank.classes.gameLogic.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    private ConexaoBanco conexaoBanco;
    private AlternativeDAO alternativeDAO; // Dependência do AlternativeDAO

    public QuestionDAO() {
        this.conexaoBanco = new ConexaoBanco();
        this.alternativeDAO = new AlternativeDAO(); // Instancia o DAO de alternativas
    }

    // Metodo para buscar todas as perguntas de uma fase
    public List<Question> findByFaseId(int idFase) {
        String sql = "SELECT * FROM Question WHERE id_fase = ?";
        List<Question> perguntas = new ArrayList<>();

        try (Connection conn = conexaoBanco.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFase);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // 1. Cria o objeto Question básico
                Question question = new Question();
                question.setId(rs.getInt("id"));
                question.setTextoPergunta(rs.getString("texto_pergunta"));
                question.setRecompensa(rs.getDouble("recompensa"));
                question.setIdFase(rs.getInt("id_fase"));

                // 2. Busca as alternativas associadas a esta pergunta
                List<Alternative> alternativas = alternativeDAO.findByQuestion(question.getId());
                question.setAlternativas(alternativas);

                // 3. Adiciona a pergunta completa (com alternativas) à lista
                perguntas.add(question);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar perguntas: " + e.getMessage());
        }
        return perguntas;
    }

    public ConexaoBanco getConexaoBanco() {
        return conexaoBanco;
    }
}
