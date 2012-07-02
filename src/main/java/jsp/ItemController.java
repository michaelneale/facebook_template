package jsp;

import java.io.IOException;
import java.util.*;
import java.lang.StringBuffer;
import java.security.SecureRandom;
import java.math.BigInteger;
import java.net.URLConnection;
import java.net.URL;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;

import com.facebook.api.*;

public class ItemController  extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	private static final long serialVersionUID = 1L;

	@PersistenceUnit(unitName="itemManager")
	private EntityManagerFactory emf;
	private SecureRandom random = new SecureRandom();

	String APP_ID = "353270288079506";
	String APP_SECRET = "5ca7809e881d119fcf52c2d59aef3141";
	String REDIRECT_URL = "http://facebook.bsavoy.cloudbees.net/";

	public ItemController() {
		super();
	}

	public String getNewState() {
    	return new BigInteger(130, random).toString(32);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("token") != null) {
			doPost(request,response);
		} else {
			if (request.getParameter("state") != null) {
				if (request.getParameter("code") != null) {
					String code = request.getParameter("code");
					String tokenReq = "https://graph.facebook.com/oauth/access_token?client_id=" + APP_ID + "&redirect_uri=" + REDIRECT_URL + "&client_secret=" + APP_SECRET +"&code=" + code;
					URL url = new URL(tokenReq);
					URLConnection connection = url.openConnection();
					connection.connect();
					String token = connection.getHeaderField("access_token");
					request.setAttribute("token", token);
					request.getRequestDispatcher("/").forward(request, response);
				} else {
					request.getRequestDispatcher("jsp/denied.jsp").forward(request, response);
				}	
			} else {
				String facebook;
				facebook = "https://www.facebook.com/dialog/oauth?client_id=" + APP_ID + "&redirect_uri=" + REDIRECT_URL + "&state=" + getNewState();
				response.sendRedirect(facebook);
			}
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

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
							//TODO Get facebook name! If token doesn't work, do a doGet
							String author = "me";
							em.persist(new Item(name, comment, author));
							em.getTransaction().commit();
						}
					}

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

				} finally {
					if (em.getTransaction().isActive())
						em.getTransaction().rollback();
					em.close();
				}
	}
}
