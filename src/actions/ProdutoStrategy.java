package actions;

import model.Startup;

public class ProdutoStrategy implements DecisaoStrategy {

    @Override
    public void aplicar(Startup s) {
        s.diminuirCaixa(8_000);
        s.addBonusPercentReceitaProx(0.04);
        s.registrar("Produto: -R$8k caixa, +4% receita na próxima rodada");
        s.clamparHumor();
    }

    @Override
    public String getNome() {
        return "Produto";
    }
}
