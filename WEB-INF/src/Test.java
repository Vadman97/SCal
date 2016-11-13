package main;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

public class Test extends HttpServlet {
  public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
  {
    PrintWriter out = res.getWriter();
    out.println("Hello, world!");

    RequestDispatcher RequetsDispatcherObj = req.getRequestDispatcher("/test.jsp");
    RequetsDispatcherObj.forward(req, res);

    out.close();
  }
}
