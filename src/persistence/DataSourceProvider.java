package persistence;

import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.Statement;

public class DataSourceProvider {

    private static DataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL("jdbc:h2:./database/startup_game;AUTO_SERVER=TRUE");
            ds.setUser("sa");
            ds.setPassword("");

            dataSource = ds;

            initDatabase();
        }
        return dataSource;
    }

    /** Carrega e executa schema.sql automaticamente */
    private static void initDatabase() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS startups (
                    id IDENTITY PRIMARY KEY,
                    nome VARCHAR(255),
                    caixa DOUBLE,
                    receita_base DOUBLE,
                    reputacao INT,
                    moral INT,
                    score DOUBLE
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS historico (
                    id IDENTITY PRIMARY KEY,
                    startup_id BIGINT,
                    mensagem VARCHAR(500)
                );
            """);

        } catch (Exception e) {
            System.out.println("Erro ao inicializar banco H2: " + e.getMessage());
        }
    }
}
