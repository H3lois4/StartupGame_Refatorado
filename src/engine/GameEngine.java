package engine;

import config.Config;
import model.Startup;
import observer.GameEvent;
import observer.GameEventManager;
import persistence.StartupRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class GameEngine {

    private final int TOTAL_RODADAS;
    private final int MAX_DECISOES_POR_RODADA;
    private static final GameEventManager OBSERVER = new GameEventManager();

    public static GameEventManager getEventManager() { return OBSERVER; }
    private final StartupRepository repository = new StartupRepository();

    public GameEngine(Config config) {
        this.TOTAL_RODADAS = config.getTotalRodadas();
        this.MAX_DECISOES_POR_RODADA = config.getMaxDecisoesPorRodada();
    }

    public enum TipoDecisao {
        MARKETING,
        EQUIPE,
        PRODUTO,
        INVESTIDORES,
        CORTAR_CUSTOS
    }

    public void executarJogo(List<Startup> startups, Scanner in) {
        try {
            for (int rodada = 1; rodada <= TOTAL_RODADAS; rodada++) {

                System.out.println("\n====== RODADA " + rodada + " / " + TOTAL_RODADAS + " ======");

                int idx = 1;
                for (Startup s : startups) {
                    s.setRodadaAtual(rodada);
                    System.out.println("\n-- Jogador " + idx++ + ": " + s.getNome() + " --");
                    System.out.println(s);

                    List<TipoDecisao> escolhidas = escolherDecisoesNoConsole(in);

                    for (TipoDecisao d : escolhidas) {
                        OBSERVER.notify(new GameEvent("DECISAO",
                            s.getNome() + " escolheu " + d));
                        aplicarDecisao(s, d);
                    }

                    fecharRodada(s);

                            OBSERVER.notify(new GameEvent("FECHAMENTO",
                                "Rodada concluída para " + s.getNome()));

                    System.out.println("Resumo pós-rodada: " + s);
                }
            }

            exibirRanking(startups, in);

        } catch (Exception e) {
            System.err.println("ERRO FATAL DURANTE O JOGO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void aplicarDecisao(Startup s, TipoDecisao d) {

        String desc = "";

        try {
            switch (d) {
                case MARKETING: {
                    s.diminuirCaixa(10_000);
                    s.getReputacao().aumentar(5);
                    s.addBonusPercentReceitaProx(0.03);
                    desc = "Marketing: -R$10k caixa, +5 rep, +3% receita";
                    break;
                }
                case EQUIPE: {
                    s.diminuirCaixa(5_000);
                    s.getMoral().aumentar(7);
                    desc = "Equipe: -R$5k caixa, +7 moral";
                    break;
                }
                case PRODUTO: {
                    s.diminuirCaixa(8_000);
                    s.addBonusPercentReceitaProx(0.04);
                    desc = "Produto: -R$8k caixa, +4% receita";
                    break;
                }
                case INVESTIDORES: {
                    boolean aprovado = Math.random() < 0.60;
                    if (aprovado) {
                        s.aumentarCaixa(40_000);
                        s.getReputacao().aumentar(3);
                        desc = "Investidores APROVADO: +R$40k caixa, +3 rep";
                    } else {
                        s.getReputacao().diminuir(2);
                        desc = "Investidores REPROVADO: -2 rep";
                    }
                    break;
                }
                case CORTAR_CUSTOS: {
                    s.aumentarCaixa(8_000);
                    s.getMoral().diminuir(5);
                    desc = "Cortar custos: +R$8k caixa, -5 moral";
                    break;
                }
                default: {
                    break;
                }
            }

            s.clamparHumor();
            s.registrar(desc);

            OBSERVER.notify(new GameEvent("IMPACTO", s.getNome() + ": " + desc));

        } catch (Exception e) {
            System.err.println("Erro ao aplicar decisão " + d + ": " + e.getMessage());
        }
    }

    private void fecharRodada(Startup s) {

        try {
            double receita = s.receitaRodada();
            s.aumentarCaixa(receita);

            double fatorCrescimento = 1.0
                    + (s.getReputacao().getValor() / 100.0) * 0.01
                    + (s.getMoral().getValor() / 100.0) * 0.005;

            s.aumentarReceitaBasePercentual(fatorCrescimento - 1);

            String msg = String.format(Locale.US,
                    "Fechamento: receita R$%.2f; nova receitaBase R$%.2f; caixa R$%.2f",
                    round2(receita), s.getReceitaBase().toDouble(), s.getCaixa().toDouble());

            s.registrar(msg);

            OBSERVER.notify(new GameEvent("FECHAMENTO", s.getNome() + ": " + msg));

        } catch (Exception e) {
            System.err.println("Erro ao fechar rodada: " + e.getMessage());
        }
    }

    private void exibirRanking(List<Startup> startups, Scanner in) {

        try {
            System.out.println("\n====== RELATÓRIO FINAL ======");

            startups.sort(Comparator.comparingDouble(Startup::scoreFinal).reversed());

            int pos = 1;

            for (Startup s : startups) {

                OBSERVER.notify(new GameEvent("RANKING",
                    s.getNome() + " score: " + round2(s.scoreFinal())));

                System.out.printf(Locale.US, "%d) %s | SCORE: %.2f | %s%n",
                        pos++, s.getNome(), round2(s.scoreFinal()), s);
            }

            //  Persistência real no H2
            System.out.println("\nSalvando dados no banco H2...");

            for (Startup s : startups) {
                long id = repository.salvarStartup(s);  // retorna ID real
                repository.salvarHistorico(id, s);      // salva histórico vinculado ao ID
            }

            System.out.println("Dados gravados com sucesso!\n");

            System.out.print("Deseja ver histórico (s/n)? ");
            if (in.nextLine().trim().equalsIgnoreCase("s")) {
                for (Startup s : startups) {
                    System.out.println("\n-- Histórico: " + s.getNome() + " --");
                    s.getHistorico().forEach(System.out::println);
                }
            }

            System.out.println("\nFim. Obrigado por jogar!");

        } catch (Exception e) {
            System.err.println("Erro ao exibir ranking: " + e.getMessage());
        }
    }

    private static double round2(double v) {
        return BigDecimal.valueOf(v)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private List<TipoDecisao> escolherDecisoesNoConsole(Scanner in) {

        List<TipoDecisao> todas = Arrays.asList(TipoDecisao.values());
        List<TipoDecisao> escolhidas = new ArrayList<>();
        int restantes = MAX_DECISOES_POR_RODADA;

        while (restantes > 0) {
            System.out.println("\nEscolha uma decisão (" + restantes + " restante(s)):");
            for (int i = 0; i < todas.size(); i++) {
                System.out.println((i + 1) + ") " + todas.get(i));
            }
            System.out.println("0) finalizar");
            System.out.println("Q) sair");

            System.out.print("Opção: ");
            String entrada = in.nextLine().trim();

            if (entrada.isEmpty() || entrada.equals("0")) break;
            if (entrada.equalsIgnoreCase("q")) System.exit(0);

            try {
                int opt = Integer.parseInt(entrada);

                if (opt < 1 || opt > todas.size()) {
                    System.out.println("Opção inválida.");
                    continue;
                }

                TipoDecisao d = todas.get(opt - 1);
                if (escolhidas.contains(d)) {
                    System.out.println("Decisão já escolhida!");
                    continue;
                }

                escolhidas.add(d);
                restantes--;

            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }

        return escolhidas;
    }
}
