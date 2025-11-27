package persistence;

import javax.sql.DataSource;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;

public class DataSourceProvider {

    private static DataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            try {
                // tenta instanciar JdbcDataSource via reflexão para evitar dependência direta em tempo de compilação
                Class<?> clazz = Class.forName("org.h2.jdbcx.JdbcDataSource");
                Object ds = clazz.getDeclaredConstructor().newInstance();

                Method mSetURL = clazz.getMethod("setURL", String.class);
                Method mSetUser = clazz.getMethod("setUser", String.class);
                Method mSetPassword = clazz.getMethod("setPassword", String.class);

                mSetURL.invoke(ds, "jdbc:h2:./database/startup_game;AUTO_SERVER=TRUE");
                mSetUser.invoke(ds, "sa");
                mSetPassword.invoke(ds, "");

                dataSource = (DataSource) ds;
                initDatabase();

            } catch (ClassNotFoundException e) {
                System.out.println("H2 não encontrado no classpath: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro ao inicializar DataSource via reflexão: " + e.getMessage());
            }
        }
        return dataSource;
    }

    /** Carrega e executa schema.sql automaticamente */
    private static void initDatabase() {
        if (dataSource == null) return;

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

                stmt.execute("CREATE TABLE IF NOT EXISTS startups (" +
                        " id IDENTITY PRIMARY KEY," +
                        " nome VARCHAR(255)," +
                        " caixa DOUBLE," +
                        " receita_base DOUBLE," +
                        " reputacao INT," +
                        " moral INT," +
                        " score DOUBLE" +
                        " );");

                stmt.execute("CREATE TABLE IF NOT EXISTS historico (" +
                        " id IDENTITY PRIMARY KEY," +
                        " startup_id BIGINT," +
                        " mensagem VARCHAR(500)" +
                        " );");

        } catch (Exception e) {
            System.out.println("Erro ao inicializar banco H2: " + e.getMessage());
        }
    }
}
