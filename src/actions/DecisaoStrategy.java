package actions;

import model.Startup;

/**
 * Interface base do padrão Strategy.
 * Cada decisão do jogo implementa esta interface.
 */
public interface DecisaoStrategy {
    void aplicar(Startup startup);
    String getNome();
}
