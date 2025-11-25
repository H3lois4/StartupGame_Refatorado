package actions;

import model.Startup;
import model.vo.Dinheiro;
import model.vo.Humor;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CortarCustosStrategyTest {

    @Test
    void deveAplicarCorteDeCustos() {

        Startup s = mock(Startup.class);

        Dinheiro caixa = new Dinheiro(10000);
        Humor moral = new Humor(50);

        when(s.getCaixa()).thenReturn(caixa);
        when(s.getMoral()).thenReturn(moral);

        CortarCustosStrategy strategy = new CortarCustosStrategy();
        strategy.aplicar(s);

        assert caixa.toDouble() == 18000;
        assert moral.getValor() == 45;

        verify(s).registrar("Cortar custos: +R$8k caixa, -5 moral");
        verify(s).clamparHumor();
    }
}
