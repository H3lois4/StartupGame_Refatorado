package model.vo;

public class Percentual {

    private double valor; // Ex: 0.05 = 5%

    public Percentual(double valor) {
        this.valor = valor;
    }

    public double toDouble() {
        return valor;
    }

    public void somar(double p) {
        this.valor += p;
    }

    public void zerar() {
        this.valor = 0;
    }

    @Override
    public String toString() {
        return (valor * 100) + "%";
    }
}
