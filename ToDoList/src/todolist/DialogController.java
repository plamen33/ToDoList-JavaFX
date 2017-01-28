package todolist;


import todolist.datamodel.ToDoData;
import todolist.datamodel.ToDoItem;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;


import java.time.LocalDate;


public class DialogController {
    @FXML
    private TextField shortDescriptionField;

    @FXML
    private TextArea detailsArea;

    @FXML
    private DatePicker deadlinePicker;

    public ToDoItem processResults() {
        String shortDescription = shortDescriptionField.getText().trim();
        String details = detailsArea.getText().trim();
        LocalDate deadlineValue = deadlinePicker.getValue();

        ToDoItem newItem = new ToDoItem(shortDescription, details, deadlineValue); // we do this to select a newly created item
        ToDoData.getInstance().addToDoItem(newItem);//// we do this to select a newly created item
        return newItem;
    }

}
