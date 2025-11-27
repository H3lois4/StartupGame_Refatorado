package persistence;

import model.Startup;
import model.vo.Dinheiro;
import model.vo.Humor;

import java.sql.*;
import java.io.FileWriter;
import java.io.IOException;

public class StartupRepository {

    private final DataSourceProvider provider = new DataSourceProvider();

    public long salvarStartup(Startup s) {
        try {
            String sql = "INSERT INTO startups (nome, caixa, receita_base, reputacao, moral, score)"
                    + " VALUES (?, ?, ?, ?, ?, ?)";

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
            }
        } catch (Exception e) {
            System.err.println("H2 não disponível, salvando histórico em arquivo: " + e.getMessage());
            // Fallback: retorna -1 para indicar que não foi salvo no banco
            return -1;
        }
        return -1;
    }

    public void salvarHistorico(long startupId, Startup s) {
        if (startupId == -1) {
            // Fallback: salva histórico em arquivo texto
            try (FileWriter fw = new FileWriter("historico.txt", true)) {
                fw.write("--- Histórico: " + s.getNome() + " ---\n");
                for (String linha : s.getHistorico()) {
                    fw.write(linha + "\n");
                }
                fw.write("\n");
            } catch (IOException e) {
                System.err.println("Erro ao salvar histórico em arquivo: " + e.getMessage());
            }
            return;
        }

        String sql = "INSERT INTO historico (startup_id, mensagem) VALUES (?, ?)";

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
