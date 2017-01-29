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

        if(shortDescription.equals("")||shortDescription.equals(null)){
            shortDescription = "empty";
        }
        if(details.equals("")||shortDescription.equals(null)){
            shortDescription = "empty";
        }
        try {
            if (deadlineValue == null) {
                deadlineValue = LocalDate.now();
            }
        }
        catch(Exception e){
            deadlineValue = LocalDate.now();
        }

        ToDoItem newItem = new ToDoItem(shortDescription, details, deadlineValue); // we do this to select a newly created item
        ToDoData.getInstance().addToDoItem(newItem);//// we do this to select a newly created item
        return newItem;
    }

}
