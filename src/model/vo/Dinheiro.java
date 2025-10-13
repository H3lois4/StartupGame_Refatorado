package model.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/**
 * Value Object para representar valores monetários.
 */
public class Dinheiro {

    private final BigDecimal valor;

    public Dinheiro(double valor) {
        this.valor = BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }

    public double getValor() {
        return valor.doubleValue();
    }

    public Dinheiro somar(Dinheiro outro) {
        return new Dinheiro(this.valor.add(outro.valor).doubleValue());
    }

    public Dinheiro subtrair(Dinheiro outro) {
        return new Dinheiro(this.valor.subtract(outro.valor).doubleValue());
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "R$%.2f", valor);
    }
}
