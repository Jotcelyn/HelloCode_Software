package Nuevo_Modulo_Leccion.controllers;

import javax.swing.JOptionPane;

import Nuevo_Modulo_Leccion.logic.Leccion;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LeccionUIController {
    /*
    public static void mostrarUnaLeccion(Leccion leccionAMostrar, Stage ventanaAnterior) {
        // Mostrar mensaje emergente
        JOptionPane.showMessageDialog(null, "Próximamente esta lógica de mostrar una lección");

        // Volver a mostrar la ventana anterior, por si estaba oculta
        ventanaAnterior.show();
    }
    public static void mostrarUnaLeccion(Leccion leccionAMostrar) {
        JOptionPane.showMessageDialog(null, "Proximamente esta logica de mostar una leccion");
        //MetodosFrecuentes.mostrarVentana("/Modulo_Ejercicios/views/SeleccionMultiple-view.fxml", "Titulo xd");
    } */

    public static void mostrarUnaLeccion(Leccion leccionAMostrar, Stage ventanaActual, String rutaFXML) {
        // Mostrar mensaje emergente
        ventanaActual.close(); 
        JOptionPane.showMessageDialog(null, "Próximamente esta lógica de mostrar una lección");

        try {
            // Volver a la pantalla de ruta del módulo GestionAprendizaje_Modulo
            FXMLLoader loader = new FXMLLoader(LeccionUIController.class.getResource("/GestionAprendizaje_Modulo/Vistas/Ruta.fxml"));
            AnchorPane rutaPane = loader.load();

            // Configurar la nueva escena
            Scene scene = new Scene(rutaPane);
            ventanaActual.setScene(scene);
            ventanaActual.show();
        } catch (Exception e) {
            System.err.println("Error al regresar a la pantalla de ruta: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
