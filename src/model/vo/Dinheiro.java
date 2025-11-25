package model.vo;

public class Dinheiro {

    private double valor;

    public Dinheiro(double valor) {
        this.valor = Math.max(0, valor);
    }

    public double toDouble() {
        return valor;
    }

    public void aumentar(double v) {
        valor += v;
    }

    public void diminuir(double v) {
        valor = Math.max(0, valor - v);
    }

    @Override
    public String toString() {
        return String.format("R$ %.2f", valor);
    }
}
