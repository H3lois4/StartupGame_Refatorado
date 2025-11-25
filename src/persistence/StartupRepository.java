package persistence;

import model.Startup;
import model.vo.Dinheiro;
import model.vo.Humor;

import java.sql.*;

public class StartupRepository {

    private final DataSourceProvider provider = new DataSourceProvider();

    public long salvarStartup(Startup s) {

        String sql = """
            INSERT INTO startups (nome, caixa, receita_base, reputacao, moral, score)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = provider.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getNome());
            ps.setDouble(2, s.getCaixa().toDouble());
            ps.setDouble(3, s.getReceitaBase().toDouble());
            ps.setInt(4, s.getReputacao().getValor());
            ps.setInt(5, s.getMoral().getValor());
            ps.setDouble(6, s.scoreFinal());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar startup: " + e.getMessage());
        }

        return -1;
    }

    public void salvarHistorico(long startupId, Startup s) {

        if (startupId == -1) {
            System.err.println("ID inválido ao tentar salvar histórico.");
            return;
        }

        String sql = """
            INSERT INTO historico (startup_id, mensagem)
            VALUES (?, ?)
        """;

        try (Connection conn = provider.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (String linha : s.getHistorico()) {
                ps.setLong(1, startupId);
                ps.setString(2, linha);
                ps.addBatch();
            }

            ps.executeBatch();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar histórico da startup: " + e.getMessage());
        }
    }

    public long obterIdPorNome(String nome) {
        String sql = "SELECT id FROM startups WHERE nome = ? ORDER BY id DESC LIMIT 1";

        try (Connection conn = provider.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nome);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong("id");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao consultar ID da startup: " + e.getMessage());
        }

        return -1;
    }
}
