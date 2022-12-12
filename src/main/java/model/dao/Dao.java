package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Vene;

public class Dao {
	private Connection con = null;
	private ResultSet rs = null;
	private PreparedStatement stmtPrep = null;
	private String sql;
	private final String DATABASE = "Venekanta.sqlite";
	
	// Avaa yhteys tietokantaan
	private Connection openDatabaseConnection() {
		Connection con = null;
		String path = System.getProperty("catalina.base");
		path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/"); // Eclipsessa
		// System.out.println(path); //Tanne tietokantatiedosto
		// path += "/webapps/"; //Tuotannossa. Laita tietokanta webapps-kansioon
		final String URL = "jdbc:sqlite:" + path + DATABASE;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(URL);
			System.out.println("Yhteys avattu.");
		} catch (Exception e) {
			System.out.println("Yhteyden avaus epäonnistui.");
			e.printStackTrace();
		}
		return con;
	}

	// Sulje yhteys tietokantaan
	private void closeDatabaseConnection() {
		if (stmtPrep != null) {
			try {
				stmtPrep.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
				System.out.println("Yhteys suljettu.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// hae kaikki data
	public ArrayList<Vene> getAllItems() {
		ArrayList<Vene> veneet = new ArrayList<Vene>();
		sql = "SELECT * FROM veneet ORDER BY tunnus DESC";
		try {
			con = openDatabaseConnection();
			if (con != null) {	// yhteys avattu
				stmtPrep = con.prepareStatement(sql);
				rs = stmtPrep.executeQuery();
				if (rs != null) {
					while (rs.next()) {		// siirry seuraavalle riville
						Vene vene = new Vene();
						vene.setTunnus(rs.getInt(1));
						vene.setNimi(rs.getString(2));
						vene.setMerkkimalli(rs.getString(3));
						vene.setPituus(rs.getDouble(4));
						vene.setLeveys(rs.getDouble(5));
						vene.setHinta(rs.getInt(6));
						veneet.add(vene);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConnection();
		}
		return veneet;
	}
	
	// hae data hakusanalla
	public ArrayList<Vene> getAllItems(String searchText) {
		ArrayList<Vene> veneet = new ArrayList<Vene>();
		sql = "SELECT * FROM veneet WHERE nimi LIKE ? OR merkkimalli LIKE ? ORDER BY tunnus DESC";
		try {
			con = openDatabaseConnection();
			if (con != null) {
				stmtPrep = con.prepareStatement(sql);
				stmtPrep.setString(1, "%" + searchText + "%");	// Hae hakusanalla
				stmtPrep.setString(2, "%" + searchText + "%");
				rs = stmtPrep.executeQuery();
				if (rs != null) {
					while (rs.next()) {		// siirry seuraavalle riville
						Vene vene = new Vene();
						vene.setTunnus(rs.getInt(1));
						vene.setNimi(rs.getString(2));
						vene.setMerkkimalli(rs.getString(3));
						vene.setPituus(rs.getDouble(4));
						vene.setLeveys(rs.getDouble(5));
						vene.setHinta(rs.getInt(6));
						veneet.add(vene);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConnection();
		}
		return veneet;
	}
	
	// hae yksittäinen
	public Vene getItem(int tunnus) {
		Vene vene = null;
		sql = "SELECT * FROM veneet WHERE tunnus=?";
		try {
			con = openDatabaseConnection();
			if (con != null) {
				stmtPrep = con.prepareStatement(sql);
				stmtPrep.setInt(1, tunnus);
        		rs = stmtPrep.executeQuery();
    			if (rs.isBeforeFirst()) { 	//jos kysely tuotti dataa
        			rs.next();
        			vene = new Vene();
        			vene.setTunnus(rs.getInt(1));
					vene.setNimi(rs.getString(2));
					vene.setMerkkimalli(rs.getString(3));
					vene.setPituus(rs.getDouble(4));
					vene.setLeveys(rs.getDouble(5));
					vene.setHinta(rs.getInt(6));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConnection();
		}		
		return vene;
	}
	
	// lisää rivi
	public boolean addItem(Vene vene) {
		boolean returnValue = true;
		sql = "INSERT INTO veneet (nimi, merkkimalli, pituus, leveys, hinta) VALUES (?,?,?,?,?)";
		try {
			con = openDatabaseConnection();
			if (con != null) {
				stmtPrep = con.prepareStatement(sql);
				stmtPrep.setString(1, vene.getNimi());
				stmtPrep.setString(2, vene.getMerkkimalli());
				stmtPrep.setDouble(3, vene.getPituus());
				stmtPrep.setDouble(4, vene.getLeveys());
				stmtPrep.setInt(5, vene.getHinta());
				stmtPrep.executeUpdate();
			} else {
				returnValue = false;
			}
		} catch (Exception e) {
			returnValue = false;
			e.printStackTrace();
		} finally {
			closeDatabaseConnection();
		}
		return returnValue;
	}
	
	// muuta riviä
	public boolean changeItem(Vene vene){
		boolean returnValue = true;
		sql="UPDATE veneet SET nimi=?, merkkimalli=?, pituus=?, leveys=?, hinta=? WHERE tunnus=?";						  
		try {
			con = openDatabaseConnection();
			if (con != null) {
				stmtPrep = con.prepareStatement(sql); 
				stmtPrep.setString(1, vene.getNimi());
				stmtPrep.setString(2, vene.getMerkkimalli());
				stmtPrep.setDouble(3, vene.getPituus());
				stmtPrep.setDouble(4, vene.getLeveys());
				stmtPrep.setInt(5, vene.getHinta());
				stmtPrep.setInt(6, vene.getTunnus());
				stmtPrep.executeUpdate();
			} else {
				returnValue = false;
			}
		} catch (Exception e) {				
			e.printStackTrace();
			returnValue = false;
		} finally {
			closeDatabaseConnection();
		}				
		return returnValue;
	}
	
	// poista rivi tunnuksen perusteella (tunnus = id)
	public boolean removeItem(int tunnus) { 
		boolean returnValue = true;
		sql = "DELETE FROM veneet WHERE tunnus=?";
		try {
			con = openDatabaseConnection();
			if (con != null) {
				stmtPrep = con.prepareStatement(sql);
				stmtPrep.setInt(1, tunnus);		// poistetaan tunnuksella
				stmtPrep.executeUpdate();
			} else {
				returnValue = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = false;
		} finally {
			closeDatabaseConnection();
		}
		return returnValue;
	}
}
