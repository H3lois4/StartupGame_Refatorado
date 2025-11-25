package actions;

import model.Startup;
import model.vo.Dinheiro;
import model.vo.Humor;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class EquipeStrategyTest {

    @Test
    void deveAplicarEquipe() {

        Startup s = mock(Startup.class);

        Dinheiro caixa = new Dinheiro(20000);
        Humor moral = new Humor(40);

        when(s.getCaixa()).thenReturn(caixa);
        when(s.getMoral()).thenReturn(moral);

        EquipeStrategy strategy = new EquipeStrategy();
        strategy.aplicar(s);

        assert caixa.toDouble() == 15000;
        assert moral.getValor() == 47;

        verify(s).registrar("Equipe: -R$5k caixa, +7 moral");
        verify(s).clamparHumor();
    }
}
