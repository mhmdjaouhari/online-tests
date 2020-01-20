module online.tests {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires com.jfoenix;

    opens GUI.admins;
    opens GUI.students;
    opens GUI.profs;
}