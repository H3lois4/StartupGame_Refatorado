package engine;

import model.Startup;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Responsável por executar a lógica principal do jogo (rodadas, decisões, ranking).
 */
public class GameEngine {

    public static final int TOTAL_RODADAS = 2;
    public static final int MAX_DECISOES_POR_RODADA = 3;

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

                List<TipoDecisao> escolhidas = escolherDecisoesNoConsole(in, s);
                for (TipoDecisao d : escolhidas) {
                    aplicarDecisao(s, d);
                }

                fecharRodada(s);
                System.out.println("Resumo pós-rodada: " + s);
            }
        }

        exibirRanking(startups, in);
    }

    private void aplicarDecisao(Startup s, TipoDecisao d) {
        switch (d) {
            case MARKETING -> {
                s.setCaixa(s.getCaixa() - 10_000);
                s.setReputacao(s.getReputacao() + 5);
                s.addBonusPercentReceitaProx(0.03);
                s.registrar("Marketing: -R$10k caixa, +5 rep, +3% receita na próxima rodada");
            }
            case EQUIPE -> {
                s.setCaixa(s.getCaixa() - 5_000);
                s.setMoral(s.getMoral() + 7);
                s.registrar("Equipe: -R$5k caixa, +7 moral");
            }
            case PRODUTO -> {
                s.setCaixa(s.getCaixa() - 8_000);
                s.addBonusPercentReceitaProx(0.04);
                s.registrar("Produto: -R$8k caixa, +4% receita na próxima rodada");
            }
            case INVESTIDORES -> {
                boolean aprovado = Math.random() < 0.60;
                if (aprovado) {
                    s.setCaixa(s.getCaixa() + 40_000);
                    s.setReputacao(s.getReputacao() + 3);
                    s.registrar("Investidores: APROVADO +R$40k caixa, +3 rep");
                } else {
                    s.setReputacao(s.getReputacao() - 2);
                    s.registrar("Investidores: REPROVADO -2 rep");
                }
            }
            case CORTAR_CUSTOS -> {
                s.setCaixa(s.getCaixa() + 8_000);
                s.setMoral(s.getMoral() - 5);
                s.registrar("Cortar custos: +R$8k caixa, -5 moral");
            }
        }
        s.clamparHumor();
    }

    private void fecharRodada(Startup s) {
        double receita = s.receitaRodada();
        s.setCaixa(s.getCaixa() + receita);

        double fatorCrescimento = 1.0
                + (s.getReputacao() / 100.0) * 0.01
                + (s.getMoral() / 100.0) * 0.005;
        s.setReceitaBase(s.getReceitaBase() * fatorCrescimento);

        s.registrar(String.format(Locale.US,
                "Fechamento: receita R$%.2f; nova receitaBase R$%.2f; caixa R$%.2f",
                round2(receita), round2(s.getReceitaBase()), round2(s.getCaixa())));
    }

    private void exibirRanking(List<Startup> startups, Scanner in) {
        System.out.println("\n====== RELATÓRIO FINAL ======");
        startups.sort(Comparator.comparingDouble(Startup::scoreFinal).reversed());
        int pos = 1;
        for (Startup s : startups) {
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

    private List<TipoDecisao> escolherDecisoesNoConsole(Scanner in, Startup s) {
        List<TipoDecisao> todas = Arrays.asList(TipoDecisao.values());
        List<TipoDecisao> escolhidas = new ArrayList<>();
        int restantes = MAX_DECISOES_POR_RODADA;

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

    private double round2(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
