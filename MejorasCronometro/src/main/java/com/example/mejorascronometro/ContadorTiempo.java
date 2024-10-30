package com.example.mejorascronometro;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ContadorTiempo extends Application {
    private TextField inputField;
    private ProgressBar progressBar;
    private Label tiempoLabel;
    private Button iniciarButton;
    private Button cancelarButton;
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
        tiempoLabel = new Label("Tiempo: 0 segundos");
        iniciarButton = new Button("Iniciar");
        cancelarButton = new Button("Cancelar");
        cancelarButton.setDisable(true);
        root.getChildren().addAll(instruccionLabel, inputField, progressBar, tiempoLabel,
                iniciarButton, cancelarButton);
        iniciarButton.setOnAction(e -> iniciarContador());
        cancelarButton.setOnAction(e -> cancelarContador());
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void iniciarContador() {
        try {
            tiempoTotal = Integer.parseInt(inputField.getText());
            if (tiempoTotal <= 0) {
                throw new NumberFormatException();
            }
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
        tiempoLabel.setText("Tiempo: " + tiempoActual + " segundos");
    }

    private void reiniciarUI() {
        iniciarButton.setDisable(false);
        cancelarButton.setDisable(true);
        inputField.setDisable(false);
        progressBar.setProgress(0);
        tiempoLabel.setText("Tiempo: 0 segundos");
    }

    public static void main(String[] args) {
        launch(args);
    }
}