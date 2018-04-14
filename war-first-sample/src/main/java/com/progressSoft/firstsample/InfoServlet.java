package com.progressSoft.firstsample;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InfoServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		writer.println("<html><body><table>");
		Enumeration<String> names = req.getHeaderNames();
		writer.println("<thead><tr><th>name</th><th>value</th></tr></thead>");
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String headerValue = req.getHeader(name);
			writer.append("<tr>");
			writer.append("<td>").append(name).append("</td>");
			writer.append("<td>").append(headerValue).append("</td>");
			writer.append("</tr>");

		}
	}
}
