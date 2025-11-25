package engine;

import config.Config;
import model.Startup;
import model.vo.Dinheiro;
import model.vo.Humor;
import model.vo.Percentual;
import observer.GameEvent;
import observer.GameEventManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class GameEngine {

    private final int totalRodadas;
    private final int maxDecisoes;
    private final GameEventManager eventManager;

    public enum TipoDecisao {
        MARKETING,
        EQUIPE,
        PRODUTO,
        INVESTIDORES,
        CORTAR_CUSTOS
    }

    public GameEngine(GameEventManager eventManager) {
        this.eventManager = eventManager;

        // Carrega configuração do arquivo game.properties
        Config cfg = new Config();
        this.totalRodadas = cfg.getTotalRodadas();
        this.maxDecisoes = cfg.getMaxDecisoesPorRodada();
    }

    public void executarJogo(List<Startup> startups, Scanner in) {

        for (int rodada = 1; rodada <= totalRodadas; rodada++) {
            System.out.println("\n====== RODADA " + rodada + " / " + totalRodadas + " ======");

            int idx = 1;
            for (Startup s : startups) {
                s.setRodadaAtual(rodada);

                System.out.println("\n-- Jogador " + idx++ + ": " + s.getNome() + " --");
                System.out.println(s);

                List<TipoDecisao> escolhidas = escolherDecisoesNoConsole(in);

                for (TipoDecisao d : escolhidas) {
                    eventManager.notify(new GameEvent("DECISAO",
                            s.getNome() + " escolheu " + d));

                    aplicarDecisao(s, d);
                }

                fecharRodada(s);

                eventManager.notify(new GameEvent("FECHAMENTO",
                        "Fechamento da rodada para " + s.getNome()));

                System.out.println("Resumo pós-rodada: " + s);
            }
        }

        exibirRanking(startups, in);
    }

    private void aplicarDecisao(Startup s, TipoDecisao d) {

        String desc = "";

        switch (d) {
            case MARKETING -> {
                s.addCaixa(-10_000);
                s.addReputacao(+5);
                s.addBonusReceitaProx(0.03);
                desc = "Marketing: -R$10k, +5 rep, +3% receita";
            }
            case EQUIPE -> {
                s.addCaixa(-5_000);
                s.addMoral(+7);
                desc = "Equipe: -R$5k, +7 moral";
            }
            case PRODUTO -> {
                s.addCaixa(-8_000);
                s.addBonusReceitaProx(0.04);
                desc = "Produto: -R$8k, +4% receita";
            }
            case INVESTIDORES -> {
                boolean aprovado = Math.random() < 0.60;
                if (aprovado) {
                    s.addCaixa(+40_000);
                    s.addReputacao(+3);
                    desc = "Investidores APROVADO: +R$40k, +3 rep";
                } else {
                    s.addReputacao(-2);
                    desc = "Investidores REPROVADO: -2 rep";
                }
            }
            case CORTAR_CUSTOS -> {
                s.addCaixa(+8_000);
                s.addMoral(-5);
                desc = "Cortar custos: +R$8k, -5 moral";
            }
        }

        s.clamparHumor();
        s.registrar(desc);

        eventManager.notify(new GameEvent("IMPACTO", s.getNome() + ": " + desc));
    }

    private void fecharRodada(Startup s) {

        Dinheiro receita = s.receitaRodada();

        s.setCaixa(s.getCaixa().add(receita.toDouble()));

        double fatorCrescimento =
                1.0 +
                (s.getReputacao().getValor() / 100.0) * 0.01 +
                (s.getMoral().getValor() / 100.0) * 0.005;

        s.setReceitaBase(s.getReceitaBase().multiply(fatorCrescimento));

        String msg = String.format(Locale.US,
                "Fechamento: receita %s; nova receitaBase %s; caixa %s",
                receita,
                s.getReceitaBase(),
                s.getCaixa());

        s.registrar(msg);
        eventManager.notify(new GameEvent("FECHAMENTO", s.getNome() + ": " + msg));
    }

    private void exibirRanking(List<Startup> startups, Scanner in) {
        System.out.println("\n====== RELATÓRIO FINAL ======");

        startups.sort(Comparator.comparingDouble(Startup::scoreFinal).reversed());

        int pos = 1;
        for (Startup s : startups) {
            eventManager.notify(new GameEvent("RANKING",
                    s.getNome() + " terminou com score " + round2(s.scoreFinal())));

            System.out.printf(Locale.US, "%d) %s | SCORE: %.2f | %s%n",
                    pos++, s.getNome(), round2(s.scoreFinal()), s.toString());
        }

        System.out.print("\nDeseja ver histórico (s/n)? ");
        if (in.nextLine().trim().equalsIgnoreCase("s")) {
            for (Startup s : startups) {
                System.out.println("\n-- Histórico: " + s.getNome() + " --");
                s.getHistorico().forEach(System.out::println);
            }
        }

        System.out.println("\nFim. Obrigado por jogar!");
    }

    private List<TipoDecisao> escolherDecisoesNoConsole(Scanner in) {
        List<TipoDecisao> todas = Arrays.asList(TipoDecisao.values());
        List<TipoDecisao> escolhidas = new ArrayList<>();

        int restantes = maxDecisoes;

        while (restantes > 0) {
            System.out.println("\nEscolha uma decisão (" + restantes + " restante(s)):");
            for (int i = 0; i < todas.size(); i++) {
                System.out.println((i + 1) + ") " + todas.get(i));
            }
            System.out.println("0) finalizar");
            System.out.println("Q) sair do jogo");

            System.out.print("Opção: ");
            String entrada = in.nextLine().trim();

            if (entrada.isEmpty() || entrada.equals("0")) break;
            if (entrada.equalsIgnoreCase("q")) {
                System.out.println("Saindo do jogo...");
                System.exit(0);
            }

            int opt;
            try {
                opt = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
                continue;
            }

            if (opt < 1 || opt > todas.size()) {
                System.out.println("Opção inválida.");
                continue;
            }

            TipoDecisao d = todas.get(opt - 1);
            if (escolhidas.contains(d)) {
                System.out.println("Já escolhida nesta rodada.");
                continue;
            }

            escolhidas.add(d);
            restantes--;
        }

        return escolhidas;
    }

    private static double round2(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
