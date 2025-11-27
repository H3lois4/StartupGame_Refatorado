package model;

import model.vo.Dinheiro;
import model.vo.Humor;
import model.vo.Percentual;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Startup {

    private String nome;
    private Dinheiro caixa;
    private Dinheiro receitaBase;
    private Humor reputacao;
    private Humor moral;

    private Percentual bonusReceitaProx = new Percentual(0.0);
    private int rodadaAtual = 1;

    private final List<String> historico = new ArrayList<>();

    public Startup(String nome, double caixaInicial, double receitaInicial, int reputacaoInicial, int moralInicial) {
        this.nome = nome;
        this.caixa = new Dinheiro(caixaInicial);
        this.receitaBase = new Dinheiro(receitaInicial);
        this.reputacao = new Humor(reputacaoInicial);
        this.moral = new Humor(moralInicial);
    }

    public void registrar(String linha) {
        historico.add("R" + rodadaAtual + " - " + linha);
    }

    /** Receita da rodada considerando bônus */
    public double receitaRodada() {
        double valor = receitaBase.toDouble() * (1.0 + bonusReceitaProx.toDouble());
        bonusReceitaProx = new Percentual(0.0);
        return valor;
    }

    /** Garantir reputação e moral em 0..100 (delegado ao VO Humor) */
    public void clamparHumor() {
        reputacao = reputacao.clamp();
        moral = moral.clamp();
    }

    public double scoreFinal() {
        return reputacao.getValor() * 0.35
                + moral.getValor() * 0.25
                + (caixa.toDouble() / 1000.0) * 0.15
                + (receitaBase.toDouble() / 1000.0) * 0.25;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Dinheiro getCaixa() { return caixa; }
    public void setCaixa(Dinheiro caixa) { this.caixa = caixa; }
    public void addCaixa(double delta) { this.caixa.aumentar(delta); }
    public void aumentarCaixa(double v) { this.caixa.aumentar(v); }
    public void diminuirCaixa(double v) { this.caixa.diminuir(v); }

    public Dinheiro getReceitaBase() { return receitaBase; }
    public void setReceitaBase(Dinheiro receitaBase) { this.receitaBase = receitaBase; }
    public void addReceitaBase(double delta) { this.receitaBase.aumentar(delta); }
    public void aumentarReceitaBasePercentual(double percentual) {
        double delta = this.receitaBase.toDouble() * percentual;
        this.receitaBase.aumentar(delta);
    }

    public Humor getReputacao() { return reputacao; }
    public void setReputacao(Humor reputacao) { this.reputacao = reputacao; }
    public void addReputacao(int delta) { this.reputacao.aumentar(delta); }

    public Humor getMoral() { return moral; }
    public void setMoral(Humor moral) { this.moral = moral; }
    public void addMoral(int delta) { this.moral.aumentar(delta); }

    public Percentual getBonusReceitaProx() { return bonusReceitaProx; }
    public void addBonusReceitaProx(double delta) {
        this.bonusReceitaProx.somar(delta);
    }

    // alias usado por GameEngine/tests
    public void addBonusPercentReceitaProx(double delta) { addBonusReceitaProx(delta); }

    public int getRodadaAtual() { return rodadaAtual; }
    public void setRodadaAtual(int rodadaAtual) { this.rodadaAtual = rodadaAtual; }

    public List<String> getHistorico() { return historico; }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "%s | Caixa: %s | ReceitaBase: %s | Rep: %d | Moral: %d",
                nome,
                caixa,
                receitaBase,
                reputacao.getValor(),
                moral.getValor());
    }
}
