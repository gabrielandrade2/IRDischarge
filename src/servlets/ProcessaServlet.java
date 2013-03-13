package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nlp.Tagger;

/**
 * Servlet implementation class ProcessaServlet
 */
public class ProcessaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
	    
	    //Obtém parâmetros de configuração
	    String origem = request.getParameter("origem");
	    String tipo = request.getParameter("tipo");
	    String tool = request.getParameter("ferramenta");
	    
	    Tagger tag = new Tagger();
	    
	    //Se foi informa texto
	    if(origem.equals("texto")){
	    	
	    	//TODO: Fazer filtro também para caso input seja arquivo EXCEL
	    	if(tipo.equals("etiquetacao")){
	    		//if(tool.equals("opennlp"))
	    		//	out.print(tag.tagText(request.getParameter("texto"), true));
	    		//else
	    		out.print(tag.tagTextCogroo(request.getParameter("texto"), true));
	    	}
	    	else if(tipo.equals("diagnostico")){
	    		//out.print(tag.testTagRules(request.getParameter("texto"), true));
	    	}
	    	else if(tipo.equals("encaminhamento")){
	    		
	    	}
	    	
	    }
		
	}

}
