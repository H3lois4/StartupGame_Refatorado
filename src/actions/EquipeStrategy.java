package actions;

import model.Startup;

public class EquipeStrategy implements DecisaoStrategy {

    @Override
    public void aplicar(Startup s) {
        s.setCaixa(s.getCaixa() - 5_000);
        s.setMoral(s.getMoral() + 7);
        s.registrar("Equipe: -R$5k caixa, +7 moral");
        s.clamparHumor();
    }

    @Override
    public String getNome() {
        return "Equipe";
    }
}
