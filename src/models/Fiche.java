package models;

import java.io.Serializable;
import java.util.ArrayList;

public class Fiche implements Serializable {

    private int id;
    private float note;
    private String CNE;
    private String nomEtudiant; // nom & prénom
    private String nomGroupeEtudiant;
    private Test test;
    private ArrayList<Reponse> reponses;

    public Fiche() {
    }

    public Fiche(int id, float note, String nomEtudiant, String nomGroupeEtudiant, Test test) {
        this.id = id;
        this.note = note;
        this.nomEtudiant = nomEtudiant;
        this.nomGroupeEtudiant = nomGroupeEtudiant;
        this.test = test;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public String getCNE() {
        return CNE;
    }

    public void setCNE(String CNE) {
        this.CNE = CNE;
    }

    public String getNomEtudiant() {
        return nomEtudiant;
    }

    public void setNomEtudiant(String nomEtudiant) {
        this.nomEtudiant = nomEtudiant;
    }

    public String getNomGroupeEtudiant() {
        return nomGroupeEtudiant;
    }

    public void setNomGroupeEtudiant(String nomGroupeEtudiant) {
        this.nomGroupeEtudiant = nomGroupeEtudiant;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public ArrayList<Reponse> getReponses() {
        return reponses;
    }

    public void setReponses(ArrayList<Reponse> reponses) {
        this.reponses = reponses;
    }

    @Override
    public String toString() {
        return "Fiche{" +
                "id=" + id +
                ", note=" + note +
                ", nomEtudiant='" + nomEtudiant + '\'' +
                ", nomGroupeEtudiant='" + nomGroupeEtudiant + '\'' +
                ", test=" + test +
                '}';
    }
}
