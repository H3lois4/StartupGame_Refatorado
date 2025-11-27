package actions;

import model.Startup;

public class MarketingStrategy implements DecisaoStrategy {

    @Override
    public void aplicar(Startup s) {
        s.diminuirCaixa(10_000);
        s.addReputacao(5);
        s.addBonusPercentReceitaProx(0.03);
        s.registrar("Marketing: -R$10k caixa, +5 rep, +3% receita na próxima rodada");
        s.clamparHumor();
    }

    @Override
    public String getNome() {
        return "Marketing";
    }
}
