package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nlp.Tagger;

/**
 * Servlet implementation class RulesServlet
 */
public class RulesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RulesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
	    
	    String action = request.getParameter("action"); 
	    String type = request.getParameter("type"); 

	    Tagger tag = new Tagger();
	    
	    if(action.equals("get")){
	    	out.print(tag.getRulesFile(type));
	    }
	    else if(action.equals("salvar")){
	    	tag.setRulesFile(type, request.getParameter("text"));
	    }
	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		doGet(request, response);
	}

}
