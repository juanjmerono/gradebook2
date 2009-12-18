package org.sakaiproject.gradebook.gwt.client.resource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public interface GradebookResources extends ClientBundle {
	
	@Source("resources.css")
	GradebookCssResource css();
	
	
	@Source("application_edit.png")
	ImageResource application_edit();

	@Source("calculator_edit.png")
	ImageResource calculator_edit();
	
	@Source("calendar.png")
	ImageResource calendar();
	
	@Source("chart_curve.png")
	ImageResource chart_curve();
	
	@Source("commented.gif")
	@ImageOptions(repeatStyle=RepeatStyle.None)
	ImageResource commented();
	
	@Source("failed.gif")
	@ImageOptions(repeatStyle=RepeatStyle.None)
	ImageResource failed();
	
	@Source("folder_add.png")
	ImageResource folder_add();
	
	@Source("folder_delete.png")
	ImageResource folder_delete();
	
	@Source("folder_edit.png")
	ImageResource folder_edit();
	
	@Source("large_loading.gif")
	ImageResource large_loading();
	
	@Source("modified.gif")
	@ImageOptions(repeatStyle=RepeatStyle.None)
	ImageResource modified();
	
	@Source("page_white_get.png")
	ImageResource page_white_get();
	
	@Source("page_white_put.png")
	ImageResource page_white_put();
	
	@Source("table.png")
	ImageResource table();
	
	@Source("table_add.png")
	ImageResource table_add();
	
	@Source("table_delete.png")
	ImageResource table_delete();
	
	@Source("table_edit.png")
	ImageResource table_edit();
}
