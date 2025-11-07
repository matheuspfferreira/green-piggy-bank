package org.greenpiggybank.database;

import org.greenpiggybank.database.ConexaoBanco;
import org.greenpiggybank.classes.player.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDAO {
    private ConexaoBanco conexaoBanco;

    public PlayerDAO() {
        this.conexaoBanco = new ConexaoBanco();
    }

    // Metodo para buscar um jogador pelo nome ou criá-lo se não existir
    public Player getOrCreatePlayer(String nome) {
        Player player = findByNome(nome);
        if (player == null) {
            player = new Player(nome);
            save(player); // Salva o novo jogador no banco
            player = findByNome(nome); // Busca novamente para pegar o ID
        }
        return player;
    }

    // Metodo para salvar um novo jogador
    private void save(Player player) {
        String sql = "INSERT INTO Player (nome, saldo) VALUES (?, ?)";

        try (Connection conn = conexaoBanco.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, player.getNome());
            pstmt.setDouble(2, player.getSaldo());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar jogador: " + e.getMessage());
        }
    }

    // Metodo para buscar um jogador pelo nome
    private Player findByNome(String nome) {
        String sql = "SELECT * FROM Player WHERE nome = ?";
        Player player = null;

        try (Connection conn = conexaoBanco.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                player = new Player();
                player.setId(rs.getInt("id"));
                player.setNome(rs.getString("nome"));
                player.setSaldo(rs.getDouble("saldo"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar jogador: " + e.getMessage());
        }
        return player;
    }

    // Metodo para atualizar o saldo do jogador
    public void updateSaldo(Player player) {
        String sql = "UPDATE Player SET saldo = ? WHERE id = ?";

        try (Connection conn = conexaoBanco.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, player.getSaldo());
            pstmt.setInt(2, player.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar saldo: " + e.getMessage());
        }
    }
}
