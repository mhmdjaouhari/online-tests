package models;

import java.io.Serializable;

public class Etudiant implements Serializable {
    private String CNE;
    private String nom;
    private String prenom;
    private String username;
    private String password;
    private int idGroupe;
    private String nomGroupe;

    public Etudiant(String CNE, String nom, String prenom, String username, String password, int idGroupe) {
        this.CNE = CNE;
        this.idGroupe = idGroupe;
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
                ", login='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
