package todolist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import todolist.datamodel.ToDoData;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainwindow.fxml"));
        primaryStage.setTitle("ToDo List");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.getIcons().add(new Image(getClass().getResource("images/todo.png").toExternalForm()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public  void stop() throws Exception{
        try {
            ToDoData.getInstance().storeToDoItems();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void init() throws Exception {
        try {
            ToDoData.getInstance().loadToDoItems();

        } catch(Exception e) {
            System.out.println("Big Crash right now - Catch is taking it");
            System.out.println(e.getMessage());
        }
    }
}
