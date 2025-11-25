package persistence;

import model.Startup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StartupRepository {

    private final DataSourceProvider provider = new DataSourceProvider();

    public void salvarStartup(Startup s) {
        String sql = """
            INSERT INTO startups (nome, caixa, receita_base, reputacao, moral, score)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = provider.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getNome());
            ps.setDouble(2, s.getCaixa().toDouble());
            ps.setDouble(3, s.getReceitaBase().toDouble());
            ps.setInt(4, s.getReputacao().getValor());
            ps.setInt(5, s.getMoral().getValor());
            ps.setDouble(6, s.scoreFinal());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao salvar startup: " + e.getMessage());
        }
    }

    public void salvarHistorico(Startup s) {
        String sql = "INSERT INTO historico (startup_id, mensagem) VALUES (?, ?)";

        try (Connection conn = provider.getDataSource().getConnection()) {

            long startupId = obterIdPorNome(conn, s.getNome());

            for (String linha : s.getHistorico()) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setLong(1, startupId);
                    ps.setString(2, linha);
                    ps.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }

    private long obterIdPorNome(Connection conn, String nome) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM startups WHERE nome = ? ORDER BY id DESC LIMIT 1"
        )) {
            ps.setString(1, nome);
            var rs = ps.executeQuery();
            if (rs.next()) return rs.getLong("id");
        }
        return -1;
    }
}
