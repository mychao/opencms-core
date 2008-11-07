/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/widgets/CmsVfsImageWidgetConfiguration.java,v $
 * Date   : $Date: 2008/11/07 16:02:17 $
 * Version: $Revision: 1.2 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) 2002 - 2008 Alkacon Software GmbH (http://www.alkacon.com)
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
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.widgets;

import org.opencms.file.CmsObject;
import org.opencms.json.JSONArray;
import org.opencms.json.JSONException;
import org.opencms.json.JSONObject;
import org.opencms.util.CmsMacroResolver;
import org.opencms.util.CmsStringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration options for the VFS image widget.<p>
 * 
 * The configuration options are read from the configuration String of the widget.<p>
 *
 * @author Andreas Zahner 
 * 
 * @version $Revision: 1.2 $ 
 * 
 * @since 7.0.6 
 */
public class CmsVfsImageWidgetConfiguration {

    /** Configuration key name for the class configuration. */
    public static final String CONFIG_KEY_CLASS = "class";

    /** Configuration key name for the formatnames configuration. */
    public static final String CONFIG_KEY_FORMATNAMES = "formatnames";

    /** Configuration key name for the formatvalues configuration. */
    public static final String CONFIG_KEY_FORMATVALUES = "formatvalues";

    /** Configuration key name for the scaleparams configuration. */
    public static final String CONFIG_KEY_SCALEPARAMS = "scaleparams";

    /** Configuration key name for the startup configuration. */
    public static final String CONFIG_KEY_STARTUP = "startup";

    /** Configuration key name for the type configuration. */
    public static final String CONFIG_KEY_TYPE = "type";

    /** Configuration key name for the usedescription configuration. */
    public static final String CONFIG_KEY_USEDESCRIPTION = "usedescription";

    /** Configuration key name for the useformat configuration. */
    public static final String CONFIG_KEY_USEFORMAT = "useformat";

    /** Configuration value name for a dynamic configuration. */
    public static final String CONFIG_VALUE_DYNAMIC = "dynamic";

    /** The type "category" for the initial image list to load. */
    public static final String TYPE_CATEGORY = "category";

    /** The type "gallery" for the initial image list to load. */
    public static final String TYPE_GALLERY = "gallery";

    /** The optional class name for generating dynamic configurations, must implement {@link I_CmsImageWidgetDynamicConfiguration}. */
    private String m_className;

    /** The list of image format values matching the options for the format select box. */
    private List m_formatValues;

    /** The scale parameters to apply to a scaled image (e.g. quality, type). */
    private String m_scaleParams;

    /** The list of select options for the format select box, must contain {@link CmsSelectWidgetOption} objects. */
    private List m_selectFormat;

    /** The select options for the format select box as String. */
    private String m_selectFormatString;

    /** The flag if the description field should be shown. */
    private boolean m_showDescription;

    /** The flag if the format select box should be shown. */
    private boolean m_showFormat;

    /** The required information for the initial image list to load. */
    private String m_startup;

    /** The type of the initial image list to load, either gallery or category. */
    private String m_type;

    /**
     * Generates an initialized configuration for the image widget using the given configuration string.<p>
     * 
     * @param cms an initialized instance of a CmsObject
     * @param widgetDialog the dialog where the widget is used on
     * @param param the widget parameter to generate the widget for
     * @param configuration the widget configuration string
     */
    public CmsVfsImageWidgetConfiguration(
        CmsObject cms,
        I_CmsWidgetDialog widgetDialog,
        I_CmsWidgetParameter param,
        String configuration) {

        init(cms, widgetDialog, param, configuration);
    }

    /**
     * Returns the optional class name for generating dynamic configurations, must implement {@link I_CmsImageWidgetDynamicConfiguration}.<p>
     * 
     * @return the optional class name for generating dynamic configurations
     */
    public String getClassName() {

        return m_className;
    }

    /**
     * Returns the list of image format values matching the options for the format select box.<p>
     * 
     * @return the list of image format values matching the options for the format select box
     */
    public List getFormatValues() {

        return m_formatValues;
    }

    /**
     * Returns the scale parameters to apply to a scaled image (e.g. quality, type).<p>
     * 
     * @return scale the parameters to apply to a scaled image
     */
    public String getScaleParams() {

        return m_scaleParams;
    }

    /**
     * Returns the list of select options for the format select box, must contain {@link CmsSelectWidgetOption} objects.<p>
     * 
     * @return the list of select options for the format select box
     */
    public List getSelectFormat() {

        return m_selectFormat;
    }

    /**
     * Returns the select options for the format select box as String.<p>
     * 
     * The String has the following structure <code>format name 1:localized name 1|format name 2:localized name 2|...</code>.<p>
     * 
     * @return the select options for the format select box
     */
    public String getSelectFormatString() {

        return m_selectFormatString;
    }

    /**
     * Returns the required information for the initial image list to load.<p>
     * 
     * If a gallery should be shown, the path to the gallery must be specified,
     * for a category the category path.<p>
     * 
     * @return the required information for the initial image list to load
     */
    public String getStartup() {

        return m_startup;
    }

    /**
     * Returns the type of the initial image list to load, either gallery or category.<p>
     * 
     * @return the type of the initial image list to load
     */
    public String getType() {

        return m_type;
    }

    /**
     * Returns if the description field should be shown.<p>
     * 
     * @return true if the description field should be shown, otherwise false
     */
    public boolean isShowDescription() {

        return m_showDescription;
    }

    /**
     * Returns if the format select box should be shown.<p>
     * 
     * @return true if the format select box should be shown, otherwise false
     */
    public boolean isShowFormat() {

        return m_showFormat;
    }

    /**
     * Initializes the widget configuration using the given configuration string.<p>
     * 
     * @param cms an initialized instance of a CmsObject
     * @param widgetDialog the dialog where the widget is used on
     * @param param the widget parameter to generate the widget for
     * @param configuration the widget configuration string
     */
    protected void init(CmsObject cms, I_CmsWidgetDialog widgetDialog, I_CmsWidgetParameter param, String configuration) {

        if (configuration == null) {
            // no configuration String found, return
            return;
        }
        configuration = CmsMacroResolver.resolveMacros(configuration, cms, widgetDialog.getMessages());
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj = new JSONObject(configuration);
        } catch (JSONException e) {
            // TODO: error handling
            return;
        }
        // determine the class name that fills in values dynamically
        setClassName(jsonObj.optString(CONFIG_KEY_CLASS, null));
        I_CmsImageWidgetDynamicConfiguration dynConf = null;
        if (getClassName() != null) {
            try {
                dynConf = (I_CmsImageWidgetDynamicConfiguration)Class.forName(getClassName()).newInstance();
            } catch (Exception e) {
                // TODO: error handling 
            }
        }
        // determine if the description field should be shown
        setShowDescription(jsonObj.optBoolean(CONFIG_KEY_USEDESCRIPTION));
        // determine if the format select box should be shown
        setShowFormat(jsonObj.optBoolean(CONFIG_KEY_USEFORMAT));
        if (isShowFormat()) {
            // only parse options if the format select box should be shown
            String optionsStr = (String)jsonObj.opt(CONFIG_KEY_FORMATNAMES);
            setSelectFormatString(optionsStr);
            setSelectFormat(CmsSelectWidgetOption.parseOptions(optionsStr));
            // get the corresponding format values as well
            JSONArray formatValues = jsonObj.optJSONArray(CONFIG_KEY_FORMATVALUES);
            if (formatValues != null) {
                List formatValueList = new ArrayList(formatValues.length());
                for (int i = 0; i < formatValues.length(); i++) {
                    formatValueList.add(formatValues.optString(i));
                }
                setFormatValues(formatValueList);
            }
            if (dynConf != null) {
                setFormatValues(dynConf.getFormatValues(cms, widgetDialog, param, getSelectFormat(), getFormatValues()));
            }
        }
        // determine the initial image list settings
        setType(jsonObj.optString(CONFIG_KEY_TYPE));
        if ((CONFIG_VALUE_DYNAMIC.equals(getType()) || CmsStringUtil.isEmpty(getType())) && dynConf != null) {
            setType(dynConf.getType(cms, widgetDialog, param));
        }
        setStartup(jsonObj.optString(CONFIG_KEY_STARTUP));
        if ((CONFIG_VALUE_DYNAMIC.equals(getStartup()) || CmsStringUtil.isEmpty(getStartup())) && dynConf != null) {
            setStartup(dynConf.getStartup(cms, widgetDialog, param));
        }
        // determine the scale parameters
        setScaleParams(jsonObj.optString(CONFIG_KEY_SCALEPARAMS));
    }

    /**
     * Sets the optional class name for generating dynamic configurations, must implement {@link I_CmsImageWidgetDynamicConfiguration}.<p>
     * 
     * @param className the optional class name for generating dynamic configurations
     */
    private void setClassName(String className) {

        m_className = className;
    }

    /**
     * Sets the list of image format values matching the options for the format select box.<p>
     * 
     * @param formatValues the list of image format values matching the options for the format select box
     */
    private void setFormatValues(List formatValues) {

        m_formatValues = formatValues;
    }

    /**
     * Sets the scale parameters to apply to a scaled image (e.g. quality, type).<p>
     * 
     * @param scaleParams the scale parameters to apply to a scaled image
     */
    private void setScaleParams(String scaleParams) {

        m_scaleParams = scaleParams;
    }

    /**
     * Sets the list of select options for the format select box, must contain {@link CmsSelectWidgetOption} objects.<p>
     * 
     * @param selectFormat the list of select options for the format select box
     */
    private void setSelectFormat(List selectFormat) {

        m_selectFormat = selectFormat;
    }

    /**
     * Sets the select options for the format select box as String.<p>
     * 
     * @param formatString the select options for the format select box as String
     */
    private void setSelectFormatString(String formatString) {

        m_selectFormatString = formatString;
    }

    /**
     * Sets if the description field should be shown.<p>
     * 
     * @param showDescription true if the description field should be shown, otherwise false
     */
    private void setShowDescription(boolean showDescription) {

        m_showDescription = showDescription;
    }

    /**
     * Sets if the format select box should be shown.<p>
     * 
     * @param showFormat true if the format select box should be shown, otherwise false
     */
    private void setShowFormat(boolean showFormat) {

        m_showFormat = showFormat;
    }

    /**
     * Sets the required information for the initial image list to load.<p>
     * 
     * If a gallery should be shown, the path to the gallery must be specified,
     * for a category the category path.<p>
     * 
     * @param startup the required information for the initial image list to load
     */
    private void setStartup(String startup) {

        m_startup = startup;
    }

    /**
     * Sets the type of the initial image list to load, either gallery or category.<p>
     * 
     * @param type the type of the initial image list to load
     */
    private void setType(String type) {

        m_type = type;
    }

}