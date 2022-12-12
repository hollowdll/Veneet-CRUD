package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Vene;
import model.dao.Dao;

// Servlet implementation class Veneet. REST API.

@WebServlet("/veneet")	// route
public class Veneet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public Veneet() {
        System.out.println("Veneet.Veneet()");
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Veneet.doGet()");
		String searchText = request.getParameter("search");
		String tunnus = request.getParameter("tunnus");
		Dao dao = new Dao();
		String strJSON = "";
		ArrayList<Vene> veneet;
		
		if (searchText != null) {		//Jos kutsun mukana tuli hakusana
			if (!searchText.equals("")) {
				veneet = dao.getAllItems(searchText);	//Haetaan kaikki hakusanan mukaiset							
			} else {
				veneet = dao.getAllItems();	//Haetaan kaikki
			}
			strJSON = new Gson().toJson(veneet);
		} else if (tunnus != null) {
			Vene vene = dao.getItem(Integer.parseInt(tunnus));	// Hae tunnuksella
			strJSON = new Gson().toJson(vene);
		} else {
			veneet = dao.getAllItems();
			strJSON = new Gson().toJson(veneet);
		}
		
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(strJSON);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Veneet.doPost()");
		//Luetaan JSON-tiedot POST-pyynnön bodysta ja luodaan niiden perusteella uusi vene
		String strJSONInput = request.getReader().lines().collect(Collectors.joining());
		//System.out.println(strJSONInput);
		Vene vene = new Gson().fromJson(strJSONInput, Vene.class);
		Dao dao = new Dao();
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		if (dao.addItem(vene)) {
			out.println("{\"response\":1}");  // lisääminen onnistui {"response":1}
		} else {
			out.println("{\"response\":0}");  // lisääminen epäonnistui {"response":0}
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Veneet.doPut()");
		//Luetaan JSON-tiedot PUT-pyynnän bodysta ja luodaan niiden perusteella uusi vene
		String strJSONInput = request.getReader().lines().collect(Collectors.joining());
		//System.out.println("strJSONInput " + strJSONInput);
		Vene vene = new Gson().fromJson(strJSONInput, Vene.class);
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();
		
		if (dao.changeItem(vene)) {
			out.println("{\"response\":1}");  //muuttaminen onnistui {"response":1}
		} else {
			out.println("{\"response\":0}");  //muuttaminen epäonnistui {"response":0}
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Veneet.doDelete()");
		String strTunnus = request.getParameter("tunnus");
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		if (strTunnus != null) {	// jos mukana tuli tunnus
			int tunnus = Integer.parseInt(strTunnus);	// poistettavan tunnus
			Dao dao = new Dao();
			
			if (dao.removeItem(tunnus)) {
				out.println("{\"response\":1}");  // poistaminen onnistui {"response":1}
			} else {
				out.println("{\"response\":0}");  // poistaminen epäonnistui {"response":0}
			}
		} else {
			out.println("{\"response\":0}");
		}
	}

}
