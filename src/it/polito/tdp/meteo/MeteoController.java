package it.polito.tdp.meteo;

import java.net.URL;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.bean.Citta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;

public class MeteoController {
	
	private Model model ;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Month> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;

	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		Month m = boxMese.getValue() ;
		if(m!=null) {
			List<Citta> best = model.calcolaSequenza(m) ;
			txtResult.appendText("Sequenza ottima per il mese "+m.toString()+"\n");
			txtResult.appendText(best+"\n");
		}

	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {

		Month m = boxMese.getValue() ;
		if(m!=null) {
			txtResult.appendText(String.format("Dati del mese %s\n", m.toString()));

			for(Citta c: model.getLeCitta()) {
				Double u = model.getUmiditaMedia(m, c) ;
				txtResult.appendText(String.format("Città %s: umidità %f\n", c.getNome(), u));
			}
		}
		
	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
		
//		boxMese.getItems().add(Month.JANUARY) ;
//		boxMese.getItems().add(Month.FEBRUARY) ;
		
		for(int mese = 1; mese <= 12 ; mese ++)
			boxMese.getItems().add(Month.of(mese)) ;
		
		// il setConverter serve a definire un metodo alternativo al toString nativo di <Month> per definire
		// l'etichetta del bottone. In questo caso lo covertiamo utilizzando la lingua italiana.
		// La ChoiceBox utilizzerà quindi il toString di questo StringConverter anziché quello di default.
		boxMese.setConverter(new StringConverter<Month>() {
			@Override
			public String toString(Month m) {
				return m.getDisplayName(TextStyle.FULL, Locale.ITALIAN) ;
			}
			
			@Override
			public Month fromString(String string) {
				return null;
			}
		});
	}
	
	public void setModel(Model m) {
		this.model = m ;
	}

}
