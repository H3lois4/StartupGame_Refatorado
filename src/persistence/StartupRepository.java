package persistence;

import model.Startup;
import java.sql.*;

/**
 * Exemplo simples de repositório para salvar startups no banco.
 */
public class StartupRepository {

    public void salvar(Startup s) {
        String sql = "INSERT INTO startups (nome, caixa, receitaBase, reputacao, moral) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DataSourceProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getNome());
            ps.setDouble(2, s.getCaixa());
            ps.setDouble(3, s.getReceitaBase());
            ps.setInt(4, s.getReputacao());
            ps.setInt(5, s.getMoral());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar startup: " + e.getMessage());
        }
    }
}
