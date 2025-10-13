package model.vo;

/**
 * Value Object para representar estados de humor (0 a 100).
 * Pode ser usado tanto para moral quanto para reputação.
 */
public class Humor {

    private final int valor;

    public Humor(int valor) {
        if (valor < 0 || valor > 100) {
            throw new IllegalArgumentException("Humor deve estar entre 0 e 100");
        }
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public Humor aumentar(int delta) {
        return new Humor(Math.min(100, valor + delta));
    }

    public Humor reduzir(int delta) {
        return new Humor(Math.max(0, valor - delta));
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }
}
