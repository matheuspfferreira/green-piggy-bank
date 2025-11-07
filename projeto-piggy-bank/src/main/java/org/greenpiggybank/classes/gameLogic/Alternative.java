package org.greenpiggybank.classes.gameLogic;

public class Alternative {

    private int id;
    private String textoAlternativa;
    private boolean correta;
    private int idPergunta; // Chave estrangeira

    // Construtor, Getters e Setters

    public Alternative() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextoAlternativa() {
        return textoAlternativa;
    }

    public void setTextoAlternativa(String textoAlternativa) {
        this.textoAlternativa = textoAlternativa;
    }

    public boolean isCorreta() {
        return correta;
    }

    public void setCorreta(boolean correta) {
        this.correta = correta;
    }

    public int getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(int idPergunta) {
        this.idPergunta = idPergunta;
    }

    @Override
    public String toString() {
        return textoAlternativa; // Simples para exibir no terminal
    }
}