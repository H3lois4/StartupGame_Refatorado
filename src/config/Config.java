package config;

import java.io.InputStream;
import java.util.Properties;

public class Config {

    private int totalRodadas;
    private int maxDecisoesPorRodada;

    public Config() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("game.properties")) {

            if (is == null) {
                throw new ConfigException("Arquivo game.properties não encontrado no classpath!");
            }

            Properties props = new Properties();
            props.load(is);

            // Lê propriedades
            String rodadasStr = props.getProperty("total.rodadas");
            String decisoesStr = props.getProperty("max.decisoes.por.rodada");

            if (rodadasStr == null || decisoesStr == null) {
                throw new ConfigException("Parâmetros obrigatórios ausentes no game.properties");
            }

            this.totalRodadas = Integer.parseInt(rodadasStr);
            this.maxDecisoesPorRodada = Integer.parseInt(decisoesStr);

            // validações
            if (totalRodadas <= 0) {
                throw new ConfigException("total.rodadas deve ser maior que zero.");
            }
            if (maxDecisoesPorRodada <= 0) {
                throw new ConfigException("max.decisoes.por.rodada deve ser maior que zero.");
            }

        } catch (NumberFormatException e) {
            throw new ConfigException("Erro ao converter parâmetros numéricos: " + e.getMessage());

        } catch (ConfigException e) {
            // repassa exceções de configuração personalizadas
            throw e;

        } catch (Exception e) {
            // fallback geral
            throw new ConfigException("Erro inesperado ao carregar configurações: " + e.getMessage());
        }
    }

    public int getTotalRodadas() {
        return totalRodadas;
    }

    public int getMaxDecisoesPorRodada() {
        return maxDecisoesPorRodada;
    }
}
