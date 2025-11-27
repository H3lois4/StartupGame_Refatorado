package actions;

import model.Startup;

public class InvestidoresStrategy implements DecisaoStrategy {

    private static final double CHANCE_APROVACAO = 0.60;

    @Override
    public void aplicar(Startup s) {
        boolean aprovado = Math.random() < CHANCE_APROVACAO;

        if (aprovado) {
            s.aumentarCaixa(40_000);
            s.addReputacao(3);
            s.registrar("Investidores: APROVADO +R$40k caixa, +3 rep");
        } else {
            s.addReputacao(-2);
            s.registrar("Investidores: REPROVADO -2 rep");
        }

        s.clamparHumor();
    }

    @Override
    public String getNome() {
        return "Investidores";
    }
}
