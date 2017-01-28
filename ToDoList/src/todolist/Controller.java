package todolist;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import todolist.datamodel.ToDoData;
import todolist.datamodel.ToDoItem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Controller {


    private List<ToDoItem> toDoItems;
    @FXML
    private TextArea itemDetailsTextArea;

    @FXML
    private ListView<ToDoItem> toDoListView;

    @FXML
    private Label deadlineLabel;

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu listContextMenu;

    public void initialize(){
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });
        listContextMenu.getItems().addAll(deleteMenuItem);

        toDoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItem> observable, ToDoItem oldValue, ToDoItem newValue) {
                if(newValue!=null){
                    ToDoItem item = toDoListView.getSelectionModel().getSelectedItem(); // we show the details of 1st item
                    itemDetailsTextArea.setText(item.getDetails());         // we show the details of 1st item
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");  // to format the date outlook
                    deadlineLabel.setText(df.format(item.getDeadline()));  // we show the deadline
                }
            }
        });

        toDoListView.setItems(ToDoData.getInstance().getToDoItems());  // we bind the toDoListView to Observable list in ToDoData
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toDoListView.getSelectionModel().selectFirst();

        /// we use the JavaFX cell factory to paint a cell when we have deadline today, 1 day before and dates in the past
        toDoListView.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> param) {
                ListCell<ToDoItem> cell = new ListCell<ToDoItem>(){
                    @Override
                    protected void updateItem(ToDoItem item, boolean empty){
                        super.updateItem(item, empty);
                        if(empty){
                            setText(null);
                        }
                        else{
                            setText(item.getShortDescription());
                            if(item.getDeadline().equals(LocalDate.now())){
                                setTextFill(Color.ORANGE);
                            }
                            else if(item.getDeadline().equals(LocalDate.now().plusDays(1))){
                                setTextFill(Color.BLUEVIOLET);
                            }
                            else if(item.getDeadline().isBefore(LocalDate.now())){
                                setTextFill(Color.RED);

                            }
                        }
                    }
                };

                cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                    if(isNowEmpty){
                        cell.setContextMenu(null);
                    }
                    else{
                        cell.setContextMenu(listContextMenu);
                    }
                });
                return cell;
            }
        });
    }
    @FXML
    public void showNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add new ToDo item");  // title for the new item screen window
        dialog.setHeaderText("Use this dialog to create a new window");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }
        catch(IOException e){
            System.out.println("no dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent()&& result.get() == ButtonType.OK){
            DialogController controller = fxmlLoader.getController();
            ToDoItem newItem = controller.processResults();  /// we do this in order to select new item
            //we do not need that any more         toDoListView.getItems().setAll(ToDoData.getInstance().getToDoItems()); // this will make our item to be added in our list visually not after restart as previously
            toDoListView.getSelectionModel().select(newItem); ///// we do this in order to select new item
        }
    }

    public void handleClickListView(){
        ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();
        itemDetailsTextArea.setText(item.getDetails());
        deadlineLabel.setText(item.getDeadline().toString());
    }
    public void deleteItem(ToDoItem item){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete ToDo Item");
        alert.setHeaderText("Delete item: " + item.getShortDescription());
        alert.setContentText("Are you sure? Press OK to confirm, or Cancel to back out");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()&& (result.get() == ButtonType.OK)){
            ToDoData.getInstance().deleteToDoItem(item);
        }
    }


}