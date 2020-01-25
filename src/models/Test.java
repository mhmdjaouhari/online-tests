package models;

import javafx.scene.control.Label;

import java.io.Serializable;
import java.util.ArrayList;

public class Test implements Serializable {
    private int id;
    private String titre;
    private boolean locked;
    private boolean penalite;
    private int duration;
    private String matriculeProf;
    private String nomProf; // nom & prénom
    private ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<Groupe> groupes = new ArrayList<>();

    public Test(int id, String titre, boolean locked, int duration, String matriculeProf, String nomProf) {
        this.id = id;
        this.titre = titre;
        this.locked = locked;
        this.duration = duration;
        this.matriculeProf = matriculeProf;
        this.nomProf = nomProf;
    }
    public Test(int id, String titre, boolean locked, int duration, String matriculeProf, String nomProf, ArrayList<Question> qst, ArrayList<Groupe> grp) {
        this.id = id;
        this.titre = titre;
        this.locked = locked;
        this.duration = duration;
        this.matriculeProf = matriculeProf;
        this.nomProf = nomProf;
        this.questions=qst;
        this.groupes=grp;
    }

    public Test() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isPenalite() {
        return penalite;
    }

    public void setPenalite(boolean penalite) {
        this.penalite = penalite;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMatriculeProf() {
        return matriculeProf;
    }

    public void setMatriculeProf(String matriculeProf) {
        this.matriculeProf = matriculeProf;
    }

    public String getNomProf() {
        return nomProf;
    }

    public void setNomProf(String nomProf) {
        this.nomProf = nomProf;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public ArrayList<Groupe> getGroupes() {
        return groupes;
    }

    public void setGroupes(ArrayList<Groupe> groupes) {
        this.groupes = groupes;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", locked=" + locked +
                ", penalite=" + penalite +
                ", duration=" + duration +
                ", matriculeProf='" + matriculeProf + '\'' +
                ", nomProf='" + nomProf + '\'' +
                '}';
    }

    public String getDurationString() {
        int hours = (int) Math.floor((float) this.getDuration() / 60);
        String durationString = "" + hours + "h" + (this.getDuration() - hours * 60);
        return durationString;
    }

    public String getLockedString() {
        return isLocked() ? "Verouillé" : "Non-verouillé";
    }

    public String getPenaliteString() {
        return isPenalite() ? "Avec pénalités" : "Sans pénalités";
    }

    public String getDetails() {
        return getDurationString() + "  ·  " + getLockedString() + "  ·  " + getPenaliteString();
    }

    public String getGroupesString() {
        ArrayList<String> nomsGroupes = new ArrayList<>();
        for (Groupe g : groupes) {
            nomsGroupes.add(g.getNom());
        }
        return String.join(", ", nomsGroupes);
    }
}
