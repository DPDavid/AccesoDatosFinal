module org.example.accesodatosfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires basex;


    opens org.example.accesodatosfinal to javafx.fxml;
    exports org.example.accesodatosfinal;
}