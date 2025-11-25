package actions;

import model.Startup;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.mockito.MockedStatic;

class InvestidoresStrategyTest {

    @Test
    void deveAprovarInvestidores() {

        Startup s = mock(Startup.class);

        when(s.getCaixa()).thenReturn(20000.0);
        when(s.getReputacao()).thenReturn(5);

        try (MockedStatic<Math> math = mockStatic(Math.class)) {
            math.when(Math::random).thenReturn(0.10);

            InvestidoresStrategy strategy = new InvestidoresStrategy();
            strategy.aplicar(s);

            verify(s).setCaixa(20000.0 + 40000);
            verify(s).setReputacao(5 + 3);
            verify(s).registrar("Investidores: APROVADO +R$40k caixa, +3 rep");
            verify(s).clamparHumor();
        }
    }

    @Test
    void deveReprovarInvestidores() {

        Startup s = mock(Startup.class);

        when(s.getReputacao()).thenReturn(5);

        try (MockedStatic<Math> math = mockStatic(Math.class)) {
            math.when(Math::random).thenReturn(0.90);

            InvestidoresStrategy strategy = new InvestidoresStrategy();
            strategy.aplicar(s);

            verify(s).setReputacao(5 - 2);
            verify(s).registrar("Investidores: REPROVADO -2 rep");
            verify(s).clamparHumor();
        }
    }
}
