package jsp;

import java.io.IOException;
import java.util.List;
import java.lang.StringBuffer;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ItemController
 */
public class ItemController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit(unitName="itemManager")
	private EntityManagerFactory emf;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ItemController() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
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
					em.persist(new Item(name, comment));
					em.getTransaction().commit();
				}
			}

			List<Item> itemList = em.createQuery("SELECT i FROM Item i",
					Item.class).getResultList();
			StringBuffer sb = new StringBuffer();
			for (Item item: itemList) { 
				sb.append("<div class=\"item\">\n<tr><td>");
				sb.append(item.getName());
				sb.append("</td><td>");
				sb.append(item.getComment());
				sb.append("</td></tr>\n</div>\n");
    		}
    		String out = sb.toString();
			request.setAttribute("items", out);
			request.getRequestDispatcher("jsp/index.jsp").forward(request,
					response);

		} finally {

			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
	}

}
