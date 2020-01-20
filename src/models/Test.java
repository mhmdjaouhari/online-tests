package models;

import java.io.Serializable;

public class Test implements Serializable {
    private int id_test;
    private String matricule;
    private String titre;

    public Test(int id_test, String matricule, String titre) {
        this.id_test = id_test;
        this.matricule = matricule;
        this.titre = titre;
    }

    public Test() {
    }

    public int getId_test() {
        return id_test;
    }

    public void setId_test(int id_test) {
        this.id_test = id_test;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id_test=" + id_test +
                ", matricule='" + matricule + '\'' +
                ", titre='" + titre + '\'' +
                '}';
    }
}
