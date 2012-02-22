/**********************************************************************************
 *
 * $Id:$
 *
 ***********************************************************************************
 *
 * Copyright (c) 2008, 2009 The Regents of the University of California
 *
 * Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.gradebook.gwt.sakai;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.sakaiproject.util.ResourceLoader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.sakaiproject.gradebook.gwt.client.AppConstants;
import org.sakaiproject.gradebook.gwt.client.model.Learner;
import org.sakaiproject.gradebook.gwt.client.model.Upload;
import org.sakaiproject.gradebook.gwt.server.ImportExportUtility;
import org.sakaiproject.gradebook.gwt.server.ImportExportUtility.FileType;
import org.sakaiproject.gradebook.gwt.server.OpenController;
import org.sakaiproject.gradebook.gwt.server.model.UploadImpl;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class GradebookImportController extends SimpleFormController implements OpenController {

	private static final Log log = LogFactory.getLog(GradebookImportController.class);

	private Gradebook2ComponentService service;
	private GradebookToolService gbToolService;
	private ImportExportUtility importExportUtility;
	
	private final String CONTENT_TYPE_TEXT_HTML = "text/html";
		
	// Set via IoC
	private ResourceLoader i18n;
	
	public ModelAndView submit(HttpServletRequest request,
			HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		
		
		
		return onSubmit(request, response, command, errors);
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response,
			Object command, BindException errors) throws Exception {

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;

		String gradebookUid = multipartRequest.getParameter(AppConstants.REQUEST_FORM_FIELD_GBUID);
		
		String justStructureCheckBox = multipartRequest.getParameter(AppConstants.IMPORT_PARAM_STRUCTURE);
		
		Boolean importOnlyStructure =  "on".equalsIgnoreCase(justStructureCheckBox);

		for (Iterator<String> fileNameIterator = multipartRequest.getFileNames();fileNameIterator.hasNext();) {
			String fileName = fileNameIterator.next();

			MultipartFile file = multipartRequest.getFile(fileName);
			String origName = file.getOriginalFilename(); 
			Upload importFile = null;
			
			FileType fileType = null;
			if (origName.lastIndexOf(".")>0) {
				fileType = FileType.getTypeFromExtension(origName.substring(origName.lastIndexOf(".")));
			} else {
				importFile = new UploadImpl();
				importFile.setNotes(i18n.getString("noFileExtensionFound"));
				importFile.setErrors(true);
			}

			log.debug("Original Name: " + origName + "; type: " + fileType);
			if (fileType != null) {
				if (fileType.isExcelNative())
				{
					log.debug("Excel file detected"); 
					importFile = importExportUtility.parseImportXLS(service, gradebookUid, file.getInputStream(), origName.toLowerCase(), gbToolService, importOnlyStructure);
				}
				else
				{
					log.debug("Assuming CSV file"); 
					InputStreamReader reader = new InputStreamReader(file.getInputStream());
					importFile = importExportUtility.parseImportCSV(service, gradebookUid, reader, importOnlyStructure);
				}
			}

			PrintWriter writer = response.getWriter();
			response.setContentType(CONTENT_TYPE_TEXT_HTML);

			// NOTE: Only use this during DEV phase
			//saveJsonToFile(importFile, "/tmp/data.json"); 

			if (null == importFile) {
				importFile = new UploadImpl();
				importFile.setErrors(true);
				importFile.setNotes(i18n.getString("unknownExcelFileFormat"));
			} else {
				
				//GRBK-1194
				List<Learner> rows = importFile.getRows();
				List<String> studentIds = new ArrayList<String>();
				StringBuffer msg = null;
				
				boolean dupsFound = false;
				
				if (rows != null) {
					for (Learner student : rows) {
						String id = student.getIdentifier();
						if (null == id)
							continue;
						if (studentIds.contains(id)) {
							dupsFound = true;
							if (null == msg) {
								msg = new StringBuffer(i18n.getString("importDuplicateStudentsFound", 
										"Duplicate rows found in the table. The following Student Id's where duplicated: ")).append(id);
							} else {
								msg.append(",").append(id);
							}
						} else {
							studentIds.add(id);
						}
					}
				
				
					if (dupsFound) {
						importFile.setErrors(true);
						importFile.setNotes(msg.toString());
					}
					
				}
			}
			writer.write(toJson(importFile)); 
			writer.flush();
			writer.close();
		}

		return null;
	}
	
	// NOTE: This is a DEBUG helper class and should not be removed during a dead code sweep 
	private void saveJsonToFile(Upload importFile, String outfile) {

		File f = new File(outfile);
		boolean isDeleted = f.delete();
		
		if(!isDeleted) {
			log.error("Was not able to delete file = " + f.getName());
		}

		PrintWriter out;
		try {
			out = new PrintWriter(f);
			out.write(toJson(importFile, true));
			out.flush();
			out.close(); 
		} catch (FileNotFoundException e) {
			log.warn("Caught exception: " + e, e); 
		} 
	}

	protected String toJson(Object o)
	{
		return toJson(o, false); 
	}
	protected String toJson(Object o, boolean prettyPrint) {
		ObjectMapper mapper = new ObjectMapper();
		if (prettyPrint)
		{
			mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true); 
		}

		StringWriter w = new StringWriter();
		try {
			mapper.writeValue(w, o);
		} catch (Exception e) {
			log.error("Caught an exception serializing to JSON: ", e);
		}
		return w.toString();
	}

	public void initializeBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
		// to actually be able to convert Multipart instance to byte[]
		// we have to register a custom editor
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		// now Spring knows how to handle multipart object and convert them
	}

	
	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		
		initializeBinder(request, binder);
	}

	public Gradebook2ComponentService getService() {
		return service;
	}

	public void setService(Gradebook2ComponentService service) {
		this.service = service;
	}

	public GradebookToolService getGbToolService() {
		return gbToolService;
	}

	public void setGbToolService(GradebookToolService gbToolService) {
		this.gbToolService = gbToolService;
	}

	public void setImportExportUtility(ImportExportUtility importExportUtility) {
		this.importExportUtility = importExportUtility;
	}

	public void setI18n(ResourceLoader i18n) {
		this.i18n = i18n;
	}
}
