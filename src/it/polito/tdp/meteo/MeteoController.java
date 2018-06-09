package it.polito.tdp.meteo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import it.polito.tdp.meteo.db.MeteoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class MeteoController {

	ObservableList<String> elencoMesi;
	Model m;
	
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<String> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;

	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		String risultato=m.trovaSequenza(6);
		txtResult.setText(risultato);
	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		txtResult.setText(m.getUmiditaMedia(Integer.getInteger(boxMese.getValue())));
	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
		
		m=new Model();
		List<String> elenco=new ArrayList<>();
    	for(int i=1;i<=12;i++) {
    		elenco.add(String.valueOf(i)); 
    	}
    	elencoMesi=FXCollections.observableArrayList(elenco);
    	boxMese.setItems(elencoMesi);
    	System.out.println("interfaccia grafica creata con successo");
	}

}
