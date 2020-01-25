module online.tests {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires com.jfoenix;

    opens GUI.admin;
    opens GUI.etudiant;
    opens GUI.professeur;
    opens GUI.professeur.dashboard;
}