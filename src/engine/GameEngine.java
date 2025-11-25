package engine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import model.Startup;
import observer.GameEvent;
import observer.GameEventManager;

public class GameEngine {

    public static final int TOTAL_RODADAS = 2;
    public static final int MAX_DECISOES_POR_RODADA = 3;

    private final GameEventManager eventManager;

    public GameEngine(GameEventManager eventManager) {
        this.eventManager = eventManager;
    }

    public enum TipoDecisao {
        MARKETING,
        EQUIPE,
        PRODUTO,
        INVESTIDORES,
        CORTAR_CUSTOS
    }

    public void executarJogo(List<Startup> startups, Scanner in) {
        for (int rodada = 1; rodada <= TOTAL_RODADAS; rodada++) {
            System.out.println("\n====== RODADA " + rodada + " / " + TOTAL_RODADAS + " ======");

            int idx = 1;
            for (Startup s : startups) {
                s.setRodadaAtual(rodada);
                System.out.println("\n-- Jogador " + idx++ + ": " + s.getNome() + " --");
                System.out.println(s);

                List<TipoDecisao> escolhidas = escolherDecisoesNoConsole(in);

                //  Notifica cada escolha ANTES de aplicar
                for (TipoDecisao d : escolhidas) {
                    eventManager.notify(new GameEvent(
                            "DECISAO",
                            s.getNome() + " escolheu " + d
                    ));

                    aplicarDecisao(s, d);
                }

                fecharRodada(s);

                //  Notifica fechamento da rodada
                eventManager.notify(new GameEvent(
                        "FECHAMENTO",
                        "Fechamento da rodada para " + s.getNome()
                ));

                System.out.println("Resumo pos-rodada: " + s);
            }
        }

        exibirRanking(startups, in);
    }

    private void aplicarDecisao(Startup s, TipoDecisao d) {

        String desc = ""; // <-- Para gerar evento depois

        switch (d) {
            case MARKETING -> {
                s.setCaixa(s.getCaixa() - 10_000);
                s.setReputacao(s.getReputacao() + 5);
                s.addBonusPercentReceitaProx(0.03);
                desc = "Marketing: -R$10k caixa, +5 rep, +3% receita";
            }
            case EQUIPE -> {
                s.setCaixa(s.getCaixa() - 5_000);
                s.setMoral(s.getMoral() + 7);
                desc = "Equipe: -R$5k caixa, +7 moral";
            }
            case PRODUTO -> {
                s.setCaixa(s.getCaixa() - 8_000);
                s.addBonusPercentReceitaProx(0.04);
                desc = "Produto: -R$8k caixa, +4% receita";
            }
            case INVESTIDORES -> {
                boolean aprovado = Math.random() < 0.60;
                if (aprovado) {
                    s.setCaixa(s.getCaixa() + 40_000);
                    s.setReputacao(s.getReputacao() + 3);
                    desc = "Investidores APROVADO: +R$40k caixa, +3 rep";
                } else {
                    s.setReputacao(s.getReputacao() - 2);
                    desc = "Investidores REPROVADO: -2 rep";
                }
            }
            case CORTAR_CUSTOS -> {
                s.setCaixa(s.getCaixa() + 8_000);
                s.setMoral(s.getMoral() - 5);
                desc = "Cortar custos: +R$8k caixa, -5 moral";
            }
        }

        s.clamparHumor();
        s.registrar(desc);

        //  Notifica os efeitos
        eventManager.notify(new GameEvent("IMPACTO", s.getNome() + ": " + desc));
    }

    private void fecharRodada(Startup s) {
        double receita = s.receitaRodada();
        s.setCaixa(s.getCaixa() + receita);

        double fatorCrescimento = 1.0
                + (s.getReputacao() / 100.0) * 0.01
                + (s.getMoral() / 100.0) * 0.005;
        s.setReceitaBase(s.getReceitaBase() * fatorCrescimento);

        String msg = String.format(Locale.US,
                "Fechamento: receita R$%.2f; nova receitaBase R$%.2f; caixa R$%.2f",
                round2(receita), round2(s.getReceitaBase()), round2(s.getCaixa()));

        s.registrar(msg);

        //  Evento de fechamento
        eventManager.notify(new GameEvent("FECHAMENTO", s.getNome() + ": " + msg));
    }

    private void exibirRanking(List<Startup> startups, Scanner in) {
        System.out.println("\n====== RELATORIO FINAL ======");

        startups.sort(Comparator.comparingDouble(Startup::scoreFinal).reversed());

        int pos = 1;
        for (Startup s : startups) {
            eventManager.notify(new GameEvent("RANKING",
                    s.getNome() + " terminou com score " + round2(s.scoreFinal())));

            System.out.printf(Locale.US, "%d) %s | SCORE: %.2f | %s%n",
                    pos++, s.getNome(), round2(s.scoreFinal()), s.toString());
        }

        System.out.print("\nDeseja ver historico (s/n)? ");
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
        int restantes = MAX_DECISOES_POR_RODADA;

        while (restantes > 0) {
            System.out.println("\nEscolha uma decisao (" + restantes + " restante(s)):");
            for (int i = 0; i < todas.size(); i++) {
                System.out.println((i + 1) + ") " + todas.get(i));
            }
            System.out.println("0) finalizar");
            System.out.println("Q) sair do jogo");

            System.out.print("Opção: ");
            String entrada = in.nextLine().trim();

            if (entrada.isEmpty() || entrada.equals("0")) {
                break;
            }
            if (entrada.equalsIgnoreCase("q")) {
                System.out.println("Saindo do jogo...");
                System.exit(0);
            }

            int opt;
            try {
                opt = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida.");
                continue;
            }

            if (opt < 1 || opt > todas.size()) {
                System.out.println("Opcao inválida.");
                continue;
            }

            TipoDecisao d = todas.get(opt - 1);
            if (escolhidas.contains(d)) {
                System.out.println("Ja escolhida nesta rodada.");
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
