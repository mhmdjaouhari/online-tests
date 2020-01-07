module online.tests {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens GUI.admins;
    opens GUI.students;
    opens GUI.profs;
}