package webservice;

import database.DatabaseManager;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BalancedNewsService
 */
@WebServlet("/BalancedNewsService")
public class BalancedNewsService extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private RequestHandler handler=new RequestHandler();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BalancedNewsService() {
        super();
        // TODO Auto-generated constructor stub
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
                String cmd=request.getParameter("cmd");
                //String arg=request.getParameter("arg");

                //String reply=handler.getResponse(cmd, arg);
                String reply=handler.getResponse(cmd, request);
                PrintWriter pw=response.getWriter();

                pw.write(reply);
                pw.flush();
                pw.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
