package model.vo;

/**
 * Value Object para representar porcentagens (0.0 a 1.0).
 */
public class Percentual {

    private final double valor;

    public Percentual(double valor) {
        if (valor < 0 || valor > 1) {
            throw new IllegalArgumentException("Percentual deve estar entre 0.0 e 1.0");
        }
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }

    public double aplicar(double base) {
        return base * valor;
    }

    public Percentual somar(Percentual outro) {
        return new Percentual(Math.min(this.valor + outro.valor, 1.0));
    }

    @Override
    public String toString() {
        return String.format("%.2f%%", valor * 100);
    }
}
