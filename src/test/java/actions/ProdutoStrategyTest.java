package actions;

import model.Startup;
import model.vo.Dinheiro;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ProdutoStrategyTest {

    @Test
    void deveAplicarProduto() {

        Startup s = mock(Startup.class);

        Dinheiro caixa = new Dinheiro(30000);

        when(s.getCaixa()).thenReturn(caixa);

        ProdutoStrategy strategy = new ProdutoStrategy();
        strategy.aplicar(s);

        assert caixa.toDouble() == 22000;

        verify(s).addBonusPercentReceitaProx(0.04);
        verify(s).registrar("Produto: -R$8k caixa, +4% receita na próxima rodada");
        verify(s).clamparHumor();
    }
}
