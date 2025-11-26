package model.vo;

public class Humor {

    private int valor; // 0..100

    public Humor(int valor) {
        this.valor = clamp(valor);
    }

    private int clamp(int v) {
        return Math.max(0, Math.min(100, v));
    }

    public int getValor() {
        return valor;
    }

    public void aumentar(int v) {
        valor = clamp(valor + v);
    }

    public void diminuir(int v) {
        valor = clamp(valor - v);
    }

    @Override
    public String toString() {
        return valor + "/100";
    }
}
