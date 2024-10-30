package com.example.mejorascronometro;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ContadorTiempo extends Application {
    private TextField inputField;
    private ProgressBar progressBar;
    private Label tiempoLabel;
    private Button iniciarButton;
    private Button cancelarButton;
    private ComboBox<String> temaComboBox;
    private ComboBox<String> tiemposGuardadosBox;
    private int tiempoTotal;
    private int tiempoActual;
    private boolean contando;
    private Thread hiloContador;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Contador de Tiempo");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label instruccionLabel = new Label("Introduce el tiempo en segundos:");
        inputField = new TextField();
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(200);
        tiempoLabel = new Label("Tiempo: 00:00:00");

        iniciarButton = new Button("Iniciar");
        cancelarButton = new Button("Cancelar");
        cancelarButton.setDisable(true);

        // Tema ComboBox
        temaComboBox = new ComboBox<>();
        temaComboBox.getItems().addAll("Claro", "Oscuro");
        temaComboBox.setValue("Claro");
        temaComboBox.setOnAction(e -> cambiarTema(root));

        // ComboBox para cargar tiempos predefinidos
        tiemposGuardadosBox = new ComboBox<>();
        cargarTiemposGuardados();
        tiemposGuardadosBox.setOnAction(e -> cargarTiempoSeleccionado());

        root.getChildren().addAll(instruccionLabel, inputField, progressBar, tiempoLabel, iniciarButton, cancelarButton, temaComboBox, tiemposGuardadosBox);

        iniciarButton.setOnAction(e -> iniciarContador());
        cancelarButton.setOnAction(e -> cancelarContador());

        Scene scene = new Scene(root, 300, 350);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void iniciarContador() {
        try {
            tiempoTotal = Integer.parseInt(inputField.getText());
            if (tiempoTotal <= 0) {
                throw new NumberFormatException();
            }
            guardarTiempoPredefinido(inputField.getText());

            tiempoActual = 0;
            contando = true;
            progressBar.setProgress(0);
            iniciarButton.setDisable(true);
            cancelarButton.setDisable(false);
            inputField.setDisable(true);

            hiloContador = new Thread(() -> {
                try {
                    while (contando && tiempoActual < tiempoTotal) {
                        Thread.sleep(1000);
                        tiempoActual++;
                        Platform.runLater(this::actualizarUI);
                    }
                    if (tiempoActual >= tiempoTotal) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Tiempo completado");
                            alert.setHeaderText(null);
                            alert.setContentText("¡El tiempo ha finalizado!");
                            alert.showAndWait();
                            reiniciarUI();
                        });
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            });
            hiloContador.setDaemon(true);
            hiloContador.start();
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, introduce un número válido mayor que cero.");
            alert.showAndWait();
        }
    }

    private void cancelarContador() {
        contando = false;
        if (hiloContador != null && hiloContador.isAlive()) {
            hiloContador.interrupt();
        }
        reiniciarUI();
    }

    private void actualizarUI() {
        double progreso = (double) tiempoActual / tiempoTotal;
        progressBar.setProgress(progreso);
        tiempoLabel.setText("Tiempo: " + formatearTiempo(tiempoActual));
    }

    private String formatearTiempo(int segundos) {
        int horas = segundos / 3600;
        int minutos = (segundos % 3600) / 60;
        int segs = segundos % 60;
        return String.format("%02d:%02d:%02d", horas, minutos, segs);
    }

    private void reiniciarUI() {
        iniciarButton.setDisable(false);
        cancelarButton.setDisable(true);
        inputField.setDisable(false);
        progressBar.setProgress(0);
        tiempoLabel.setText("Tiempo: 00:00:00");
    }

    private void cambiarTema(VBox root) {
        if (temaComboBox.getValue().equals("Oscuro")) {
            root.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
            tiempoLabel.setTextFill(Color.WHITE);
        } else {
            root.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            tiempoLabel.setTextFill(Color.BLACK);
        }
    }

    private void cargarTiempoSeleccionado() {
        if (tiemposGuardadosBox.getValue() != null) {
            inputField.setText(tiemposGuardadosBox.getValue());
        }
    }

    private void guardarTiempoPredefinido(String tiempo) {
        Properties propiedades = new Properties();
        try (FileOutputStream fos = new FileOutputStream("tiempos_predefinidos.properties", true)) {
            propiedades.setProperty("Tiempo " + tiempo, tiempo);
            propiedades.store(fos, null);
            tiemposGuardadosBox.getItems().add(tiempo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarTiemposGuardados() {
        Properties propiedades = new Properties();
        try (FileInputStream fis = new FileInputStream("tiempos_predefinidos.properties")) {
            propiedades.load(fis);
            tiemposGuardadosBox.getItems().addAll(propiedades.stringPropertyNames());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
