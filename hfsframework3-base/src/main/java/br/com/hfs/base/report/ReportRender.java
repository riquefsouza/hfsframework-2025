package br.com.hfs.base.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletResponse;

public class ReportRender implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The log. */
	//@Autowired
	private Logger log = LoggerFactory.getLogger(ReportRender.class);

	/** The context. */
	@Autowired
	private FacesContext context;
	
	public void render(final byte[] content, final ReportTypeEnum typeReport, String fileName,
			boolean forceDownload) {
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

		log.info("Rendering to file " + fileName + ".");

		if (typeReport.equals(ReportTypeEnum.HTML)){
			fileName = fileName.replaceAll(".html", ".zip");
		}
		
		try {
			response.setContentType(typeReport.getTypeContent());
			response.setContentLength(content.length);

			String forcarDownloadComando = forceDownload ? "attachment; " : "";
			response.setHeader("Content-Disposition", forcarDownloadComando + "filename=\"" + fileName + "\"");

			log.info("Writing the file " + fileName + " in the response.");
			response.getOutputStream().write(content, 0, content.length);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			log.warn("Error generating the report.", e);
			context.addMessage(null, new FacesMessage(e.getMessage(), e.getMessage()));
		}
		context.responseComplete();
	}
	
	public void render(final byte[] content, final ReportTypeEnum typeReport, final String fileName) {
		render(content, typeReport, fileName, false);
	}
	
	public void render(final InputStream stream, final ReportTypeEnum typeReport, final String fileName,
			boolean forceDownload) {
		log.info("Rendering the file " + fileName + ".");
		try {
			render(IOUtils.toByteArray(stream), typeReport, fileName, forceDownload);
		} catch (IOException e) {
			log.warn("Error generating the report.", e);
			context.addMessage(null, new FacesMessage(e.getMessage(), e.getMessage()));
		}
	}
	
	public void render(final InputStream stream, final ReportTypeEnum typeReport, final String fileName) {
		render(stream, typeReport, fileName, false);
	}
	
	public void render(File file, ReportTypeEnum typeReport, String fileName, boolean forceDownload) {
		log.info("Rendering to file " + fileName + ".");
		try {
			render(new FileInputStream(file), typeReport, fileName, forceDownload);
		} catch (FileNotFoundException e) {
			log.warn("Error generating the report.", e);
			context.addMessage(null, new FacesMessage(e.getMessage(), e.getMessage()));
		}
	}
	
	public void render(File file, ReportTypeEnum typeReport, String fileName) {
		render(file, typeReport, fileName, false);
	}
	
}
