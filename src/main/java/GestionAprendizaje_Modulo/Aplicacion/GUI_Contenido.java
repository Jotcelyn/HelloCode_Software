package GestionAprendizaje_Modulo.Aplicacion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI_Contenido extends Application {
    @Override
    public void start(Stage stage)throws Exception{
    // FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/GestionAprendizaje_Modulo/Vistas/selectorRol.fxml"));
    // Scene scene=new Scene(fxmlLoader.load());
    // stage.setTitle("Rol");//Titulo
    // stage.setScene(scene);
    // stage.show();
    // }
     // Cargar la pantalla de login del módulo de usuario
    //     FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Modulo_Usuario/views/login.fxml"));
    //     Scene scene = new Scene(fxmlLoader.load(), 360, 720);
    //     stage.setTitle("Hello Code Software - Login");
    //     stage.setScene(scene);
    //     stage.setResizable(false);
    //     stage.show();
    // }
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Modulo_Usuario/views/Splash.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("EntradaAplicativo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main (String[] args){
        launch(args);
    }

}