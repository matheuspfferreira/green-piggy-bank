package org.greenpiggybank.database;
import org.greenpiggybank.classes.gameLogic.Alternative;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlternativeDAO {

    private ConexaoBanco conexaoBanco;

    public AlternativeDAO(){
        this.conexaoBanco = new ConexaoBanco();
    }

    public List<Alternative> findByQuestion(int idPergunta){
        String sql = "SELECT * FROM Alternative WHERE id_pergunta = ?";
        List<Alternative> alternatives = new ArrayList<>();

        try(Connection conn = conexaoBanco.getConexao();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, idPergunta);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Alternative alt = new Alternative();
                alt.setId(rs.getInt("id"));
                alt.setTextoAlternativa(rs.getString("texto_alternativa"));
                alt.setCorreta(rs.getBoolean("correta"));
                alt.setIdPergunta(rs.getInt("id_pergunta"));
                alternatives.add(alt);
            }


        } catch (SQLException e){
            System.err.println("Erro ao buscar alternativas: " + e.getMessage());
        }

        return alternatives;
    }

}
