package org.example.accesodatosfinal;

import basex.Libreria;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;


public class MenuController {
    @FXML
    private Button btnSalir;


    private Libreria libreria;


    public MenuController() {
        libreria = new Libreria();
    }

    public void abrirFormularioAgregar() {
        Stage ventanaAgregar = crearVentana("Agregar Libro", 300, 350);

        ComboBox<String> comboColeccion = new ComboBox<>();
        comboColeccion.setPromptText("Seleccionar Colección");
        comboColeccion.getItems().addAll(libreria.obtenerColecciones());

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
            String coleccionSeleccionada = comboColeccion.getValue();
            String titulo = campoTitulo.getText();
            String autor = campoAutor.getText();
            String genero = campoGenero.getText();
            String precioTexto = campoPrecio.getText();

            if (coleccionSeleccionada == null || titulo.isEmpty() || autor.isEmpty() || genero.isEmpty() || precioTexto.isEmpty()) {
                mostrarAlerta("Error", "Por favor, seleccione una colección y rellene todos los campos.", Alert.AlertType.ERROR);
                return;
            }

            try {
                double precio = Double.parseDouble(precioTexto);
                String libroXML = "<Libro><Titulo>" + titulo + "</Titulo><Autor>" + autor + "</Autor><Genero>" + genero + "</Genero><Precio>" + precio + "</Precio></Libro>";

                libreria.insertarLibro(coleccionSeleccionada, libroXML);

                ventanaAgregar.close();
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "El precio debe ser un número válido.", Alert.AlertType.ERROR);
            }
        });

        Button botonCancelar = new Button("Cancelar");
        botonCancelar.setOnAction(e -> ventanaAgregar.close());

        VBox layout = new VBox(10, comboColeccion, campoTitulo, campoAutor, campoGenero, campoPrecio, botonGuardar, botonCancelar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        ventanaAgregar.setScene(new Scene(layout));
        ventanaAgregar.showAndWait();
    }



    public void abrirFormularioActualizar() {
        Stage ventanaActualizar = crearVentana("Actualizar Libro", 300, 250);

        ComboBox<String> comboColeccion = new ComboBox<>();
        comboColeccion.setPromptText("Seleccionar Colección");
        comboColeccion.getItems().addAll(libreria.obtenerColecciones());

        TextField campoTitulo = new TextField();
        campoTitulo.setPromptText("Título del Libro");

        TextField campoPrecio = new TextField();
        campoPrecio.setPromptText("Nuevo Precio");

        Button botonActualizar = new Button("Actualizar");
        botonActualizar.setOnAction(e -> {
            String coleccionSeleccionada = comboColeccion.getValue();
            String titulo = campoTitulo.getText();
            String precioTexto = campoPrecio.getText();

            if (coleccionSeleccionada == null || titulo.isEmpty() || precioTexto.isEmpty()) {
                mostrarAlerta("Error", "Por favor, rellene todos los campos.", Alert.AlertType.ERROR);
                return;
            }

            try {
                double nuevoPrecio = Double.parseDouble(precioTexto);
                libreria.actualizarPrecio(coleccionSeleccionada,titulo, nuevoPrecio);
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "El precio debe ser un número válido.", Alert.AlertType.ERROR);
            }

            ventanaActualizar.close();
        });

        VBox layout = new VBox(10,comboColeccion, campoTitulo, campoPrecio, botonActualizar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        ventanaActualizar.setScene(new Scene(layout));
        ventanaActualizar.showAndWait();
    }

    public void abrirFormularioEliminar() {
        Stage ventanaEliminar = crearVentana("Eliminar Libro", 300, 200);

        ComboBox<String> comboColeccion = new ComboBox<>();
        comboColeccion.setPromptText("Seleccionar Colección");
        comboColeccion.getItems().addAll(libreria.obtenerColecciones());

        Label labelTitulo = new Label("Titulo del Libro");
        TextField campoTitulo = new TextField();
        campoTitulo.setPromptText("Título del Libro");

        Button botonEliminar = new Button("Eliminar");
        botonEliminar.setOnAction(e -> {
            String coleccionSeleccionada = comboColeccion.getValue();
            String titulo = campoTitulo.getText();

            if (coleccionSeleccionada == null || titulo.isEmpty()) {
                mostrarAlerta("Error", "Ingrese un título.", Alert.AlertType.ERROR);
                return;
            }

            libreria.eliminarLibro(coleccionSeleccionada ,titulo);
            ventanaEliminar.close();
        });

        VBox layout = new VBox(10, comboColeccion,labelTitulo,campoTitulo, botonEliminar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        ventanaEliminar.setScene(new Scene(layout));
        ventanaEliminar.showAndWait();
    }

    public void abrirFormularioListar() {
        Stage ventanaListar = crearVentana("Lista de Libros", 900, 600);

        ComboBox<String> comboColeccion = new ComboBox<>();
        comboColeccion.setPromptText("Seleccionar Colección");
        comboColeccion.getItems().addAll(libreria.obtenerColecciones());

        ListView<String> listViewLibros = new ListView<>();
        Button botonCargar = new Button("Mostrar Libros");

        botonCargar.setOnAction(e -> {
            String coleccionSeleccionada = comboColeccion.getValue();
            String resultado = libreria.listarLibros(coleccionSeleccionada);
            listViewLibros.getItems().clear();
            if (resultado != null && !resultado.isEmpty()) {
                String[] libros = resultado.split("\n");
                listViewLibros.getItems().addAll(libros);
            } else {
                mostrarAlerta("Información", "No hay libros en la base de datos.", Alert.AlertType.INFORMATION);
            }
        });

        VBox layout = new VBox(10, comboColeccion,botonCargar, listViewLibros);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        ventanaListar.setScene(new Scene(layout));
        ventanaListar.showAndWait();
    }

    public void abrirFormularioColecciones() {
        Stage ventana = new Stage();
        ventana.setTitle("Gestión de Colecciones");

        TextField campoNombreColeccion = new TextField();
        campoNombreColeccion.setPromptText("Nombre de la Colección");

        Button botonSeleccionarArchivo = new Button("Seleccionar Archivo XML");
        Label etiquetaArchivo = new Label("No se ha seleccionado ningún archivo");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos XML", "*.xml"));

        final File[] archivoSeleccionado = {null};

        botonSeleccionarArchivo.setOnAction(e -> {
            File archivo = fileChooser.showOpenDialog(ventana);
            if (archivo != null) {
                archivoSeleccionado[0] = archivo;
                etiquetaArchivo.setText("Archivo: " + archivo.getName());
            }
        });

        Button botonAgregar = new Button("Añadir Documento a Colección");
        botonAgregar.setOnAction(e -> {
            String nombreColeccion = campoNombreColeccion.getText();
            if (nombreColeccion.isEmpty() || archivoSeleccionado[0] == null) {
                mostrarAlerta("Error", "Debe ingresar un nombre de colección y seleccionar un archivo.", Alert.AlertType.ERROR);
                return;
            }
            libreria.agregarDocumentoAColeccion(nombreColeccion, archivoSeleccionado[0].getAbsolutePath());
        });

        Button botonEliminar = new Button("Eliminar Colección");
        botonEliminar.setOnAction(e -> {
            String nombreColeccion = campoNombreColeccion.getText();
            if (nombreColeccion.isEmpty()) {
                mostrarAlerta("Error", "Ingrese un nombre de colección.", Alert.AlertType.ERROR);
                return;
            }
            libreria.eliminarColeccion(nombreColeccion);
        });

        VBox layout = new VBox(10, campoNombreColeccion, botonSeleccionarArchivo, etiquetaArchivo, botonAgregar, botonEliminar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 400, 250);
        ventana.setScene(scene);
        ventana.showAndWait();
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