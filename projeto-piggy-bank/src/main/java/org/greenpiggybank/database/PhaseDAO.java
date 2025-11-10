package org.greenpiggybank.database;

import org.greenpiggybank.classes.gameLogic.Phase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhaseDAO {
    private ConexaoBanco conexaoBanco;

    public PhaseDAO() {
        this.conexaoBanco = new ConexaoBanco();
    }

    /**
     * Busca todas as fases cadastradas no banco, ordenadas por ID.
     * @return Uma lista de objetos Phase.
     */
    public List<Phase> findAll() {
        String sql = "SELECT * FROM Phase ORDER BY id";
        List<Phase> fases = new ArrayList<>();

        try (Connection conn = conexaoBanco.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                fases.add(buildPhase(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as fases: " + e.getMessage());
        }
        return fases;
    }

    /**
     * Busca uma fase específica pelo seu ID.
     * @param id O ID da fase a ser buscada.
     * @return O objeto Phase, ou null se não for encontrado.
     */
    public Phase findById(int id) {
        String sql = "SELECT * FROM Phase WHERE id = ?";
        Phase fase = null;

        try (Connection conn = conexaoBanco.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                fase = buildPhase(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar fase por ID: " + e.getMessage());
        }
        return fase;
    }


    private Phase buildPhase(ResultSet rs) throws SQLException {
        Phase phase = new Phase();
        phase.setId(rs.getInt("id"));
        phase.setNomeDaFase(rs.getString("nome_fase"));
        phase.setDescricao(rs.getString("descricao"));
        phase.setNarrativa(rs.getString("narrativa"));
        return phase;
    }

    public ConexaoBanco getConexaoBanco() {
        return conexaoBanco;
    }
}
