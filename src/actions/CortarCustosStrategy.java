package actions;

import model.Startup;

public class CortarCustosStrategy implements DecisaoStrategy {

    @Override
    public void aplicar(Startup s) {
        s.aumentarCaixa(8_000);
        s.addMoral(-5);
        s.registrar("Cortar custos: +R$8k caixa, -5 moral");
        s.clamparHumor();
    }

    @Override
    public String getNome() {
        return "Cortar Custos";
    }
}
