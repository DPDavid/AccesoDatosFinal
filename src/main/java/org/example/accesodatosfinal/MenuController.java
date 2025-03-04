package org.example.accesodatosfinal;

import basex.Libreria;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;



public class MenuController {
    @FXML
    private Button btnSalir;


    private Libreria libreria;


    public MenuController() {
        libreria = new Libreria();
    }

    public void abrirFormularioAgregar() {
        Stage ventanaAgregar = crearVentana("Agregar Libro", 300, 300);

        TextField campoTitulo = new TextField();
        campoTitulo.setPromptText("Título");

        TextField campoAutor = new TextField();
        campoAutor.setPromptText("Autor");

        TextField campoGenero = new TextField();
        campoGenero.setPromptText("Género");

        TextField campoPrecio = new TextField();
        campoPrecio.setPromptText("Precio");

        Button botonGuardar = new Button("Guardar");
        botonGuardar.setOnAction(e -> {
            String titulo = campoTitulo.getText();
            String autor = campoAutor.getText();
            String genero = campoGenero.getText();
            String precioTexto = campoPrecio.getText();

            if (titulo.isEmpty() || autor.isEmpty() || genero.isEmpty() || precioTexto.isEmpty()) {
                mostrarAlerta("Error", "Por favor, rellene todos los campos.", Alert.AlertType.ERROR);
                return;
            }

            try {
                double precio = Double.parseDouble(precioTexto);
                String libroXML = "<Libro><Titulo>" + titulo + "</Titulo><Autor>" + autor + "</Autor><Genero>" + genero + "</Genero><Precio>" + precio + "</Precio></Libro>";
                libreria.insertarLibro(libroXML);
                ventanaAgregar.close();
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "El precio debe ser un número válido.", Alert.AlertType.ERROR);
            }
        });

        Button botonCancelar = new Button("Cancelar");
        botonCancelar.setOnAction(e -> ventanaAgregar.close());

        VBox layout = new VBox(10, campoTitulo, campoAutor, campoGenero, campoPrecio, botonGuardar, botonCancelar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        ventanaAgregar.setScene(new Scene(layout));
        ventanaAgregar.showAndWait();
    }


    public void abrirFormularioBuscar() {
        Stage ventanaBuscar = crearVentana("Buscar Libro", 300, 200);

        TextField campoTitulo = new TextField();
        campoTitulo.setPromptText("Título del Libro");

        Button botonBuscar = new Button("Buscar");
        botonBuscar.setOnAction(e -> {
            String titulo = campoTitulo.getText();
            if (titulo.isEmpty()) {
                mostrarAlerta("Error", "Ingrese un título.", Alert.AlertType.ERROR);
                return;
            }

            String resultado = libreria.obtenerLibroPorTitulo(titulo);

            if (resultado != null && !resultado.isEmpty()) {
                mostrarAlerta("Libro encontrado", resultado, Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No encontrado", "No se encontró un libro con el título: " + titulo, Alert.AlertType.WARNING);
            }

            ventanaBuscar.close();
        });

        VBox layout = new VBox(10, campoTitulo, botonBuscar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        ventanaBuscar.setScene(new Scene(layout));
        ventanaBuscar.showAndWait();
    }

    public void abrirFormularioActualizar() {
        Stage ventanaActualizar = crearVentana("Actualizar Libro", 300, 250);

        TextField campoTitulo = new TextField();
        campoTitulo.setPromptText("Título del Libro");

        TextField campoPrecio = new TextField();
        campoPrecio.setPromptText("Nuevo Precio");

        Button botonActualizar = new Button("Actualizar");
        botonActualizar.setOnAction(e -> {
            String titulo = campoTitulo.getText();
            String precioTexto = campoPrecio.getText();

            if (titulo.isEmpty() || precioTexto.isEmpty()) {
                mostrarAlerta("Error", "Por favor, rellene todos los campos.", Alert.AlertType.ERROR);
                return;
            }

            try {
                double nuevoPrecio = Double.parseDouble(precioTexto);
                libreria.actualizarPrecio(titulo, nuevoPrecio);
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "El precio debe ser un número válido.", Alert.AlertType.ERROR);
            }

            ventanaActualizar.close();
        });

        VBox layout = new VBox(10, campoTitulo, campoPrecio, botonActualizar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        ventanaActualizar.setScene(new Scene(layout));
        ventanaActualizar.showAndWait();
    }

    public void abrirFormularioEliminar() {
        Stage ventanaEliminar = crearVentana("Eliminar Libro", 300, 200);

        Label labelTitulo = new Label("Titulo del Libro");
        TextField campoTitulo = new TextField();
        campoTitulo.setPromptText("Título del Libro");

        Button botonEliminar = new Button("Eliminar");
        botonEliminar.setOnAction(e -> {
            String titulo = campoTitulo.getText();
            if (titulo.isEmpty()) {
                mostrarAlerta("Error", "Ingrese un título.", Alert.AlertType.ERROR);
                return;
            }

            libreria.eliminarLibro(titulo);
            ventanaEliminar.close();
        });

        VBox layout = new VBox(10, labelTitulo,campoTitulo, botonEliminar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        ventanaEliminar.setScene(new Scene(layout));
        ventanaEliminar.showAndWait();
    }

    public void abrirFormularioListar() {
        Stage ventanaListar = crearVentana("Lista de Libros", 900, 600);

        ListView<String> listViewLibros = new ListView<>();
        Button botonCargar = new Button("Mostrar Libros");

        botonCargar.setOnAction(e -> {
            String resultado = libreria.listarLibros();
            listViewLibros.getItems().clear();
            if (resultado != null && !resultado.isEmpty()) {
                String[] libros = resultado.split("\n");
                listViewLibros.getItems().addAll(libros);
            } else {
                mostrarAlerta("Información", "No hay libros en la base de datos.", Alert.AlertType.INFORMATION);
            }
        });

        VBox layout = new VBox(10, botonCargar, listViewLibros);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        ventanaListar.setScene(new Scene(layout));
        ventanaListar.showAndWait();
    }

    public void cerrarMenu() {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);

        libreria.cerrarConexion();
    }


    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private Stage crearVentana(String titulo, int ancho, int alto) {
        Stage ventana = new Stage();
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle(titulo);
        ventana.setWidth(ancho);
        ventana.setHeight(alto);
        return ventana;
    }

}