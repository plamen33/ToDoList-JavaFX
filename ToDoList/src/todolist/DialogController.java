package todolist;


import todolist.datamodel.ToDoData;
import todolist.datamodel.ToDoItem;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;


import java.time.LocalDate;

//Commit4:
//        Created a new fxml file which is needed to create a new ToDo item from the GUI-->
//        Created a new Dialog Controller which will control the new GUI - todoItemDialog

public class DialogController {
    @FXML
    private TextField shortDescriptionField;

    @FXML
    private TextArea detailsArea;

    @FXML
    private DatePicker deadlinePicker;

    public void processResults() {
        String shortDescription = shortDescriptionField.getText().trim();
        String details = detailsArea.getText().trim();
        LocalDate deadlineValue = deadlinePicker.getValue();

        ToDoData.getInstance().addToDoItem(new ToDoItem(shortDescription, details, deadlineValue));
    }

}
