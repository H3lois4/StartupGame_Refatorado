package actions;

import model.Startup;
import model.vo.Dinheiro;
import model.vo.Humor;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class MarketingStrategyTest {

    @Test
    void deveAplicarMarketing() {

        Startup s = mock(Startup.class);

        Dinheiro caixa = new Dinheiro(50000);
        Humor reputacao = new Humor(10);

        when(s.getCaixa()).thenReturn(caixa);
        when(s.getReputacao()).thenReturn(reputacao);

        MarketingStrategy strategy = new MarketingStrategy();
        strategy.aplicar(s);

        assert caixa.toDouble() == 40000;
        assert reputacao.getValor() == 15;

        verify(s).addBonusPercentReceitaProx(0.03);
        verify(s).registrar("Marketing: -R$10k caixa, +5 rep, +3% receita na próxima rodada");
        verify(s).clamparHumor();
    }
}
