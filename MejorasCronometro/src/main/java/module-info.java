module com.example.mejorascronometro {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.mejorascronometro to javafx.fxml;
    exports com.example.mejorascronometro;
}