package actions;

import java.util.*;

public class DecisaoFactory {

    private static final Map<Integer, DecisaoStrategy> DECISOES = new LinkedHashMap<>();

    static {
        DECISOES.put(1, new MarketingStrategy());
        DECISOES.put(2, new EquipeStrategy());
        DECISOES.put(3, new ProdutoStrategy());
        DECISOES.put(4, new InvestidoresStrategy());
        DECISOES.put(5, new CortarCustosStrategy());
    }

    public static void exibirOpcoes() {
        for (Map.Entry<Integer, DecisaoStrategy> entry : DECISOES.entrySet()) {
            System.out.println(entry.getKey() + ") " + entry.getValue().getNome());
        }
    }

    public static DecisaoStrategy getPorOpcao(int opcao) {
        return DECISOES.get(opcao);
    }

    public static Collection<DecisaoStrategy> getTodas() {
        return DECISOES.values();
    }
}
