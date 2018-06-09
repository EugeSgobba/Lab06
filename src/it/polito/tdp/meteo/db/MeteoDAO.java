package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {

		return null;
	}

	public double getAvgRilevamentiLocalitaMese(int mese, String localita) {
		final String sql= "SELECT avg(umidita) as media FROM situazione "
				+ "WHERE data<'2013-"+String.valueOf((mese+1))+"-01' and data>'2013-"+String.valueOf((mese-1))+"-31' and localita='"+localita+"' GROUP BY localita";
		
		double risultato=0;
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			rs.next();
			
			risultato=rs.getDouble("media");

			conn.close();
			return risultato;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<String> getCitta(){
		final String sql = "SELECT DISTINCT localita FROM situazione";

		List<String> citta = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				citta.add(rs.getString("localita"));
			}

			conn.close();
			return citta;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public int getUmiditaDatoGiornoMeseLocalita(int mese, int giorno, String localita) {
		final String sql= "SELECT umidita FROM situazione "
				+ "WHERE data='2013-"+mese+"-"+giorno+"' and localita='"+localita+"'";
		
		int risultato=0;
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			rs.next();
			
			risultato=rs.getInt("umidita");

			conn.close();
			return risultato;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	

}
