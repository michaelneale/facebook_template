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
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ItemController  extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	private static final long serialVersionUID = 1L;

	@PersistenceUnit(unitName="itemManager")
	private EntityManagerFactory emf;
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

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		Boolean valid = false;

		// If token is valid
		if (session.getValue("token") != null) {
			String info = "https://graph.facebook.com/me?access_token=" + session.getValue("token");
			// Check if token is valid, if so, valid = true
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
					// Access was denied to the application
					request.setAttribute("error", "You denied access to this Facebook application.");
					request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
				}	
			} else {
				// Redirecting to Facebook splash page
				String facebook;
				facebook = "https://www.facebook.com/dialog/oauth?client_id=" + APP_ID + "&redirect_uri=" + REDIRECT_URL + "&state=" + getNewState();
				response.sendRedirect(facebook);
			}
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (request.getSession() != null) {

				HttpSession session = request.getSession();
				Boolean valid = true;

				if (emf == null)
					emf = Persistence.createEntityManagerFactory("itemManager");
				EntityManager em = emf.createEntityManager();

				try {
					if (request.getParameter("remove") != null) {
						String name = request.getParameter("removeName");
						if (name != null) {
							em.getTransaction().begin();
    						Item item = (Item) em.find(Item.class, name);
    						em.remove(item);
    						em.getTransaction().commit();   
						}
			
					} else if (request.getParameter("add") != null) {
						String name = request.getParameter("name");
						String comment = request.getParameter("comment");
						if (name != null) {

							em.getTransaction().begin();
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

								em.persist(new Item(name, comment, author));
								em.getTransaction().commit();
							} else {
								valid = false;
							}
						}
					}

					if (valid) {
						List<Item> itemList = em.createQuery("SELECT i FROM Item i", Item.class).getResultList();
						StringBuffer sb = new StringBuffer();
						for (Item item: itemList) { 
							sb.append("<div class=\"item\">\n<tr><td>");
							sb.append(item.getName());
							sb.append("</td><td>");
							sb.append(item.getComment());
							sb.append("</td><td>");
							sb.append(item.getAuthor());
							sb.append("</td></tr>\n</div>\n");
			    		}

			    		String out = sb.toString();
						request.setAttribute("items", out);
						request.getRequestDispatcher("jsp/index.jsp").forward(request, response);
					} else {
						request.setAttribute("error", "Your token has expired.");
						request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
					}
				} catch (PersistenceException e) {
					request.setAttribute("error", "The data you entered is invalid, please make sure you did not use duplicate names.");
					request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
				} catch (IllegalArgumentException e) {
					request.setAttribute("error", "Cannot delete an item that doesn't exist.");
					request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
				} finally {
					if (em.getTransaction().isActive())
						em.getTransaction().rollback();
					em.close();
				}
		} else {
			request.setAttribute("error", "Your session has expired.");
			request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
		}

	}
}
