package com.progressSoft.firstsample;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileDownloader extends HttpServlet {

	Path path;

	@Override
	public void init() throws ServletException {
		ServletConfig servletConfig = getServletConfig();
		String documentsPath = servletConfig.getInitParameter("documentsPath");
		path = Paths.get(documentsPath);
		if (Files.notExists(path) || !Files.isDirectory(path))
			throw new ServletException("Specified path is not a directory or does not exist" + path);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pathParam = req.getParameter("path");
		Path pathToBrowse = path;
		if (pathParam != null)
			pathToBrowse = Paths.get(pathParam);
		if (Files.isDirectory(pathToBrowse))
			browseDirectory(req, resp, pathToBrowse);
		else {
			downloadFile(resp, pathToBrowse);

		}
	}

	private void downloadFile(HttpServletResponse resp, Path fileToDownload) throws IOException {
		ServletOutputStream outputStream = resp.getOutputStream();
		ServletContext servletContext = getServletContext();
		String mimeType = servletContext.getMimeType(fileToDownload.toAbsolutePath().toString());
		resp.setContentType(mimeType);
		resp.setContentLengthLong(Files.size(fileToDownload));
		resp.setHeader("Content-Disposition", "attachment; filename=" + fileToDownload.getFileName());
		Files.copy(fileToDownload, outputStream);
	}

	private void browseDirectory(HttpServletRequest req, HttpServletResponse resp, Path pathToBrowse)
			throws IOException {
		PrintWriter writer;
		resp.setContentType("text/html");
		writer = resp.getWriter();
		writer.print("<html><body>");
		writer.print("<table>");
		writer.print("<thead><tr><th>Name</th><th>Size</th></tr></thead>");
		printContact(pathToBrowse, writer, req);
		writer.print("</tbody></table></body></html>");
	}

	private void printContact(Path pathToBrowse, PrintWriter writer, HttpServletRequest req) throws IOException {
		DirectoryStream<Path> contents = Files.newDirectoryStream(pathToBrowse);
		String servletPath = req.getServletPath();
		ServletContext servletContext = req.getServletContext();
		String contextPath = servletContext.getContextPath();
		String fullURL = contextPath + servletPath;
		writer.print("<tbody>");
		contents.forEach(p -> {
			writer.print("<tr><td>");
			writer.print("<a href='" + fullURL + "?path=" + p.toAbsolutePath().toUri().toString().substring(8) + "'>");
			writer.print(p.getFileName());
			writer.print("</a>");
			writer.print("</td><td>");
			try {
				writer.print(Files.size(p));
			} catch (IOException e) {
				writer.print("</td></tr>");
			}
		});
	}
}
