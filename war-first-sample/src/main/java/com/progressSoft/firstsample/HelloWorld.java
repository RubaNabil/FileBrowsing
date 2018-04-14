package com.progressSoft.firstsample;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/hello" })
public class HelloWorld extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		writer.println("Hello ");
		writer.println(name);
		writer.flush();

	}
}
