package todolist;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
    @FXML
    private ToggleButton filterToggleButton;
    @FXML
    private ToggleButton filterToggleButton1;

    private FilteredList<ToDoItem> filteredList;

    private Predicate<ToDoItem> showAllItems;
    private Predicate<ToDoItem> showTodaysItems;
    private Predicate<ToDoItem> showOldItems;

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
                    ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();
                    itemDetailsTextArea.setText(item.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");  // to format the date outlook
                    deadlineLabel.setText(df.format(item.getDeadline()));
                }
            }
        });
        showAllItems = new Predicate<ToDoItem>() {
            @Override
            public boolean test(ToDoItem toDoItem) {
                return true;
            }
        };
        showTodaysItems = new Predicate<ToDoItem>() {
            @Override
            public boolean test(ToDoItem toDoItem) {
                return (toDoItem.getDeadline().equals(LocalDate.now()));
            }
        };
        showOldItems = new Predicate<ToDoItem>() {
            @Override
            public boolean test(ToDoItem toDoItem) {
                return (toDoItem.getDeadline().isBefore(LocalDate.now()));
            }
        };
        filteredList = new FilteredList<ToDoItem>(ToDoData.getInstance().getToDoItems(), showAllItems);
        //  use this to sort the items from first to last based on deadline
        SortedList<ToDoItem> sortedList = new SortedList<ToDoItem>(filteredList, new Comparator<ToDoItem>() {
            @Override
            public int compare(ToDoItem o1, ToDoItem o2) {
                return o1.getDeadline().compareTo(o2.getDeadline());
            }
        });

        toDoListView.setItems(sortedList); // we use this to sort the items from first to last based on deadline

        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toDoListView.getSelectionModel().selectFirst();

        /// use the JavaFX cell factory to paint a cell when we have deadline today, 1 day before and dates in the past
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
                            else if(item.getDeadline().isAfter(LocalDate.now().plusDays(1))){
                                setTextFill(Color.BLACK);
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
            ToDoItem newItem = controller.processResults();
            toDoListView.getSelectionModel().select(newItem);
    }
    }
    @FXML
    public void showEditItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit ToDo item");
        dialog.setHeaderText("Please edit the ToDo item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }
        catch(IOException e){
            System.out.println("no edit dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        DialogController controller = fxmlLoader.getController();
        ToDoItem editToDoItem = toDoListView.getSelectionModel().getSelectedItem();
        ToDoItem editItem = controller.EditToDoItem(editToDoItem);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent()&& result.get() == ButtonType.OK){
            controller.UpdateToDoItem(editToDoItem);
            toDoListView.getSelectionModel().select(editToDoItem); ///// we do this in order to select new item
            toDoListView.refresh();
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
    @FXML
    public void handleFilterButton(){
        ToDoItem selectedItem = toDoListView.getSelectionModel().getSelectedItem();
        if(filterToggleButton.isSelected()){
            filteredList.setPredicate(showTodaysItems);
            if(filteredList.isEmpty()){
                itemDetailsTextArea.clear();
                deadlineLabel.setText("");
            }
            else if(filteredList.contains(selectedItem)){
                toDoListView.getSelectionModel().select(selectedItem);
            }
            else{
                toDoListView.getSelectionModel().selectFirst();
            }
        }
        else{
            filteredList.setPredicate(showAllItems);
            toDoListView.getSelectionModel().select(selectedItem);
        }
    }
    @FXML
    public void handleFilterButtonOld(){
        ToDoItem selectedItem = toDoListView.getSelectionModel().getSelectedItem();
        if(filterToggleButton1.isSelected()){
            filteredList.setPredicate(showOldItems);
            if(filteredList.isEmpty()){
                itemDetailsTextArea.clear();
                deadlineLabel.setText("");
            }
            else if(filteredList.contains(selectedItem)){
                toDoListView.getSelectionModel().select(selectedItem);
            }
            else{
                toDoListView.getSelectionModel().selectFirst();
            }
        }
        else{
            filteredList.setPredicate(showAllItems);
            toDoListView.getSelectionModel().select(selectedItem);
        }
    }
    @FXML
    public void handleExit(){
        Platform.exit();
    }

    @FXML
    private void onAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("ToDoList JavaFX");
        alert.setTitle("About");
        alert.setContentText("ToDoList version: 3\nVersion Release: 30.01.2017\nDevelopment platform: Java\nDeveloper: Plamen Petkov\n\nPowered by Java 8");
        alert.show();

    }
    @FXML
    private void onHelp(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("ToDoList HELP");
        alert.setTitle("Help");
        alert.setContentText("To delete a ToDo item select the item and\nclick right mouse button - a Delete button will appear.\nClick and follow the new window options.");
        alert.show();

    }
}