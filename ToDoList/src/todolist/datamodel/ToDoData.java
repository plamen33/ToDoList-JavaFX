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


public class ToDoData {
    private static ToDoData instance = new ToDoData();
    private static String filename = "ToDoListItems.txt";

    private ObservableList<ToDoItem> toDoItems;
    private DateTimeFormatter formatter;

    public static ToDoData getInstance(){     // to access data we have to go through this
        return instance;
    }
    private ToDoData(){ // we make a private constructor to prevent anyone to make a new version of this class: ToDoData
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

    public void loadToDoItems() throws IOException{ // load todo items from a file

        toDoItems = FXCollections.observableArrayList();  // we use such list here as in the Controller file we have toDoListView.getItems().setAll(toDoItems); and the setAll method requires  ObservableList
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);
        StringBuffer sb = new StringBuffer();
        String input;

        try{
            while((input = br.readLine()) != null){
                sb.append(input + "\n");  // when we add \n we keep the formatting of the text - new lines are kept in the description of a task
            }
            String[] arrayWithItems = sb.toString().split("%%%%%%%");
            for (int i = 0; i < arrayWithItems.length; i++) {
                String[] itemPieces = arrayWithItems[i].split(";;;;;;;"); // we split by the element of item separator
                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];;
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

    }

    public void storeToDoItems() throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            Iterator<ToDoItem> iter = toDoItems.iterator();
            while (iter.hasNext()) {
                ToDoItem item = iter.next();
                bw.write(String.format("%s;;;;;;;%s;;;;;;;%s", item.getShortDescription(), item.getDetails(), item.getDeadline().format(formatter)));
                bw.write("%%%%%%%"); // this is the new item separator
//                bw.newLine();
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