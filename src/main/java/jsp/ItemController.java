package jsp;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.*;

import java.security.SecureRandom;
import java.math.BigInteger;
import org.w3c.dom.Document;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.hibernate.HibernateException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ItemController  extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	private static final long serialVersionUID = 1L;

	private SecureRandom random = new SecureRandom();

	String APP_ID = "";
	String APP_SECRET = "";
	String REDIRECT_URL = "";

	public ItemController() {
		super();
	}

	public String getNewState() {
    	return new BigInteger(130, random).toString(32);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		// Checks if required fields have been properly set.
		if(APP_ID == "" || APP_SECRET == "" || REDIRECT_URL == "") {
			request.setAttribute("error", "You still have to define your Facebook application API keys and URL, please refer to the README.md in the root directory of your project.");
			request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
		} else {

			HttpSession session = request.getSession(true);
			Boolean valid = false;

			if (session.getAttribute("token") != null) {
				// Check if token is valid
				String info = "https://graph.facebook.com/me?access_token=" + (String) session.getAttribute("token");
				URL url = new URL(info);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setDoOutput(true);
			    connection.connect();

				if (connection.getResponseCode() == 200)
					valid = true;
			}

			if (valid) { 
				doPost(request, response);
			} else {	
				if (request.getParameter("state") != null) {
					if (request.getParameter("code") != null) {

						// Access authorized, getting auth token.
						String code = request.getParameter("code");
						String tokenReq = "https://graph.facebook.com/oauth/access_token?client_id=" + APP_ID + "&redirect_uri=" + REDIRECT_URL + "&client_secret=" + APP_SECRET +"&code=" + code;
						
						URL url = new URL(tokenReq);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod("GET");
						connection.setDoOutput(true);
	          			connection.connect();

						if (connection.getResponseCode() == 200) {
							BufferedReader rd  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	          				StringBuffer sb = new StringBuffer();
	          				String line;
	          				while ((line = rd.readLine()) != null)
	              				sb.append(line + '\n');

	          				String body = sb.toString();
							String token = body.split("&")[0].split("=")[1];

							session.setAttribute("token", token);
							doPost(request, response);

						} else {
							request.setAttribute("error", "Failed to retrieve token.");
							request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
						}
					} else {
						request.setAttribute("error", "You denied access to this Facebook application.");
						request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
					}	
				} else {
					// Redirecting to Facebook splash page
					String facebook;
					String state = getNewState();
					facebook = "https://www.facebook.com/dialog/oauth?client_id=" + APP_ID + "&redirect_uri=" + REDIRECT_URL + "&state=" + state;
					session.setAttribute("state", state);
					response.sendRedirect(facebook);
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (request.getSession() != null) {

				HttpSession session = request.getSession();
				Boolean valid = true;

				try {
            		ItemManager manager = ItemManagerFactory.getManager();
					
					if (request.getParameter("remove") != null) {
						String name = request.getParameter("removeName");
                		manager.delete(name);
			
					} else if (request.getParameter("add") != null) {
						String name = request.getParameter("name");
						String comment = request.getParameter("comment");
						if (name != null) {

							String info = "https://graph.facebook.com/me?access_token=" + (String) session.getAttribute("token");
							URL url = new URL(info);
							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
							connection.setRequestMethod("GET");
							connection.setDoOutput(true);
		          			connection.connect();

							if (connection.getResponseCode() == 200) {
								BufferedReader rd  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		          				StringBuffer sb = new StringBuffer();
		          				String line;
		          				while ((line = rd.readLine()) != null)
		              				sb.append(line + '\n');

		          				String body = sb.toString();
		          				JSONObject json = (JSONObject) JSONSerializer.toJSON(body);
		          				String author = json.getString("name");
								manager.add(name, comment, author);

							} else {
								valid = false;
							}
						}
					}

					if (valid) {
						request.setAttribute("items", manager.getItems());
            			request.getRequestDispatcher("jsp/index.jsp").forward(request, response);
					} else {
						request.setAttribute("error", "Your token has expired.");
						request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
					}
				// ItemManager methods can throw HibernateException, so we catch them with a standard error page.
	        	} catch (HibernateException e) {
		            request.setAttribute("error", "Invalid query, please try again.");
		            request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
		        }
		} else {
			request.setAttribute("error", "Your session has expired.");
			request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
		}

	}
}
