package todolist;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import todolist.datamodel.ToDoData;
import todolist.datamodel.ToDoItem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

       public void initialize(){
//           ToDoItem item1 = new ToDoItem("Learn Java", "Do it every day", LocalDate.of(2017, Month.APRIL, 25));
//           ToDoItem item2 = new ToDoItem("Learn Spring", "Do it almost every day when you can", LocalDate.of(2017, Month.MAY, 27));
//           ToDoItem item3 = new ToDoItem("Do your homeworks", "Homeworks should be done precisely", LocalDate.of(2017, Month.APRIL, 27));
//           ToDoItem item4 = new ToDoItem("Go to the shop", "Buy what is needed and spare no unnecessary money", LocalDate.of(2017, Month.APRIL, 12));
//           ToDoItem item5 = new ToDoItem("Celebrate national holiday", "Celebrate our national holiday at 3 of March", LocalDate.of(2017, Month.MARCH, 3));
//
//           toDoItems = new ArrayList<ToDoItem>();
//           toDoItems.add(item1);
//           toDoItems.add(item2);
//           toDoItems.add(item3);
//           toDoItems.add(item4);
//           toDoItems.add(item5);

           //           ToDoData.getInstance().setToDoItems(toDoItems); // here we create the file which will store the data from the ToDo items // this is a temp file
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

           toDoListView.getItems().setAll(ToDoData.getInstance().getToDoItems());
           toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
           toDoListView.getSelectionModel().selectFirst();

       }
    @FXML
    public void showNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
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
            controller.processResults();
            System.out.println("OK pressed");
        }
        else{
            System.out.println("Cancel pressed");
        }
    }
    public void handleClickListView(){
        ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();
        itemDetailsTextArea.setText(item.getDetails());
        deadlineLabel.setText(item.getDeadline().toString());
        // System.out.println("The selected item is " + item);
//           StringBuilder sb = new StringBuilder(item.getDetails());
//           itemDetailsTextArea.setText(item.getDetails());
//           sb.append("\n\n\n\n");
//           sb.append("Due: ");
//           sb.append(item.getDeadline().toString());
//           itemDetailsTextArea.setText(sb.toString());

    }


}
