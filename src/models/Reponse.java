package models;

public class Reponse {
    private int id;
    private int idFiche;
    private int idQuestion;
    private String value;

    public Reponse() {
    }

    public Reponse(int id, int idFiche, int idQuestion, String value) {
        this.id = id;
        this.idFiche = idFiche;
        this.idQuestion = idQuestion;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFiche() {
        return idFiche;
    }

    public void setIdFiche(int idFiche) {
        this.idFiche = idFiche;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", idFiche=" + idFiche +
                ", idQuestion=" + idQuestion +
                ", value='" + value + '\'' +
                '}';
    }
}
