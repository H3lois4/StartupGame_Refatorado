package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Lê configurações do arquivo game.properties.
 */
public class Config {

    private final Properties props = new Properties();

    public Config() {
        try (FileInputStream fis = new FileInputStream("resources/game.properties")) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("Erro ao ler game.properties: " + e.getMessage());
        }
    }

    public int getTotalRodadas() {
        return Integer.parseInt(props.getProperty("total.rodadas", "2"));
    }

    public int getMaxDecisoesPorRodada() {
        return Integer.parseInt(props.getProperty("max.decisoes.por.rodada", "3"));
    }
}
