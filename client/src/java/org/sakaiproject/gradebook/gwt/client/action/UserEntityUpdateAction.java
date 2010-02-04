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
package org.sakaiproject.gradebook.gwt.client.action;

import org.sakaiproject.gradebook.gwt.client.gxt.model.EntityModel;
import org.sakaiproject.gradebook.gwt.client.gxt.model.GradeRecordModel;
import org.sakaiproject.gradebook.gwt.client.gxt.model.GradeScaleRecordModel;
import org.sakaiproject.gradebook.gwt.client.gxt.model.GradebookModel;
import org.sakaiproject.gradebook.gwt.client.gxt.model.ItemModel;
import org.sakaiproject.gradebook.gwt.client.gxt.model.LearnerModel;
import org.sakaiproject.gradebook.gwt.client.model.Gradebook;
import org.sakaiproject.gradebook.gwt.client.model.Item;
import org.sakaiproject.gradebook.gwt.client.model.type.ActionType;
import org.sakaiproject.gradebook.gwt.client.model.type.ClassType;
import org.sakaiproject.gradebook.gwt.client.model.type.EntityType;

import com.extjs.gxt.ui.client.data.ModelData;


public class UserEntityUpdateAction<M extends ModelData> extends UserEntityAction<M> {

	private static final long serialVersionUID = 1L;

	private Boolean doRecalculateChildren;

	private boolean isBulkUpdate = false;

	public UserEntityUpdateAction() {
		super(ActionType.UPDATE);
	}

	public UserEntityUpdateAction(ActionType actionType) {
		super(actionType);
	}

	public UserEntityUpdateAction(EntityType entityType, ActionType actionType) {
		super(actionType, entityType);
	}

	public UserEntityUpdateAction(Gradebook gbModel, M model) {
		super(gbModel, ActionType.UPDATE);
		setModel(model);
		this.isBulkUpdate = true;

		if (model instanceof LearnerModel) {
			setEntityType(EntityType.LEARNER);
			setActionType(ActionType.GRADED);
		} else if (model instanceof ItemModel) {
			switch (((Item)model).getItemType()) {
				case ITEM:
					setEntityType(EntityType.ITEM);
					break;
				case CATEGORY:
					setEntityType(EntityType.CATEGORY);
					break;
				case GRADEBOOK:
					setEntityType(EntityType.GRADEBOOK);
					break;
			}
		} else if (model instanceof GradeRecordModel) {
			setEntityType(EntityType.GRADE_RECORD);
		} else if (model instanceof GradebookModel) {
			setEntityType(EntityType.GRADEBOOK);
		//} else if (model instanceof CommentModel) {
		//	setEntityType(EntityType.COMMENT);
		} else if (model instanceof GradeScaleRecordModel) {
			setEntityType(EntityType.GRADE_SCALE);
		}
	}

	public UserEntityUpdateAction(Gradebook gbModel, M model, String key, ClassType classType, Object value, Object startValue) {
		super(gbModel, ActionType.UPDATE);
		setModel(model);
		if (model instanceof EntityModel)
			setEntityName(((EntityModel)model).getDisplayName());
		setKey(key);
		this.classType = classType;

		setValue(value);
		setStartValue(startValue);

		if (model instanceof LearnerModel) {
			setEntityType(EntityType.LEARNER);
			setActionType(ActionType.GRADED);
		} else if (model instanceof ItemModel) {
			setEntityType(EntityType.ITEM);
		} else if (model instanceof GradeRecordModel) {
			setEntityType(EntityType.GRADE_RECORD);
		} else if (model instanceof GradebookModel) {
			setEntityType(EntityType.GRADEBOOK);
		//} else if (model instanceof CommentModel) {
		//	setEntityType(EntityType.COMMENT);
		} else if (model instanceof GradeScaleRecordModel) {
			setEntityType(EntityType.GRADE_SCALE);
		}

	}

	public Boolean getDoRecalculateChildren() {
		return doRecalculateChildren;
	}

	public void setDoRecalculateChildren(Boolean doRecalculateChildren) {
		this.doRecalculateChildren = doRecalculateChildren;
	}

	public String toString() {
		StringBuilder text = new StringBuilder();

		//Object value = getValue();
		//Object startValue = getStartValue();

		text.append(getActionType().getVerb());
		
		//.append(" '").append(value)
		//.append("'");

		//if (startValue != null)
		//	text.append(" from '").append(startValue).append("' ");

		return text.toString();
	}

	public boolean isBulkUpdate() {
		return isBulkUpdate;
	}
}