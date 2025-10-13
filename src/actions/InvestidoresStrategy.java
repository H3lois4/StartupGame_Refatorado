package actions;

import model.Startup;

public class InvestidoresStrategy implements DecisaoStrategy {

    @Override
    public void aplicar(Startup s) {
        boolean aprovado = Math.random() < 0.60;
        if (aprovado) {
            s.setCaixa(s.getCaixa() + 40_000);
            s.setReputacao(s.getReputacao() + 3);
            s.registrar("Investidores: APROVADO +R$40k caixa, +3 rep");
        } else {
            s.setReputacao(s.getReputacao() - 2);
            s.registrar("Investidores: REPROVADO -2 rep");
        }
        s.clamparHumor();
    }

    @Override
    public String getNome() {
        return "Investidores";
    }
}
