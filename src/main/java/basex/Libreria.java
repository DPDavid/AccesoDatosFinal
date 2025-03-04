package basex;

import javafx.scene.control.Alert;
import org.basex.api.client.ClientQuery;
import org.basex.api.client.ClientSession;

public class Libreria {
    private ClientSession session;

    public Libreria() {
        try {
            this.session = new ClientSession("localhost", 1984, "David", "David");
            session.execute("OPEN libreria");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo conectar a la base de datos.", Alert.AlertType.ERROR);
        }
    }

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

    public void insertarLibro(String libroXML) {
        try {
            String xquery = "insert node " + libroXML + " into /Libreria/Libros";
            ejecutarConsulta(xquery);
            mostrarAlerta("Éxito", "Libro agregado correctamente.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo guardar el Libro.", Alert.AlertType.ERROR);
        }
    }

    public String obtenerLibroPorTitulo(String titulo) {
        String xquery = "for $libro in /Libreria/Libros/Libro[Titulo='" + titulo + "'] return $libro";
        return ejecutarConsulta(xquery);
    }

    public void actualizarPrecio(String titulo, double nuevoPrecio) {
        try {
            String xquery = "replace value of node /Libreria/Libros/Libro[Titulo='" + titulo + "']/Precio with '" + nuevoPrecio + "'";
            ejecutarConsulta(xquery);
            mostrarAlerta("Éxito", "Libro actualizado correctamente.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo actualizar el precio.", Alert.AlertType.ERROR);
        }
    }

    public void eliminarLibro(String titulo) {
        try {
            String xquery = "delete node /Libreria/Libros/Libro[Titulo='" + titulo + "']";
            ejecutarConsulta(xquery);
            mostrarAlerta("Éxito", "Libro eliminado correctamente.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo eliminar el Libro.", Alert.AlertType.ERROR);
        }
    }

    public String listarLibros() {
        String xquery = "for $libro in /Libreria/Libros/Libro " +
                "return concat('Título: ', $libro/Titulo, ', Autor: ', $libro/Autor, ', Género: ', $libro/Genero, ', Precio: ', $libro/Precio)";
        return ejecutarConsulta(xquery);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

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
