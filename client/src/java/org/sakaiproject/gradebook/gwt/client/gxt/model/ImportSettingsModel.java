package org.sakaiproject.gradebook.gwt.client.gxt.model;

import java.util.List;

import org.sakaiproject.gradebook.gwt.client.BusinessLogicCode;
import org.sakaiproject.gradebook.gwt.client.model.ImportSettings;
import org.sakaiproject.gradebook.gwt.client.model.key.UploadKey;

public class ImportSettingsModel extends EntityModel implements ImportSettings {

	
	private static final long serialVersionUID = 43434L;
	private List<BusinessLogicCode> ignoredBusinessRules;

	
	public ImportSettingsModel() {
		super();
		
		setScantron(false);
		setForceOverwriteAssignments(false);
	}
	
	public ImportSettingsModel(EntityModel e) {
		super(e.getOverlay());
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.gradebook.gwt.client.gxt.model.ImportSetting#isScantron()
	 */
	public Boolean isScantron() {
		return get(UploadKey.B_SCNTRN.name());
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.gradebook.gwt.client.gxt.model.ImportSetting#setScantron(boolean)
	 */
	public void setScantron(boolean scantron) {
		set(UploadKey.B_SCNTRN.name(),scantron);
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.gradebook.gwt.client.gxt.model.ImportSetting#isForceOverwriteAssignments()
	 */
	public Boolean isForceOverwriteAssignments() {
		return get(UploadKey.B_OVRWRT.name());
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.gradebook.gwt.client.gxt.model.ImportSetting#setForceOverwriteAssignments(boolean)
	 */
	public void setForceOverwriteAssignments(boolean forceOverwriteAssignments) {
		set(UploadKey.B_OVRWRT.name(),forceOverwriteAssignments);
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.gradebook.gwt.client.gxt.model.ImportSetting#getScantronMaxPoints()
	 */
	public String getScantronMaxPoints() {
		return get(UploadKey.S_MXPNTS.name());
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.gradebook.gwt.client.gxt.model.ImportSetting#setScantronMaxPoints(java.lang.String)
	 */
	public void setScantronMaxPoints(String scantronMaxPoints) {
		set(UploadKey.S_MXPNTS.name(),scantronMaxPoints);
	}

	public List<BusinessLogicCode> getIgnoredBusinessRules() {
		return this.ignoredBusinessRules;
	}

	public void setIgnoredBusinessRules(List<BusinessLogicCode> rules) {
		this.ignoredBusinessRules = rules;
	}

	public Boolean isJustStructure() {
		return get(UploadKey.B_STRUC.name());
	}

	public void setJustStructure(Boolean yes) {
		set(UploadKey.B_STRUC.name(), yes);
		
	}
}
