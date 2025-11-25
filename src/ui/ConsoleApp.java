package ui;

import engine.GameEngine;
import model.Startup;
import observer.GameEventManager;
import observer.ConsoleEventListener;
import java.util.*;

public class ConsoleApp {

    private final Scanner in = new Scanner(System.in);
    private final GameEngine engine;
    private final GameEventManager eventManager;

    // ======= CONSTRUTOR CORRETO =======
    public ConsoleApp(GameEventManager eventManager) {
        this.eventManager = eventManager;
        this.engine = new GameEngine(eventManager);

        // registra listener padrão
        this.eventManager.addListener(new ConsoleEventListener());
    }

    public void iniciar() {
        System.out.println("=== Startup Game (Console) ===");
        List<Startup> startups = criarStartupsIniciais();
        engine.executarJogo(startups, in);
    }

    private List<Startup> criarStartupsIniciais() {
        List<Startup> list = new ArrayList<>();
        list.add(new Startup("AlphaTech", 100000, 10000, 50, 60));
        list.add(new Startup("BioNova", 80000, 7000, 55, 55));
        list.add(new Startup("CloudZ", 110000, 12000, 45, 65));
        return list;
    }
}
