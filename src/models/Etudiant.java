package models;

import java.io.Serializable;

public class Etudiant implements Serializable {
    private String CNE;
    private int idGroupe;
    private String nom;
    private String prenom;
    private String login;
    private String password;

    public Etudiant(String CNE, int idGroupe, String nom, String prenom, String login, String password) {
        this.CNE = CNE;
        this.idGroupe = idGroupe;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.password = password;
    }

    public Etudiant(){};

    public String getCNE() {
        return CNE;
    }

    public void setCNE(String CNE) {
        this.CNE = CNE;
    }

    public int getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(int idGroupe) {
        this.idGroupe = idGroupe;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Etudiant{" +
                "CNE='" + CNE + '\'' +
                ", id_groupe=" + idGroupe +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
