package com.example.testfx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import javafx.scene.layout.VBox;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class HelloApplication extends Application {
    private ObservableList<Component> data = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPrefSize(400, 300);

        Label title = new Label("Iniciar Sesión");
        TextField userField = new TextField();
        userField.setPromptText("Usuario");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");

        Button submitButton = new Button("Login");
        submitButton.getStyleClass().addAll("btn", "btn-primary");

        gridPane.add(title, 0, 0, 2, 1);
        gridPane.add(userField, 0, 1, 2, 1);
        gridPane.add(passwordField, 0, 2, 2, 1);
        gridPane.add(submitButton, 0, 3, 2, 1);

        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        Scene scene = new Scene(gridPane);
        applyStyles(scene);

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

        submitButton.setOnAction(event -> {
            String userText = userField.getText();
            String passwordText = passwordField.getText();

            if (login(userText, passwordText)) {
                System.out.println("¡Login exitoso!");
                stage.close();
                try {
                    menuopciones();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("❌ Credenciales incorrectas");
            }
        });
    }

    public static boolean login(String user, String psw) {
        return "root".equals(user) && "1234".equals(psw);
    }

    public void menuopciones() throws IOException {
        Stage menuStage = new Stage();

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);
        gridPane.setHgap(20);
        gridPane.setPrefSize(400, 400);

        Label title = new Label("Menú de Opciones");

        // Cargar imagen del logo
        ImageView logo = new ImageView();
        try {
            Image logoImage = new Image(HelloApplication.class.getResourceAsStream("/images/logo.png"));
            logo.setImage(logoImage);
            logo.setPreserveRatio(true);
            logo.setFitWidth(200);  // Ajustar dinámicamente el ancho
            logo.setFitHeight(200); // Ajustar dinámicamente la altura
        } catch (Exception e) {
            System.err.println("❌ Error al cargar la imagen: " + e.getMessage());
        }

        Button ver = new Button("Ver productos");
        Button add = new Button("Añadir producto");
        Button close = new Button("Cerrar Sesión");

        ver.getStyleClass().add("btn");
        add.getStyleClass().add("btn");
        close.getStyleClass().addAll("btn", "btn-danger");

        // Añadir elementos al GridPane
        gridPane.add(logo, 0, 0, 2, 1);  // Centrar la imagen
        gridPane.add(title, 0, 1, 2, 1);
        gridPane.add(ver, 0, 2);
        gridPane.add(add, 1, 2);
        gridPane.add(close, 1, 3);

        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setHalignment(logo, HPos.CENTER);
        GridPane.setHalignment(close, HPos.CENTER);

        Scene menuScene = new Scene(gridPane);
        menuScene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        menuStage.setTitle("Menú de Opciones");
        menuStage.setScene(menuScene);
        menuStage.show();

        ver.setOnAction(event -> verProductos());
        add.setOnAction(event -> agregarProducto());
        close.setOnAction(event -> {
            menuStage.close();
            System.out.println("👋 Sesión cerrada.");
        });
    }

    public void verProductos() {
        Stage productTable = new Stage();

        TableView<Component> tableView = new TableView<>();
        tableView.setEditable(true);
        tableView.setPrefSize(800, 600);

        TableColumn<Component, String> colNom = new TableColumn<>("Nombre");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Component, String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));

        TableColumn<Component, Double> colPreu = new TableColumn<>("Precio");
        colPreu.setCellValueFactory(new PropertyValueFactory<>("preu"));

        TableColumn<Component, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Component, Void> colEliminar = new TableColumn<>("Eliminar");

        colNom.setPrefWidth(150);
        colMarca.setPrefWidth(150);
        colPreu.setPrefWidth(100);
        colStock.setPrefWidth(100);
        colEliminar.setPrefWidth(100);

        colEliminar.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("❌");

            {
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> {
                    Component item = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(item);
                    GestorComponentes.eliminarComponente(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        tableView.getColumns().addAll(colNom, colMarca, colPreu, colStock, colEliminar);

        data = FXCollections.observableArrayList(GestorComponentes.leerComponentes());
        tableView.setItems(data);

        VBox vbox = new VBox(tableView);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Scene scene = new Scene(vbox, 800, 600);
        applyStyles(scene);

        productTable.setScene(scene);
        productTable.setTitle("Lista de Productos");
        productTable.show();
    }

    public void agregarProducto() {
        Stage addStage = new Stage();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label title = new Label("Agregar Producto");
        TextField tipoField = new TextField();
        tipoField.setPromptText("Tipo (Procesador, TargetaGrafica, etc.)");
        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre");
        TextField marcaField = new TextField();
        marcaField.setPromptText("Marca");
        TextField precioField = new TextField();
        precioField.setPromptText("Precio");
        TextField stockField = new TextField();
        stockField.setPromptText("Stock");

        Button imageButton = new Button("Subir Imagen");
        Label imageLabel = new Label("Imagen no seleccionada");

        imageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(addStage);
            if (selectedFile != null) {
                imageLabel.setText(selectedFile.getName());
                try {
                    java.nio.file.Path destino = Paths.get("src/main/resources/images/" + selectedFile.getName());

                    Files.copy(selectedFile.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

                    imageLabel.setText("Imagen guardada: " + destino.toString());

                } catch (IOException e) {
                    System.out.println("❌ Error al guardar la imagen: " + e.getMessage());
                }
            }
        });

        Button submitButton = new Button("Agregar");
        submitButton.setOnAction(event -> {
            try {
                String tipo = tipoField.getText().trim();
                String nombre = nombreField.getText().trim();
                String marca = marcaField.getText().trim();
                double precio = Double.parseDouble(precioField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());

                int nuevoId = data.size() + 1;
                Component nuevo;

                switch (tipo.toLowerCase()) {
                    case "procesador":
                        nuevo = new Processador(nuevoId, nombre, marca, precio, stock, 3.5, 8, "AM4");
                        break;
                    case "targetagrafica":
                        nuevo = new TargetaGrafica(nuevoId, nombre, marca, precio, stock, 8, 1.7, 220);
                        break;
                    case "discdur":
                        nuevo = new DiscDur(nuevoId, nombre, marca, precio, stock, 1000, "SSD");
                        break;
                    case "fontalimentacio":
                        nuevo = new FontAlimentacio(nuevoId, nombre, marca, precio, stock, 750, "80 Plus Gold", true);
                        break;
                    default:
                        System.out.println("❌ Tipo no reconocido.");
                        return;
                }

                GestorComponentes.guardarComponente(nuevo);
                data.add(nuevo);

                System.out.println("✅ Producto agregado correctamente.");
                addStage.close();
            } catch (Exception e) {
                System.out.println("❌ Error en los datos ingresados: " + e.getMessage());
            }
        });

        gridPane.add(title, 0, 0, 2, 1);
        gridPane.add(tipoField, 0, 1, 2, 1);
        gridPane.add(nombreField, 0, 2, 2, 1);
        gridPane.add(marcaField, 0, 3, 2, 1);
        gridPane.add(precioField, 0, 4, 2, 1);
        gridPane.add(stockField, 0, 5, 2, 1);
        gridPane.add(imageButton, 0, 6);
        gridPane.add(imageLabel, 1, 6);
        gridPane.add(submitButton, 0, 7, 2, 1);

        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        Scene scene = new Scene(gridPane, 400, 500);
        applyStyles(scene);
        addStage.setScene(scene);
        addStage.setTitle("Agregar Producto");
        addStage.show();
    }


    private void applyStyles(Scene scene) {
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("❌ No se pudo cargar el CSS: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
