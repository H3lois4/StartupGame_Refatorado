package observer;

/**
 * Tipos de eventos possíveis no jogo.
 */
public enum GameEventType {
    ACAO_EXECUTADA,      // Quando uma decisão ou ação é tomada
    STARTUP_ALTERADA,    // Quando status de uma startup muda
    RODADA_FINALIZADA,   // Quando uma rodada termina
    JOGO_FINALIZADO      // Quando o jogo termina
}
