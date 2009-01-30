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
/*
 * This file is a direct copy of com.extjs.gxt.ui.client.widget.grid.GroupSummaryView @ GXT version 1.2.2, which
 * is licensed under the GPLv3.0 license and packaged here according to the FLOSS exemption included
 * under license/ext-gwt-floss-exception.txt
 * 
 * <start license>
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 * </end license>
 * 
 * The only code modification here is the change in the class extended.
 * 
 * We were forced to take a copy of the class, since we have extended the doRender method of GridView,
 * and so in order for the grouping doRender to work properly, we had to insert our own BaseCustomGridView
 * above this class.
 * 
 */
package org.sakaiproject.gradebook.gwt.client.custom.widget.grid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.DomHelper;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableSectionElement;

public class CustomGroupSummaryView extends CustomGroupingView {
	
	 protected Template rowTpl, cellTpl;

	  /**
	   * Returns the summary node element.
	   * 
	   * @param g the group element
	   * @return the summary node
	   */
	  public El getSummaryNode(Element g) {
	    if (g != null) {
	      return fly(g).down(".x-grid3-summary-row");
	    }
	    return null;
	  }

	  /**
	   * Returns true if summaries are visible.
	   * 
	   * @return true for visible
	   */
	  public boolean isSummaryVisible() {
	    return !grid.el().hasStyleName("x-grid-hide-summary");
	  }

	  /**
	   * Toggles the summary information visibility.
	   * 
	   * @param visible true for visible, false to hide
	   */
	  public void toggleSummaries(boolean visible) {
	    El el = grid.el();
	    if (el != null) {
	      if (visible) {
	        el.removeStyleName("x-grid-hide-summary");
	      } else {
	        el.addStyleName("x-grid-hide-summary");
	      }
	    }
	  }

	  protected Map<String, Object> calculate(List<ModelData> models, List<ColumnData> cs) {
	    Map<String, Object> data = new HashMap<String, Object>();

	    for (int j = 0, jlen = models.size(); j < jlen; j++) {
	      ModelData m = models.get(j);
	      for (int i = 0, len = cs.size(); i < len; i++) {
	        ColumnData c = cs.get(i);
	        SummaryColumnConfig cf = (SummaryColumnConfig) cm.getColumn(i);
	        if (cf.getSummaryType() != null) {
	          data.put(c.name, cf.getSummaryType().render(data.get(c.name), m, c.name, data));
	        }
	      }
	    }
	    return data;
	  }

	  protected void doAllWidths(List<String> ws, String tw) {
	    NodeList gs = getGroups();
	    for (int i = 0, len = gs.getLength(); i < len; i++) {
	      Element s = gs.getItem(i).getChildNodes().getItem(2).cast();
	      s.getStyle().setProperty("width", tw);
	      if (s.getFirstChild() == null) return;
	      s.getFirstChildElement().getStyle().setProperty("width", tw);
	      TableSectionElement tse = s.getFirstChildElement().cast();
	      NodeList<Element> cells = (NodeList) tse.getRows().getItem(0).getChildNodes();
	      for (int j = 0, wlen = ws.size(); j < wlen; j++) {
	        cells.getItem(j).getStyle().setProperty("width", ws.get(j));
	      }
	    }
	  }

	  @Override
	  protected void doGroupEnd(StringBuilder buf, GroupColumnData g, List<ColumnData> cs, int colCount) {
	    Map data = calculate(g.models, cs);
	    buf.append("</div>");
	    buf.append(renderSummary(data, cs));
	    buf.append("</div>");
	  }

	  protected void doWidth(int col, String w, String tw) {
	    if (!enableGrouping) return;
	    NodeList gs = getGroups();
	    for (int i = 0, len = gs.getLength(); i < len; i++) {
	      Element s = gs.getItem(i).getChildNodes().getItem(2).cast();
	      s.getStyle().setProperty("width", tw);
	      s.getFirstChildElement().getStyle().setProperty("width", tw);

	      TableSectionElement tse = s.getFirstChildElement().cast();
	      Element e = tse.getRows().getItem(0).getChildNodes().getItem(col).cast();
	      e.getStyle().setProperty("width", w);
	    }
	  }

	  @Override
	  protected void initTemplates() {
	    super.initTemplates();

	    if (rowTpl == null) {
	      StringBuffer sb = new StringBuffer();
	      sb.append("<div class='x-grid3-summary-row' style='{tstyle}'>");
	      sb.append("<table class='x-grid3-summary-table' border='0' cellspacing='0' cellpadding='0' style='{tstyle}'>");
	      sb.append("<tbody><tr>{cells}</tr></tbody>");
	      sb.append("</table></div>");
	      rowTpl = new Template(sb.toString());
	      rowTpl.compile();
	    }

	    if (cellTpl == null) {
	      StringBuffer sb = new StringBuffer();
	      sb.append("<td class='x-grid3-col x-grid3-cell x-grid3-td-{id} {css}' style='{style}'>");
	      sb.append("<div class='x-grid3-cell-inner x-grid3-col-{id}' unselectable='on'>{value}</div>");
	      sb.append("</td>");
	      cellTpl = new Template(sb.toString());
	      cellTpl.compile();
	    }
	  }

	  @Override
	  protected void onRemove(ListStore ds, ModelData m, int index, boolean isUpdate) {
	    super.onRemove(ds, m, index, isUpdate);
	    if (!isUpdate) {
	      refreshSummaryById(m.<String> get("_groupId"));
	    }
	  }

	  @Override
	  protected void onUpdate(ListStore store, ModelData model) {
	    super.onUpdate(store, model);
	    refreshSummaryById(model.<String> get("_groupId"));
	  }

	  protected void refreshSummaryById(String id) {
	    Element g = XDOM.getElementById(id);
	    if (g == null) {
	      return;
	    }
	    List models = grid.getStore().findModels("_groupId", id);
	    List<ColumnData> cs = getColumnData();
	    Map data = calculate(models, cs);
	    String markup = renderSummary(data, cs);
	    El existing = getSummaryNode(g);
	    if (existing != null) {
	      g.removeChild(existing.dom);
	    }
	    DomHelper.append((com.google.gwt.user.client.Element) g, markup);
	  }

	  protected String renderSummary(Map<String, Double> data, List<ColumnData> cs) {
	    StringBuilder buf = new StringBuilder();
	    int last = cs.size() - 1;
	    for (int i = 0, len = cs.size(); i < len; i++) {
	      ColumnData c = cs.get(i);
	      SummaryColumnConfig cf = (SummaryColumnConfig) cm.getColumn(i);
	      Params p = new Params();
	      p.set("id", c.id);
	      p.set("style", c.style);
	      String css = i == 0 ? "x-grid3-cell-first " : (i == last ? "x-grid3-cell-last " : "");
	      p.set("css", css);
	      if (cf.getSummaryFormat() != null) {
	        p.set("value", cf.getSummaryFormat().format(data.get(c.name)));
	      } else if (cf.getSummaryRenderer() != null) {
	        p.set("value", cf.getSummaryRenderer().render(data.get(c.name), data));
	      } else {
	        p.set("value", data.get(c.name));
	      }
	      buf.append(cellTpl.applyTemplate(p));

	    }
	    Params rp = new Params();
	    rp.set("tstyle", "width:" + getTotalWidth() + ";");
	    rp.set("cells", buf.toString());

	    return rowTpl.applyTemplate(rp);
	  }

	  @Override
	  protected void templateOnAllColumnWidthsUpdated(List<String> ws, String tw) {
	    super.templateOnAllColumnWidthsUpdated(ws, tw);
	    doAllWidths(ws, tw);
	  }

	  @Override
	  protected void templateOnColumnHiddenUpdated(int col, boolean hidden, String tw) {
	    if (!enableGrouping) return;
	    NodeList gs = getGroups();
	    String display = hidden ? "none" : "";
	    for (int i = 0, len = gs.getLength(); i < len; i++) {
	      Element s = gs.getItem(i).getChildNodes().getItem(2).cast();
	      s.getStyle().setProperty("width", tw);
	      s.getFirstChildElement().getStyle().setProperty("width", tw);
	      TableSectionElement tse = s.getFirstChildElement().cast();
	      Element e = tse.getRows().getItem(0).getChildNodes().getItem(col).cast();
	      e.getStyle().setProperty("display", display);
	    }
	  }

	  @Override
	  protected void templateOnColumnWidthUpdated(int col, String w, String tw) {
	    super.templateOnColumnWidthUpdated(col, w, tw);
	    doWidth(col, w, tw);
	  }
}
