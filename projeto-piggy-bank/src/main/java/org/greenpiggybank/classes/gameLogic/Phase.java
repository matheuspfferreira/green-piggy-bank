package org.greenpiggybank.classes.gameLogic;

public class Phase {
    private int id;
    private String nome_fase;
    private String descricao;

    public Phase(){}

    public Phase(String nome_fase, String descricao){
        this.nome_fase = nome_fase;
        this.descricao = descricao;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNomeDaFase(){
        return this.nome_fase;
    }

    public void setNomeDaFase(String nome){
        this.nome_fase = nome_fase;
    }

    public String getDescricao(){
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString(){
        return "Fase: "+ nome_fase +"\nDescrição: " + descricao;
    }

}
