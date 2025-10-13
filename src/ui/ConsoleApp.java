package ui;

import engine.GameEngine;
import model.Startup;
import java.util.*;

/**
 * Camada de interface (console): cria startups e inicia o jogo.
 */
public class ConsoleApp {

    private final Scanner in = new Scanner(System.in);
    private final GameEngine engine = new GameEngine();

    public void iniciar() {
        System.out.println("=== Startup Game (Console) ===");
        List<Startup> startups = criarStartupsIniciais();
        engine.executarJogo(startups, in);
    }

    private List<Startup> criarStartupsIniciais() {
        List<Startup> list = new ArrayList<>();
        list.add(new Startup("AlphaTech", 100_000, 10_000, 50, 60));
        list.add(new Startup("BioNova", 80_000, 7_000, 55, 55));
        list.add(new Startup("CloudZ", 110_000, 12_000, 45, 65));
        return list;
    }
}
