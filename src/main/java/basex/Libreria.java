package basex;

import javafx.scene.control.Alert;
import org.basex.api.client.ClientQuery;
import org.basex.api.client.ClientSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Libreria {
    private ClientSession session;

    //Conexion a la base de datos (libreria)
    public Libreria() {
        try {
            this.session = new ClientSession("localhost", 1984, "David", "David");
            session.execute("OPEN libreria");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo conectar a la base de datos.", Alert.AlertType.ERROR);
        }
    }

    //Metodo para ejecutar las consultas xquery
    public String ejecutarConsulta(String xquery) {
        try {
            ClientQuery query = session.query(xquery);
            String resultado = query.execute();
            query.close();
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Metodo para insertar libros en una coleccion XML
    public void insertarLibro(String nombreColeccion, String libroXML) {
        try {
            String xquery = "let $doc := doc('libreria/" + nombreColeccion.trim() + "') " +
                    "return insert node " + libroXML + " into $doc/Libreria/Libros";

            ejecutarConsulta(xquery);
            mostrarAlerta("Éxito", "Libro agregado correctamente.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo guardar el Libro.", Alert.AlertType.ERROR);
        }
    }

    //Metodo para actualizar el precio de un libro en una coleccion XML
    public void actualizarPrecio(String nombreColeccion, String titulo, double nuevoPrecio) {
        try {
            String xquery = "let $doc := doc('libreria/" + nombreColeccion.trim() + "') " +
                    "return replace value of node $doc/Libreria/Libros/Libro[Titulo='" + titulo + "']/Precio " +
                    "with '" + nuevoPrecio + "'";
            ejecutarConsulta(xquery);
            mostrarAlerta("Éxito", "Libro actualizado correctamente.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo actualizar el precio.", Alert.AlertType.ERROR);
        }
    }

    //Metodo para eliminar un libro en una coleccion XML
    public void eliminarLibro(String nombreColeccion, String titulo) {
        try {
            String xquery = "let $doc := doc('libreria/" + nombreColeccion.trim() + "') " +
                    "return delete node $doc/Libreria/Libros/Libro[Titulo='" + titulo + "']";
            ejecutarConsulta(xquery);
            mostrarAlerta("Éxito", "Libro eliminado correctamente.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo eliminar el Libro.", Alert.AlertType.ERROR);
        }
    }

    //Metodo para listar libros de una coleccion XML
    public String listarLibros(String nombreColeccion) {
        String xquery = "for $libro in doc('libreria/" + nombreColeccion.trim() + "')/Libreria/Libros/Libro " +
                "return concat('Título: ', $libro/Titulo, ', Autor: ', $libro/Autor, ', Género: ', $libro/Genero, ', Precio: ', $libro/Precio)";
        return ejecutarConsulta(xquery);
    }

    //Metodo para agregar una coleccion XML a la base de datos
    public void agregarDocumentoAColeccion(String nombreColeccion, String archivoXML) {
        try {
            String xquery = "ADD TO " + nombreColeccion+ " "+ archivoXML;
            session.execute(xquery);
            mostrarAlerta("Éxito", "Documento añadido correctamente a la colección.", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Metodo para eliminar una coleccion XML de la base de datos
    public void eliminarColeccion(String nombreDocumento) {
        try {
            String comando = "DELETE " + nombreDocumento;
            session.execute(comando);
            mostrarAlerta("Éxito", "Colección eliminada correctamente.", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo eliminar la colección: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    //Metodo para adquirir todas las colecciones de la base de datos
    public List<String> obtenerColecciones() {
        List<String> colecciones = new ArrayList<>();
        try {
            String xquery = "db:list('libreria')";
            String resultado = ejecutarConsulta(xquery);

            if (resultado != null && !resultado.isEmpty()) {
                colecciones.addAll(Arrays.asList(resultado.split("\n")));
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudieron obtener las colecciones.", Alert.AlertType.ERROR);
        }
        return colecciones;
    }

    //Metodo para mostrar las alertas que salen por pantalla
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    //Metodo para cerra la conxion a base de datos
    public void cerrarConexion() {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo cerrar la sesión de la base de datos.", Alert.AlertType.ERROR);
            }
        }
    }
}
