package models;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class Etudiant implements Serializable {
    private SimpleStringProperty CNE;
    private SimpleStringProperty nom;
    private SimpleStringProperty prenom;
    private SimpleStringProperty username;
    private SimpleStringProperty password;
    private int idGroupe;
    private SimpleStringProperty nomGroupe;

    public Etudiant(String CNE, String nom, String prenom, String username, String password, int idGroupe) {
        this.CNE = new SimpleStringProperty(CNE);
        this.idGroupe = idGroupe;
        this.nom = new SimpleStringProperty(nom);;
        this.prenom = new SimpleStringProperty(prenom);;
        this.username = new SimpleStringProperty(username);;
        this.password = new SimpleStringProperty(password);;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = new SimpleStringProperty(nomGroupe);
    }

    public Etudiant(){};

    public String getCNE() {
        return CNE.get();
    }

    public void setCNE(String CNE) {
        this.CNE = new SimpleStringProperty(CNE);
    }

    public int getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(int idGroupe) {
        this.idGroupe = idGroupe;
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom = new SimpleStringProperty(nom);;
    }

    public String getPrenom() {
        return prenom.get();
    }

    public void setPrenom(String prenom) {
        this.prenom =new SimpleStringProperty(prenom);;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username = new SimpleStringProperty(username);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password = new SimpleStringProperty(password);;
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
