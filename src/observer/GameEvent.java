package observer;

public class GameEvent {
    private final String tipo;
    private final String descricao;

    public GameEvent(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }
}
