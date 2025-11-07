package org.greenpiggybank.classes.player;

public class Player {
    private int id;
    private String nome;
    private double saldo;

    public Player(){}

    public Player(String nome){
        this.nome = nome;
        this.saldo = 0.0;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNome(){
        return this.nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public double getSaldo() {
        return this.saldo;
    }

    public void setSaldo(double saldo){
        this.saldo = saldo;
    }

    @Override
    public String toString(){
        return "jogador: "+ nome +" | Saldo: R$" + String.format("%.2f", saldo);
    }

}
