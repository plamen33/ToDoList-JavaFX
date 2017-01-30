package todolist.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

public class ToDoData {
    private static ToDoData instance = new ToDoData();
    private static String filename = "ToDoListItems.txt";

    private ObservableList<ToDoItem> toDoItems;  // changed to ObservableList
    private DateTimeFormatter formatter;  // to manipulate the date

    public static ToDoData getInstance(){     // to access data we have to go through this
        return instance;
    }
    private ToDoData(){ // we make a private contrsuctor to prevent anyone to make a ne version of this class: ToDoData
        // (We cannot instantiate a new object from this class by any other means essentially)
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }
    public ObservableList<ToDoItem> getToDoItems() {   // changed to ObservableList
        return toDoItems;
    }

    public void addToDoItem(ToDoItem item) {
        toDoItems.add(item);
    }
    public void editToDoItem(ToDoItem item) {
        toDoItems.remove(item);
        toDoItems.add(item);

    }
//    public void setToDoItems(List<ToDoItem> toDoItems){  // we need this only run the application and then close it to save the hard coded items
//        this.toDoItems = toDoItems;
//    }   // this was used for the hard coded items we defined initially

    public void loadToDoItems() throws IOException{ // load todo items from a file

        toDoItems = FXCollections.observableArrayList();  // we use such list here as in the Controller file we have toDoListView.getItems().setAll(toDoItems); and the setAll method requires  ObservableList
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try{
            while((input = br.readLine()) != null){
                String[] itemPieces = input.split("\t");

                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];

                LocalDate date = LocalDate.parse(dateString, formatter);
                ToDoItem toDoItem = new ToDoItem(shortDescription, details, date);
                toDoItems.add(toDoItem);
            }
        }
        finally {
            if(br != null){
                br.close();
            }
        }

    }  // end of loadToDoItems

    public void storeToDoItems() throws IOException {   // writes data of todo items to a file
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            Iterator<ToDoItem> iter = toDoItems.iterator();
            while (iter.hasNext()) {
                ToDoItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s", item.getShortDescription(), item.getDetails(), item.getDeadline().format(formatter)));
                bw.newLine();
            }
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    }
        public void deleteToDoItem(ToDoItem item){
            toDoItems.remove(item);
        }

}