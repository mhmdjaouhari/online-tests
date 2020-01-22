package models;

import server.DAOs.GroupeDAO;
import server.DAOs.ProfesseurDAO;
import server.DAOs.QuestionDAO;
import server.DAOs.TestDAO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Vector;

public class Professeur implements Serializable {
    private String matricule;
    private String username;
    private String password;
    private String nom;
    private String prenom;

    public Professeur(String matricule, String username, String password, String nom, String prenom) {
        this.matricule = matricule;
        this.username = username;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
    }

    public Professeur() { }
/*
    //creer un test
    public void submitTest()
    {
        TestDAO testDao = new TestDAO();
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez entrer le titre du test");
        String titre = sc.nextLine();
        System.out.println("Veuillez entrer le durée du test");
        int duree = sc.nextInt();
        ArrayList<Question> qsts = new ArrayList<Question>();
        int i=1;
        Question q;
        QuestionDAO qstDao=new QuestionDAO();
        GroupeDAO grpDao = new GroupeDAO();
        ProfesseurDAO prfDao = new ProfesseurDAO();
        String x,y;
        do {
            System.out.println("Entrer la question "+i+" ou -1 pour terminer");
            x=sc.nextLine();
            if(x.compareToIgnoreCase("-1")!=0)
            {
                System.out.println("Entrer les reponses correctes sout forme 1-2");
                y=sc.nextLine();
                q=new Question((Integer)(qstDao.maxId().getData())+i,x,y,(Integer)(testDao.maxId().getData())+1);
                qsts.add(q);
                i++;
            }
        }while(x.compareToIgnoreCase("-1")!=0);
        ArrayList<Groupe> grps = new ArrayList<>();
        int id_grp;
        Groupe g;
        do {
            System.out.println("Entrer l'id du groupe à celui vous que vous voulez affecter le test(1 à la fois) ou -1 pour terminer");
            id_grp=sc.nextInt();
            if(id_grp!=-1)
            {
                if(grpDao.search(id_grp).getStatus()==1)
                {
                    System.out.println(grpDao.search(id_grp).getMessage());
                }
                else
                {
                    g = new Groupe(id_grp, "aaa");
                    grps.add(g);
                }
            }
        }while(id_grp!=-1);
        Test t = new Test((Integer)(testDao.maxId().getData())+1,titre,false,duree, this.getMatricule(),((Professeur)(prfDao.search(this.getMatricule()).getData())).getNom(),qsts,grps);
        testDao.add(t);
    }
    public void submitTest(Test test)
    {
        TestDAO testDAO = new TestDAO();
        testDAO.add(test);
    }

    //test created by prof
    public ArrayList<Test> getTests()
    {
        TestDAO testdao = new TestDAO();
        return (ArrayList<Test>)(testdao.getAll(this.matricule).getData());
    }

    //trouver test by id
    public Test getTest(int id)
    {
        TestDAO testDao = new TestDAO();
        return (Test)(testDao.search(id).getData());
    }

*/
    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
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

    @Override
    public String toString() {
        return "Professeur{" +
                "matricule='" + matricule + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                '}';
    }
}
