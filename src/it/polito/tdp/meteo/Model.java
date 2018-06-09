package it.polito.tdp.meteo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	MeteoDAO dao;
	Map<String,Citta> citta;

	public Model() {
		dao=new MeteoDAO();
		System.out.println("oggetto model creato con successo");
		citta=new TreeMap<>();
	}

	public String getUmiditaMedia(int mese) {
		String risultato=new String("");
		System.out.print("oggetto MeteoDAO creato con successo");
		for(String c: dao.getCitta()) {
			risultato+=c+": "+dao.getAvgRilevamentiLocalitaMese(mese, c)+"\n";
			System.out.println(risultato);
		}
		return risultato;
	}

	public String trovaSequenza(int mese) {
		String risultato= new String("");
		List<SimpleCity> partenza=new LinkedList<>();
		List<SimpleCity> soluzioneOttima=new LinkedList<>();
		int costoSoluzioneOttima=1000000;
		for(String s: dao.getCitta()) {
			citta.put(s, new Citta(s));
		}
		ricorsione(0,partenza,soluzioneOttima, costoSoluzioneOttima,mese);
		for(SimpleCity s: soluzioneOttima) {
			risultato+=s.getNome()+" ";
		}
		return risultato;
	}

	private int calcolaPunteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
		int punteggio = 0;
		for(SimpleCity s:soluzioneCandidata) {
			punteggio+=s.getCosto();
		}
		return punteggio;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}
	
	public void ricorsione (int livello, List<SimpleCity> soluzioneParziale, List<SimpleCity> soluzioneOttima, int costoSoluzioneOttima, int mese) {
		//scartaggio rapido di una soluzione parziale troppo costosa
		if(calcolaPunteggioSoluzione(soluzioneParziale)> costoSoluzioneOttima) {
			return;
		}
		
		//confronto tra la soluzione ottima e la soluzione completa trovata
		if(livello==NUMERO_GIORNI_TOTALI) {
			if(calcolaPunteggioSoluzione(soluzioneParziale)<costoSoluzioneOttima)
				soluzioneOttima.removeAll(soluzioneOttima);
				soluzioneOttima.addAll(soluzioneParziale);
				costoSoluzioneOttima=calcolaPunteggioSoluzione(soluzioneParziale);
			return;
		}
		
		//creazione di una nuova soluzione parziale
		for(String s: dao.getCitta()) {
			if(livello!=0) {	
				if(aggiuntaPossibile(soluzioneParziale,s,livello)) {	//controllo sulla possibilità di aggiungere una determinata citta alla soluzione parziale
					soluzioneParziale.add(new SimpleCity(s));
					System.out.println(soluzioneParziale);
					citta.get(s).setCounter(citta.get(s).getCounter()+1);
					if(soluzioneParziale.get(livello).getNome().compareTo(soluzioneParziale.get(livello-1).getNome())==0)
						soluzioneParziale.get(livello).setCounter(soluzioneParziale.get(livello).getCounter()+1);
					else
						soluzioneParziale.get(livello).setCounter(0);
				}
				else	continue;

			} else {
				soluzioneParziale.add(new SimpleCity(s));
				citta.get(s).setCounter(citta.get(s).getCounter()+1);
				System.out.println(soluzioneParziale);
			}
			
			if(livello==0) {	//settaggio del costo della soluzioneParziale
				soluzioneParziale.get(livello).setCosto(COST+dao.getUmiditaDatoGiornoMeseLocalita(mese, livello+1, s));
			} 
			else {
				if(soluzioneParziale.get(livello).getNome().compareTo(soluzioneParziale.get(livello-1).getNome())==0)
					soluzioneParziale.get(livello).setCosto(dao.getUmiditaDatoGiornoMeseLocalita(mese, livello+1, s));
				else
					soluzioneParziale.get(livello).setCosto(COST+dao.getUmiditaDatoGiornoMeseLocalita(mese, livello+1, s));
			}
			livello++;
			System.out.println(livello);
			ricorsione(livello,soluzioneParziale,soluzioneOttima,costoSoluzioneOttima,mese);
			System.out.println("backtracking");
			soluzioneParziale.remove(soluzioneParziale.size()-1);	//Backtracking
			livello--;
			System.out.println(livello);
		}
	}

	private boolean aggiuntaPossibile(List<SimpleCity> soluzioneParziale, String s,int livello) {
		if(soluzioneParziale.get(livello-1).getNome().compareTo(s)!=0) {
			if((soluzioneParziale.get(livello-1).getCounter()<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN)
					||(citta.get(s).getCounter()>=NUMERO_GIORNI_CITTA_MAX))	return false;
		}
		else {
			if(citta.get(s).getCounter()>=NUMERO_GIORNI_CITTA_MAX)	return false;
		}
		return true;
	}

}
