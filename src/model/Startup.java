package model;

import java.util.*;

/**
 * Representa uma startup com seu estado e histórico.
 */
public class Startup {
    private String nome;
    private double caixa;
    private double receitaBase;
    private int reputacao;
    private int moral;
    private int rodadaAtual = 1;

    private double bonusPercentReceitaProx = 0.0;
    private final List<String> historico = new ArrayList<>();

    public Startup(String nome, double caixa, double receitaBase, int reputacao, int moral) {
        this.nome = nome;
        this.caixa = caixa;
        this.receitaBase = receitaBase;
        this.reputacao = reputacao;
        this.moral = moral;
    }

    public void registrar(String linha) {
        historico.add("R" + rodadaAtual + " - " + linha);
    }

    public double receitaRodada() {
        double receita = receitaBase * (1.0 + bonusPercentReceitaProx);
        bonusPercentReceitaProx = 0.0;
        return receita;
    }

    public void clamparHumor() {
        reputacao = Math.max(0, Math.min(100, reputacao));
        moral = Math.max(0, Math.min(100, moral));
    }

    public double scoreFinal() {
        return reputacao * 0.35
                + moral * 0.25
                + (caixa / 1000.0) * 0.15
                + (receitaBase / 1000.0) * 0.25;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getCaixa() { return caixa; }
    public void setCaixa(double caixa) { this.caixa = caixa; }

    public double getReceitaBase() { return receitaBase; }
    public void setReceitaBase(double receitaBase) { this.receitaBase = receitaBase; }

    public int getReputacao() { return reputacao; }
    public void setReputacao(int reputacao) { this.reputacao = reputacao; }

    public int getMoral() { return moral; }
    public void setMoral(int moral) { this.moral = moral; }

    public int getRodadaAtual() { return rodadaAtual; }
    public void setRodadaAtual(int rodadaAtual) { this.rodadaAtual = rodadaAtual; }

    public double getBonusPercentReceitaProx() { return bonusPercentReceitaProx; }
    public void addBonusPercentReceitaProx(double delta) { this.bonusPercentReceitaProx += delta; }

    public List<String> getHistorico() { return historico; }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "%s | Caixa: R$%.2f | ReceitaBase: R$%.2f | Rep: %d | Moral: %d",
                nome, caixa, receitaBase, reputacao, moral);
    }
}
