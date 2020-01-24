package models;

import java.util.ArrayList;

public class Fiche {

    private int id;
    private float note;
    private String nomEtudiant; // nom & prénom
    private String CNE;
    private String nomGroupeEtudiant;
    private Test test;
    private ArrayList<Reponse> reponse;

    public Fiche() {
    }

    public Fiche(int id,String CNE, float note, String nomEtudiant, String nomGroupeEtudiant, Test test) {
        this.id = id;
        this.note = note;
        this.nomEtudiant = nomEtudiant;
        this.nomGroupeEtudiant = nomGroupeEtudiant;
        this.test = test;
        this.CNE = CNE;
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

    public ArrayList<Reponse> getReponse() {
        return reponse;
    }

    public void setReponse(ArrayList<Reponse> reponse) {
        this.reponse = reponse;
    }

    public String getCNE() {
        return CNE;
    }

    public void setCNE(String CNE) {
        this.CNE = CNE;
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
