package todolist;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import todolist.datamodel.ToDoItem;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Controller {


    private List<ToDoItem> toDoItems;
    @FXML
    private TextArea itemDetailsTextArea;

    @FXML
    private ListView<ToDoItem> toDoListView;

       public void initialize(){
           ToDoItem item1 = new ToDoItem("Learn Java", "Do it every day", LocalDate.of(2017, Month.APRIL, 25));
           ToDoItem item2 = new ToDoItem("Learn Spring", "Do it almost every day when you can", LocalDate.of(2017, Month.MAY, 27));
           ToDoItem item3 = new ToDoItem("Do your homeworks", "Homeworks should be done precisely", LocalDate.of(2017, Month.APRIL, 27));
           ToDoItem item4 = new ToDoItem("Go to the shop", "Buy what is needed and spare no unnecessary money", LocalDate.of(2017, Month.APRIL, 12));
           ToDoItem item5 = new ToDoItem("Celebrate national holiday", "Celebrate our national holiday at 3 of March", LocalDate.of(2017, Month.MARCH, 3));

           toDoItems = new ArrayList<ToDoItem>();
           toDoItems.add(item1);
           toDoItems.add(item2);
           toDoItems.add(item3);
           toDoItems.add(item4);
           toDoItems.add(item5);

           toDoListView.getItems().setAll(toDoItems);
           toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

       }

       public void handleClickListView(){
          ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();
          // System.out.println("The selected item is " + item);
           StringBuilder sb = new StringBuilder(item.getDetails());
           itemDetailsTextArea.setText(item.getDetails());
           sb.append("\n\n\n\n");
           sb.append("Due: ");
           sb.append(item.getDeadline().toString());
           itemDetailsTextArea.setText(sb.toString());

       }


}
