/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/workplace/tools/widgetdemo/Attic/CmsAdminWidgetDemo7.java,v $
 * Date   : $Date: 2005/05/13 13:35:38 $
 * Version: $Revision: 1.2 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (C) 2002 - 2005 Alkacon Software (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.workplace.tools.widgetdemo;

import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsContextInfo;
import org.opencms.scheduler.CmsScheduledJobInfo;
import org.opencms.workplace.CmsWidgetDialog;
import org.opencms.workplace.CmsWorkplaceSettings;
import org.opencms.workplace.xmlwidgets.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * A basic example and proof-of-concept on how to use OpenCms widgets within a custom build form
 * without XML contents.<p>
 * 
 * @author Alexander Kandzior (a.kandzior@alkacon.com)
 * 
 * @version $Revision: 1.2 $
 * @since 5.9.1
 */
public class CmsAdminWidgetDemo7 extends CmsWidgetDialog {

    /** Value for the action: save the settings. */
    public static final int ACTION_SAVE = 300;

    /** Request parameter value for the action: save the dialog. */
    public static final String DIALOG_SAVE = "save";

    /** The dialog type. */
    public static final String DIALOG_TYPE = "widgetdemo4";

    /** The OpenCms context info object used for the job info. */
    CmsContextInfo m_contextInfo;

    /** The job info object that is edited on this dialog. */
    CmsScheduledJobInfo m_jobInfo;

    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsAdminWidgetDemo7(CmsJspActionElement jsp) {

        super(jsp);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsAdminWidgetDemo7(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * Builds the HTML for the demo7 form.<p>
     * 
     * @return the HTML for the demo7 form
     */
    public String buildDemo7Form() {

        StringBuffer result = new StringBuffer(1024);

        try {

            // create the dialog HTML
            result.append(createDialogHtml());

        } catch (Throwable t) {
            // TODO: Error handling
        }
        return result.toString();
    }
    
    /**
     * Creates the dialog HTML for all occurences of one widget parameter.<p>  
     * 
     * @param base the widget parameter base
     * @return the dialog HTML for one widget parameter6
     */
    protected String createDialogRowHtml(CmsWidgetParameter base) {

        StringBuffer result = new StringBuffer(256);

        List sequence = (List)getParameters().get(base.getName());
        int count = sequence.size();
        
        // check if value is optional or multiple
        boolean addValue = false;
        if (count < base.getMaxOccurs()) {
            addValue = true;
        }
        boolean removeValue = false;
        if (count > base.getMinOccurs()) {
            removeValue = true;
        }
        
        // check if value is present
        boolean disabledElement = false;
        if (count < 1) {
            // no parameter with the value present, but also not optional: use base as parameter
            sequence = new ArrayList();
            sequence.add(base);
            count = 1;
            if (base.getMinOccurs() == 0) {
                disabledElement = true;    
            }
        }

        // loop through multiple elements
        for (int j = 0; j < count; j++) {

            // get the parameter and the widget
            CmsWidgetParameter p = (CmsWidgetParameter)sequence.get(j);
            I_CmsXmlWidget widget = p.getWidget();

            // create label and help bubble cells
            result.append("<tr>");
            result.append("<td class=\"xmlLabel");
            if (disabledElement) {
                // element is disabled, mark it with css
                result.append("Disabled");
            }
            result.append("\">");
            result.append(key(A_CmsXmlWidget.getLabelKey(p), p.getName()));
            if (count > 1) {
                result.append(" [").append(p.getIndex() + 1).append("]");
            }
            result.append(": </td>");
            if (p.getIndex() == 0) {
                // show help bubble only on first element of each content definition 
                result.append(widget.getHelpBubble(getCms(), this, p));
            } else {
                // create empty cell for all following elements 
                result.append(dialogHorizontalSpacer(16));
            }

            // append individual widget html cell if element is enabled
            if (!disabledElement) {
                // this is a simple type, display widget
                result.append(widget.getDialogWidget(getCms(), this, p));
            } else {
                // disabled element, show message for optional element
                result.append("<td class=\"xmlTdDisabled maxwidth\">");
                result.append(key("editor.xmlcontent.optionalelement"));
                result.append("</td>");
            }

            // append add and remove element buttons if required
            result.append(dialogHorizontalSpacer(5));
            result.append("<td>");
            if (addValue || removeValue) {
                result.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr>");
                 
                if (!addValue) {
                    result.append(dialogHorizontalSpacer(24));
                } else {
                    result.append("<td><table class=\"editorbuttonbackground\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr>");
                    result.append(buildAddElement(base.getName(), p.getIndex(), addValue));
                }
                
                if (removeValue) {
                    if (!addValue) {
                        result.append("<td><table class=\"editorbuttonbackground\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr>");
                    }
                    result.append(buildRemoveElement(base.getName(), p.getIndex(), removeValue));
                }
                
                result.append("</tr></table></td>");
                
                result.append("</tr></table>");
            }
            result.append("</td>");
            // close row
            result.append("</tr>\n");
        }

        return result.toString();
    }

    /**
     * Creates the list of widgets for this dialog.<p>
     */
    protected void defineWidgets() {

        m_jobInfo = new CmsScheduledJobInfo();
        m_contextInfo = new CmsContextInfo();        

        addWidget(new CmsWidgetParameter("stringwidget", new CmsXmlStringWidget(), 0, 5));
        addWidget(new CmsWidgetParameter("textwidget", new CmsXmlTextareaWidget(), 0, 5));
        // Please note: Boolean widget sequences are currently not supported 
        addWidget(new CmsWidgetParameter("boolwidget", new CmsXmlBooleanWidget()));
        addWidget(new CmsWidgetParameter("vfsfilewidget", new CmsXmlVfsFileWidget(), 0, 5));
        addWidget(new CmsWidgetParameter("imagegalwidget", new CmsXmlImageGalleryWidget(), 0, 5));
        addWidget(new CmsWidgetParameter("downgalwidget", new CmsXmlDownloadGalleryWidget(), 0, 5));
        addWidget(new CmsWidgetParameter("htmlgalwidget", new CmsXmlHtmlGalleryWidget(), 0, 5));
        addWidget(new CmsWidgetParameter("tablegalwidget", new CmsXmlTableGalleryWidget(), 0, 5));
        addWidget(new CmsWidgetParameter("extgalwidget", new CmsXmlLinkGalleryWidget(), 0, 5));
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#initWorkplaceRequestValues(org.opencms.workplace.CmsWorkplaceSettings, javax.servlet.http.HttpServletRequest)
     */
    protected void initWorkplaceRequestValues(CmsWorkplaceSettings settings, HttpServletRequest request) {

        // set the dialog type
        setParamDialogtype(DIALOG_TYPE);

        // fill the parameter values in the get/set methods
        fillParamValues(request);

        // fill the widget map
        defineWidgets();
        fillWidgetValues(request);

        // set the action for the JSP switch 
        if (DIALOG_SAVE.equals(getParamAction())) {
            // ok button pressed
            setAction(ACTION_SAVE);
            List errors = commitWidgetValues();
            if (errors.size() > 0) {
                Iterator i = errors.iterator();
                while (i.hasNext()) {
                    Exception e = (Exception)i.next();
                    System.err.println(e.getMessage());
                    if (e.getCause() != null) {
                        System.err.println("Cause: " + e.getCause().getMessage());
                    }
                }
                setAction(ACTION_DEFAULT);
            }
        } else if (DIALOG_OK.equals(getParamAction())) {
            // ok button pressed
            setAction(ACTION_CANCEL);
        } else if (DIALOG_CANCEL.equals(getParamAction())) {
            // cancel button pressed
            setAction(ACTION_CANCEL);
        } else if (EDITOR_ACTION_ELEMENT_ADD.equals(getParamAction())) {
            setAction(ACTION_ELEMENT_ADD);
            actionToggleElement();
            setAction(ACTION_DEFAULT);
        } else if (EDITOR_ACTION_ELEMENT_REMOVE.equals(getParamAction())) {
            setAction(ACTION_ELEMENT_REMOVE);
            actionToggleElement();
            setAction(ACTION_DEFAULT);
        } else {
            // set the default action               
            setAction(ACTION_DEFAULT);
        }
    }
}
