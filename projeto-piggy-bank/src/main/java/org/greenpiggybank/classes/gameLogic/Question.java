package org.greenpiggybank.classes.gameLogic;
import java.util.List;

public class Question {
    private int id;
    private String textoPergunta;
    private double recompensa;
    private int idFase;

    //Lista das alternativas
    private List<Alternative> alternativas;

    public Question(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextoPergunta() {
        return textoPergunta;
    }

    public void setTextoPergunta(String textoPergunta) {
        this.textoPergunta = textoPergunta;
    }

    public double getRecompensa() {
        return recompensa;
    }

    public void setRecompensa(double recompensa) {
        this.recompensa = recompensa;
    }

    public int getIdFase() {
        return idFase;
    }

    public void setIdFase(int idFase) {
        this.idFase = idFase;
    }

    public List<Alternative> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternative> alternativas) {
        this.alternativas = alternativas;
    }

}
