package config;

import java.io.InputStream;
import java.util.Properties;

public class Config {

    private int totalRodadas;
    private int maxDecisoesPorRodada;

    public Config() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("game.properties")) {

            Properties props = new Properties();
            props.load(is);

            this.totalRodadas = Integer.parseInt(props.getProperty("total.rodadas"));
            this.maxDecisoesPorRodada = Integer.parseInt(props.getProperty("max.decisoes.por.rodada"));

        } catch (Exception e) {
            System.out.println("Erro ao carregar configurações: " + e.getMessage());
            this.totalRodadas = 5; // fallback
            this.maxDecisoesPorRodada = 3;
        }
    }

    public int getTotalRodadas() {
        return totalRodadas;
    }

    public int getMaxDecisoesPorRodada() {
        return maxDecisoesPorRodada;
    }
}
