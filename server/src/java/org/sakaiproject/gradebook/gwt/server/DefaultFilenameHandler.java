package org.sakaiproject.gradebook.gwt.server;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;

public class DefaultFilenameHandler implements HttpRequestHandler {

	protected int input = 2048;
	
	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String path = request.getRequestURI();
		InputStream resourceStream = request.getSession().getServletContext().getResourceAsStream(path);
		
		if (resourceStream == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		ServletOutputStream outputStream = response.getOutputStream();
		
		IOException exception = copyRange(resourceStream, outputStream);
		
		resourceStream.close();
		outputStream.close();
	
		if (exception != null)
			throw exception;
	}

	
	protected IOException copyRange(InputStream istream, ServletOutputStream ostream) {

		IOException exception = null;
		byte buffer[] = new byte[input];
		int len = buffer.length;
		while (true) {
			try {
				len = istream.read(buffer);
				if (len == -1)
					break;
				ostream.write(buffer, 0, len);
			} catch (IOException e) {
				exception = e;
				len = -1;
				break;
			}
		}
		return exception;
	}

	
}