package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import java.time.LocalDate;

public class Controller {
    @FXML
    private TableView tableView;

    @FXML
    private TableColumn fileName;

    @FXML
    private TableColumn actualClass;

    @FXML
    private TableColumn probability;


    public void initialize(){
        fileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        actualClass.setCellValueFactory(new PropertyValueFactory<>("actualClass"));
        probability.setCellValueFactory(new PropertyValueFactory<>("probability"));
    }

}
