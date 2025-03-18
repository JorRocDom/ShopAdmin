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
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.Optional;
import javafx.stage.FileChooser;
import java.io.File;

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

        TableColumn<Component, String> colImagen = new TableColumn<>("Imagen");

        colImagen.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        colImagen.setCellFactory(param -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(getClass().getResourceAsStream(imagePath));
                        imageView.setImage(image);
                        imageView.setFitWidth(50);  // Ajusta el tamaño según necesites
                        imageView.setFitHeight(50);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                        System.err.println("❌ Error al cargar la imagen: " + e.getMessage());
                    }
                }
            }
        });

        colImagen.setPrefWidth(80);
        tableView.getColumns().add(colImagen);


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

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Producto");
        dialog.setHeaderText("Ingrese los datos del producto en el formato, siguiendo este ejemplo:\n" +
                "EJEMPLO: Procesador,Intel Core i7,Intel,350.00,10 \n" +
                "(Componente, nombre, marca, precio, stock)\n");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(datos -> {
            String[] partes = datos.split(",");
            if (partes.length == 5) {
                try {
                    String tipo = partes[0].trim();
                    String nombre = partes[1].trim();
                    String marca = partes[2].trim();
                    double precio = Double.parseDouble(partes[3].trim());
                    int stock = Integer.parseInt(partes[4].trim());

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
                } catch (Exception e) {
                    System.out.println("❌ Error en los datos ingresados: " + e.getMessage());
                }
            } else {
                System.out.println("❌ Formato incorrecto.");
            }
        });
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
