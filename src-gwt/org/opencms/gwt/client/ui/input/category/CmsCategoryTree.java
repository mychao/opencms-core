/*
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) Alkacon Software GmbH (http://www.alkacon.com)
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

package org.opencms.gwt.client.ui.input.category;

import org.opencms.ade.galleries.client.Messages;
import org.opencms.gwt.client.ui.CmsList;
import org.opencms.gwt.client.ui.CmsPushButton;
import org.opencms.gwt.client.ui.CmsScrollPanel;
import org.opencms.gwt.client.ui.CmsSimpleListItem;
import org.opencms.gwt.client.ui.I_CmsButton.ButtonStyle;
import org.opencms.gwt.client.ui.I_CmsListItem;
import org.opencms.gwt.client.ui.css.I_CmsImageBundle;
import org.opencms.gwt.client.ui.css.I_CmsInputLayoutBundle;
import org.opencms.gwt.client.ui.input.CmsCheckBox;
import org.opencms.gwt.client.ui.input.CmsSelectBox;
import org.opencms.gwt.client.ui.input.CmsTextBox;
import org.opencms.gwt.client.ui.input.category.css.I_CmsLayoutBundle;
import org.opencms.gwt.client.ui.input.category.css.I_CmsLayoutBundle.I_CmsGalleryDialogCss;
import org.opencms.gwt.client.ui.tree.CmsTreeItem;
import org.opencms.gwt.shared.CmsCategoryTreeEntry;
import org.opencms.util.CmsStringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Builds the category tree.<p>
 * */
public class CmsCategoryTree extends Composite implements HasValueChangeHandlers<List<String>> {

    /** Sorting parameters. */
    public enum SortParams implements IsSerializable {

        /** Date last modified ascending. */
        dateLastModified_asc,

        /** Date last modified descending. */
        dateLastModified_desc,

        /** Resource path ascending sorting. */
        path_asc,

        /** Resource path descending sorting.*/
        path_desc,

        /** Title ascending sorting. */
        title_asc,

        /** Title descending sorting. */
        title_desc,

        /** Tree.*/
        tree,

        /** Resource type ascending sorting. */
        type_asc,

        /** Resource type descending sorting. */
        type_desc;
    }

    /**
     * @see com.google.gwt.uibinder.client.UiBinder
     */
    interface I_CmsCategoryTreeUiBinder extends UiBinder<Widget, CmsCategoryTree> {
        // GWT interface, nothing to do here
    }

    /**
     * Inner class for select box handler.<p>
     */
    private class CategoryValueChangeHandler implements ValueChangeHandler<String> {

        /**
         * Default Constructor.<p>
         */
        public CategoryValueChangeHandler() {

            // TODO: Auto-generated constructor stub

        }

        /**
         * Will be triggered if the value in the select box changes.<p>
         * 
         * @see com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
         */
        public void onValueChange(ValueChangeEvent<String> event) {

            cancelQuickFilterTimer();
            if (event.getSource() == m_sortSelectBox) {

                List<CmsDataValue> categories = new ArrayList<CmsDataValue>();
                Iterator<CmsDataValue> it = m_categories.values().iterator();
                while (it.hasNext()) {
                    categories.add(it.next());
                }
                m_event = event;
                SortParams sort = SortParams.valueOf(event.getValue());
                switch (sort) {
                    case tree:
                        m_listView = false;
                        updateContentTree(m_resultList, m_selectedCategories);
                        break;
                    default:
                        m_listView = true;
                        categories = getFilteredCategories(hasQuickFilter()
                        ? m_quickSearch.getFormValueAsString()
                        : null);
                        Collections.sort(categories, new CmsCategoryComperator(event.getValue()));
                        updateContentList(categories, m_selectedCategories);
                        break;
                }
            }
            if ((event.getSource() == m_quickSearch)) {
                if (!m_listView) {
                    m_listView = true;
                    m_sortSelectBox.setFormValueAsString(SortParams.title_asc.name());
                }
                if (hasQuickFilter()) {

                    if ((CmsStringUtil.isEmptyOrWhitespaceOnly(event.getValue()) || (event.getValue().length() >= 3))) {
                        // only act if filter length is at least 3 characters or empty
                        scheduleQuickFilterTimer();
                    }
                } else {
                    checkQuickSearchStatus();
                }
            }
        }

    }

    /**
     * Inner class for check box handler.<p>
     */
    private class CheckBoxValueChangeHandler implements ValueChangeHandler<Boolean> {

        /** Path of the TreeItem. */
        private CmsTreeItem m_item;

        /**
         * Default constructor.<p>
         * @param item The CmsTreeItem of this check box
         */
        public CheckBoxValueChangeHandler(CmsTreeItem item) {

            m_item = item;

        }

        /**
         * Is triggered if an check box is selected or deselected.<p> 
         * 
         * @param event The event that is triggered
         */
        public void onValueChange(ValueChangeEvent<Boolean> event) {

            boolean select = event.getValue().booleanValue();
            if (select) {
                if (m_isSingleSelection) {
                    deselectAll(m_item.getId());
                    m_singleResult = m_item.getId();
                } else {
                    Iterator<Widget> it = m_scrollList.iterator();
                    while (it.hasNext()) {
                        selectAllParents((CmsTreeItem)it.next(), m_item.getId());

                    }
                }
            } else {
                if (m_isSingleSelection) {
                    deselectAll("");
                } else {
                    deselect(m_item, "");
                    deselectParent(m_item);
                }
            }
            fireValueChange();
        }

    }

    /**
     * Inner class for check box handler.<p>
     */
    private class DataValueClickHander implements ClickHandler {

        /** The TreeItem. */
        private CmsTreeItem m_item;

        /** Constructor to set the right CmsTreeItem for this handler.<p>
         * 
         * @param item the CmsTreeItem of this Handler
         */
        public DataValueClickHander(CmsTreeItem item) {

            m_item = item;
        }

        /**
         * Is triggered if the DataValue widget is clicked.<p>
         * If its check box was selected the click will deselect this box otherwise it will select it.
         * 
         * @param event The event that is triggered
         * */
        public void onClick(ClickEvent event) {

            if (!m_item.getCheckBox().isChecked()) {
                if (m_isSingleSelection) {
                    deselectAll(m_item.getId());
                    m_singleResult = m_item.getId();
                } else {
                    Iterator<Widget> it = m_scrollList.iterator();
                    while (it.hasNext()) {
                        selectAllParents((CmsTreeItem)it.next(), m_item.getId());

                    }
                }
            } else {
                if (m_isSingleSelection) {
                    deselectAll("");
                } else {
                    deselect(m_item, "");
                    deselectParent(m_item);
                }
            }
            m_item.getCheckBox().setChecked(!m_item.getCheckBox().isChecked());
            fireValueChange();
        }

    }

    /** The css bundle used for this widget. */
    protected static final I_CmsGalleryDialogCss DIALOG_CSS = I_CmsLayoutBundle.INSTANCE.galleryDialogCss();

    /** Text metrics key. */
    private static final String TM_GALLERY_SORT = "gallerySort";

    /** The ui-binder instance for this class. */
    private static I_CmsCategoryTreeUiBinder uiBinder = GWT.create(I_CmsCategoryTreeUiBinder.class);

    /** Map of categories. */
    protected Map<String, CmsDataValue> m_categories;

    /** Result string for single selection. */
    protected String m_singleResult = "";

    /** List of categories. */
    protected CmsList<? extends I_CmsListItem> m_scrollList;

    /** List of categories selected from the server. */
    protected List<CmsCategoryTreeEntry> m_resultList;

    /** List of all selected categories. */
    protected List<String> m_selectedCategories;

    /** The quick search box. */
    protected CmsTextBox m_quickSearch;

    /** The quick search button. */
    protected CmsPushButton m_searchButton;

    /** The filtering delay. */
    private static final int FILTER_DELAY = 100;

    /** The scroll panel. */
    @UiField
    CmsScrollPanel m_list;

    /** The main panel. */
    @UiField
    FlowPanel m_tab;

    /** The option panel. */
    @UiField
    protected FlowPanel m_options;

    /** A label for displaying additional information about the tab. */
    protected HasText m_infoLabel;

    /** The select box to change the sort order. */
    protected CmsSelectBox m_sortSelectBox;

    /** Vale to store the widget mode. True means the single selection. */
    protected boolean m_isSingleSelection;

    /** Vale to store the view mode. True means the list view. */
    protected boolean m_listView;

    /** The event at the moment. */
    protected ValueChangeEvent<String> m_event;

    /** The quick filter timer. */
    private Timer m_filterTimer;

    /**
     * Default Constructor.<p>
     */
    public CmsCategoryTree() {

        uiBinder.createAndBindUi(this);
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Constructor to collect all categories and build a view tree.<p> 
     * @param selectedCategories A list of all selected categories
     * @param height The height of this widget 
     * @param isSingleValue Sets the modes of this widget
     * @param resultList 
     * */
    public CmsCategoryTree(
        List<String> selectedCategories,
        int height,
        boolean isSingleValue,
        List<CmsCategoryTreeEntry> resultList) {

        this();
        m_isSingleSelection = isSingleValue;
        addStyleName(I_CmsInputLayoutBundle.INSTANCE.inputCss().categoryItem());
        m_list.addStyleName(I_CmsInputLayoutBundle.INSTANCE.inputCss().categoryScrollPanel());
        m_selectedCategories = selectedCategories;
        Iterator<String> it = selectedCategories.iterator();
        while (it.hasNext()) {
            m_singleResult = it.next();
        }
        m_scrollList = createScrollList();
        m_list.setHeight(height + "px");
        m_resultList = resultList;
        m_list.add(m_scrollList);
        updateContentTree(resultList, m_selectedCategories);
        init();
    }

    /**
     * Adds children item to the category tree and select the categories.<p>
     * 
     * @param parent the parent item 
     * @param children the list of children
     * @param selectedCategories the list of categories to select
     */
    public void addChildren(CmsTreeItem parent, List<CmsCategoryTreeEntry> children, List<String> selectedCategories) {

        if (children != null) {
            for (CmsCategoryTreeEntry child : children) {
                // set the category tree item and add to parent tree item
                CmsTreeItem treeItem = buildTreeItem(child, selectedCategories);

                if ((selectedCategories != null) && selectedCategories.contains(child.getPath())) {
                    parent.setOpen(true);
                    openParents(parent);

                }
                if (m_isSingleSelection) {
                    if (treeItem.getCheckBox().isChecked()) {
                        parent.getCheckBox().setChecked(false);
                    }
                }
                parent.addChild(treeItem);
                addChildren(treeItem, child.getChildren(), selectedCategories);
            }
        }
    }

    /**
     * @see com.google.gwt.event.logical.shared.HasValueChangeHandlers#addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)
     */
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<String>> handler) {

        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * Represents a value change event.<p>
     */
    public void fireValueChange() {

        ValueChangeEvent.fire(this, getAllSelected());
    }

    /**
     * Returns a list of all selected values.<p>
     * 
     * @return a list of selected values
     */
    public List<String> getAllSelected() {

        List<String> result = new ArrayList<String>();
        Iterator<Widget> it = m_scrollList.iterator();
        while (it.hasNext()) {
            CmsTreeItem test = (CmsTreeItem)it.next();
            if (test.getCheckBox().isChecked()) {
                result.add(test.getId());
                selectedChildren(result, test);
            }
        }
        return result;
    }

    /**
     * Returns the scrollpanel of this widget.<p>
     * 
     * @return CmsScrollPanel the scrollpanel of this widget
     * */
    public CmsScrollPanel getScrollPanel() {

        return m_list;
    }

    /**
     * Returns the last selected value.<p>
     * 
     * @return the last selected value
     */
    public List<String> getSelected() {

        List<String> result = new ArrayList<String>();
        result.add(m_singleResult);
        return result;
    }

    /**
     * Goes up the tree and opens the parents of the item.<p>
     * 
     * @param item the child item to start from
     */
    public void openParents(CmsTreeItem item) {

        if (item != null) {
            item.setOpen(true);
            openParents(item.getParentItem());
        }
    }

    /**
     * Shows the tab list is empty label.<p>
     */
    public void showIsEmptyLabel() {

        CmsSimpleListItem item = new CmsSimpleListItem();
        Label isEmptyLabel = new Label(Messages.get().key(Messages.GUI_TAB_CATEGORIES_IS_EMPTY_0));
        item.add(isEmptyLabel);
        m_scrollList.add(item);
    }

    /**
     * Updates the content of the categories list.<p>
     * 
     * @param categoriesBeans the updates list of categories tree item beans
     * @param selectedCategories the categories to select in the list by update
     */
    public void updateContentList(List<CmsDataValue> categoriesBeans, List<String> selectedCategories) {

        m_scrollList.clearList();
        // clearList();
        if (m_categories == null) {
            m_categories = new HashMap<String, CmsDataValue>();
        }
        if ((categoriesBeans != null) && !categoriesBeans.isEmpty()) {
            for (CmsDataValue dataValue : categoriesBeans) {

                m_categories.put(dataValue.getParameter(1), dataValue);
                // the checkbox
                CmsCheckBox checkBox = new CmsCheckBox();
                boolean isPartofPath = false;
                Iterator<String> it = selectedCategories.iterator();
                while (it.hasNext()) {
                    String path = it.next();
                    if (path.contains(dataValue.getParameter(1))) {
                        isPartofPath = true;
                    }
                }
                if (isPartofPath) {
                    checkBox.setChecked(true);
                }

                // set the category list item and add to list 
                CmsTreeItem listItem = new CmsTreeItem(false, checkBox, dataValue);
                checkBox.addValueChangeHandler(new CheckBoxValueChangeHandler(listItem));
                listItem.setSmallView(true);
                listItem.setId(dataValue.getParameter(1));
                addWidgetToList(listItem);
                CmsScrollPanel scrollparent = (CmsScrollPanel)m_scrollList.getParent();
                scrollparent.onResize();
            }
        } else {
            showIsEmptyLabel();
        }
    }

    /**
     * Updates the content of the categories tree.<p>
     * 
     * @param treeEntries the root category entry
     * @param selectedCategories the categories to select after update
     */
    public void updateContentTree(List<CmsCategoryTreeEntry> treeEntries, List<String> selectedCategories) {

        m_scrollList.clearList();
        if (m_categories == null) {
            m_categories = new HashMap<String, CmsDataValue>();
        }
        if ((treeEntries != null) && !treeEntries.isEmpty()) {
            // add the first level and children
            for (CmsCategoryTreeEntry category : treeEntries) {
                // set the category tree item and add to list 
                CmsTreeItem treeItem = buildTreeItem(category, selectedCategories);
                addWidgetToList(treeItem);
                addChildren(treeItem, category.getChildren(), selectedCategories);
                treeItem.setOpen(true);
            }
        } else {
            showIsEmptyLabel();
        }

    }

    /**
     * Add a list item widget to the list panel.<p>
     * 
     * @param listItem the list item to add
     */
    protected void addWidgetToList(Widget listItem) {

        m_scrollList.add(listItem);
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            public void execute() {

                m_list.onResize();
            }
        });
    }

    /**
     * Cancels the quick filter timer.<p>
     */
    protected void cancelQuickFilterTimer() {

        if (m_filterTimer != null) {
            m_filterTimer.cancel();
        }
    }

    /**
     * Checks the quick search input and enables/disables the search button accordingly.<p>
     */
    protected void checkQuickSearchStatus() {

        if ((m_quickSearch != null) && (m_searchButton != null)) {
            if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(m_quickSearch.getFormValueAsString())) {
                m_searchButton.enable();
            } else {
                m_searchButton.disable("Enter a search query");
            }
        }
    }

    /**
     * Creates the quick search/finder box.<p>
     */
    protected void createQuickBox() {

        m_quickSearch = new CmsTextBox();
        // m_quickFilter.setVisible(hasQuickFilter());
        m_quickSearch.getElement().getStyle().setFloat(Float.RIGHT);
        m_quickSearch.getTextBoxContainer().getElement().getStyle().setHeight(18, Unit.PX);
        m_quickSearch.getTextBox().getElement().getStyle().setMarginTop(2, Unit.PX);

        m_quickSearch.setTriggerChangeOnKeyPress(true);
        m_quickSearch.setGhostValue(Messages.get().key(Messages.GUI_QUICK_FINDER_SEARCH_0), true);
        m_quickSearch.setGhostModeClear(true);
        m_options.insert(m_quickSearch, 0);
        m_searchButton = new CmsPushButton();
        m_searchButton.setImageClass(I_CmsImageBundle.INSTANCE.style().searchIcon());
        m_searchButton.setButtonStyle(ButtonStyle.TRANSPARENT, null);
        m_searchButton.getElement().getStyle().setFloat(Style.Float.RIGHT);
        m_options.insert(m_searchButton, 0);
        m_quickSearch.addValueChangeHandler(new CategoryValueChangeHandler());

        m_filterTimer = new Timer() {

            @Override
            public void run() {

                quickSearch();

            }
        };
        m_searchButton.setTitle(Messages.get().key(Messages.GUI_QUICK_FINDER_SEARCH_0));

        m_searchButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent arg0) {

                quickSearch();
            }
        });
        m_searchButton.setTitle(Messages.get().key(Messages.GUI_TAB_SEARCH_SEARCH_EXISTING_0));
    }

    /**
     * Creates the list which should contain the list items of the tab.<p>
     * 
     * @return the newly created list widget 
     */
    protected CmsList<? extends I_CmsListItem> createScrollList() {

        return new CmsList<I_CmsListItem>();
    }

    /**
     * Helper class to deselect all values.<p>
     * @param item The CmsTreeItem that should be deselected
     * @param ignorItem The Item that should not be deselected
     */
    protected void deselect(CmsTreeItem item, String ignorItem) {

        if (ignorItem.equals("")) {
            item.getCheckBox().setChecked(false);
            m_selectedCategories.remove(item.getId());
        } else {
            if (!item.getId().equals(ignorItem)) {
                item.getCheckBox().setChecked(false);
                m_selectedCategories.remove(item.getId());
            }
        }
        Iterator<Widget> it = item.getChildren().iterator();
        while (it.hasNext()) {
            deselect((CmsTreeItem)it.next(), ignorItem);
        }
    }

    /**
     * Deselect all Values.<p>
     * 
     * @param ignorItem The Item that should not be deselected
     */
    protected void deselectAll(String ignorItem) {

        // clear list of all selected categories
        m_selectedCategories.clear();
        // if there is a value to ignore
        if (!ignorItem.equals("")) {
            if (!m_selectedCategories.contains(ignorItem)) {
                m_selectedCategories.add(ignorItem);
            }
        }
        // iterate about all values in scrolling list
        Iterator<Widget> it = m_scrollList.iterator();
        while (it.hasNext()) {
            CmsTreeItem item = (CmsTreeItem)it.next();
            deselect(item, ignorItem);
        }
    }

    /**
     * Deselect the parent if there are no other values selected.
     * 
     * @param item The item that should be deselected
     */
    protected void deselectParent(CmsTreeItem item) {

        // get parent item from given item
        CmsTreeItem parent = item.getParentItem();
        if (parent != null) {
            boolean deselect = false;
            // check if there are other children selected
            Iterator<Widget> it = parent.getChildren().iterator();
            while (it.hasNext()) {
                deselect = hasSelectedChildren((CmsTreeItem)it.next());
                if (deselect) {
                    return;
                }
            }
            // no children are selected so deselect this parent item
            parent.getCheckBox().setChecked(false);
            // check it with its parent parent
            deselectParent(parent);
        }
    }

    /**
     * Gets the filtered list of categories.<p>
     * 
     * @param filter the search string to use for filtering 
     * 
     * @return the filtered category beans 
     */
    protected List<CmsDataValue> getFilteredCategories(String filter) {

        List<CmsDataValue> result = new ArrayList<CmsDataValue>();
        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(filter)) {
            result = new ArrayList<CmsDataValue>();
            for (CmsDataValue category : m_categories.values()) {
                if (category.matchesFilter(filter, 0, 1)) {
                    result.add(category);
                }
            }
        } else {
            Iterator<CmsDataValue> it = m_categories.values().iterator();
            while (it.hasNext()) {
                result.add(it.next());
            }

        }
        return result;
    }

    /**
     * List of all sort parameters.<p>
     * 
     * @return List of all sort parameters
     */
    protected LinkedHashMap<String, String> getSortList() {

        LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();
        list.put(SortParams.tree.name(), "TREE");
        list.put(SortParams.title_asc.name(), "Titel ASC");
        list.put(SortParams.title_desc.name(), "Titel DECS");
        list.put(SortParams.path_asc.name(), "Path ASC");
        list.put(SortParams.path_desc.name(), "Path DECS");

        return list;
    }

    /**
     * Returns true if this widget hat an QuickFilter.<p>
     * 
     * @return true if this widget hat an QuickFilter
     */
    protected boolean hasQuickFilter() {

        // allow filter if not in tree mode
        return SortParams.tree != SortParams.valueOf(m_sortSelectBox.getFormValueAsString());
    }

    /**
     * Call after all handlers have been set.<p>
     */
    protected void init() {

        LinkedHashMap<String, String> sortList = getSortList();
        if (sortList != null) {
            // generate the sort select box
            m_sortSelectBox = new CmsSelectBox(sortList);
            // add the right handler
            m_sortSelectBox.addValueChangeHandler(new CategoryValueChangeHandler());
            // style the select box
            m_sortSelectBox.getElement().getStyle().setWidth(200, Unit.PX);
            m_sortSelectBox.truncate(TM_GALLERY_SORT, 200);
            // add it to the right panel
            m_options.add(m_sortSelectBox);
            // create the box label
            Label infoLabel = new Label();
            infoLabel.setStyleName(DIALOG_CSS.infoLabel());
            m_infoLabel = infoLabel;
            // add it to the right panel
            m_options.insert(infoLabel, 0);
            // create quick search box
            createQuickBox();
        }

    }

    /**
     * Sets the search query an selects the result tab.<p>
     */
    protected void quickSearch() {

        List<CmsDataValue> categories = new ArrayList<CmsDataValue>();
        if ((m_quickSearch != null)) {
            categories = getFilteredCategories(hasQuickFilter() ? m_quickSearch.getFormValueAsString() : null);
            Collections.sort(categories, new CmsCategoryComperator(m_event.getValue()));
            updateContentList(categories, m_selectedCategories);
        }
    }

    /**
     * Removes the quick search/finder box.<p>
     */
    protected void removeQuickBox() {

        if (m_quickSearch != null) {
            m_quickSearch.removeFromParent();
            m_quickSearch = null;
        }
        if (m_searchButton != null) {
            m_searchButton.removeFromParent();
            m_searchButton = null;
        }
    }

    /**
     * Schedules the quick filter action.<p>
     */
    protected void scheduleQuickFilterTimer() {

        m_filterTimer.schedule(FILTER_DELAY);
    }

    /**
     * Select a singel value and all parents.<p>
     * @param item 
     * @param path The path of the Item that should be selected
     * @return true if this CmsTreeItem is selected or one of it�s children
     */
    protected boolean selectAllParents(CmsTreeItem item, String path) {

        // if this is a list view
        if (m_listView) {
            // check if the path contains the item path
            if (path.contains(item.getId())) {
                // if it do check it
                item.getCheckBox().setChecked(true);
                // add it to the list of selected categories
                if (!m_selectedCategories.contains(item.getId())) {
                    m_selectedCategories.add(item.getId());
                }
                return true;
            }

        }
        // if this is a tree view
        else {
            // check if the pach contains the item path
            if (item.getId().equals(path)) {
                // if it do check it
                item.getCheckBox().setChecked(true);
                // add it to the list of selected categories
                if (!m_selectedCategories.contains(item.getId())) {
                    m_selectedCategories.add(item.getId());
                }
                return true;
            } else {
                // iterate about all children of this item
                Iterator<Widget> it = item.getChildren().iterator();
                while (it.hasNext()) {
                    if (selectAllParents((CmsTreeItem)it.next(), path)) {
                        item.getCheckBox().setChecked(true);
                        if (!m_selectedCategories.contains(item.getId())) {
                            m_selectedCategories.add(item.getId());
                        }
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Select a singel value and all parents.<p>
     * @param item 
     * @param path The path of the Item that should be selected
     * @param result 
     * @return true if this CmsTreeItem is selected or one of it�s children
     */
    protected boolean selectAllParents(CmsTreeItem item, String path, List<String> result) {

        // if this is a list view
        if (m_listView) {
            // check if the path contains the item path
            if (path.contains(item.getId())) {
                // add it to the list of selected categories
                if (!result.contains(item.getId())) {
                    result.add(item.getId());
                }
                return true;
            }

        }
        // if this is a tree view
        else {
            // check if the pach contains the item path
            if (item.getId().equals(path)) {
                // add it to the list of selected categories
                if (!result.contains(item.getId())) {
                    result.add(item.getId());
                }
                return true;
            } else {
                // iterate about all children of this item
                Iterator<Widget> it = item.getChildren().iterator();
                while (it.hasNext()) {
                    if (selectAllParents((CmsTreeItem)it.next(), path, result)) {
                        if (!result.contains(item.getId())) {
                            result.add(item.getId());
                        }
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Builds a tree item for the given category.<p>
     * 
     * @param category the category
     * @param selectedCategories the selected categories
     * 
     * @return the tree item widget
     */
    private CmsTreeItem buildTreeItem(CmsCategoryTreeEntry category, List<String> selectedCategories) {

        // generate the widget that should be shown in the list
        CmsDataValue dataValue = new CmsDataValue(
            600,
            3,
            I_CmsImageBundle.INSTANCE.icons().deleteIconActive().getName(),
            category.getTitle(),
            category.getPath());
        // add it to the list of all categories
        m_categories.put(category.getPath(), dataValue);
        // create the check box for this item 
        CmsCheckBox checkBox = new CmsCheckBox();
        // if it has to be selected, select it 
        boolean isPartofPath = false;
        Iterator<String> it = selectedCategories.iterator();
        while (it.hasNext()) {
            String path = it.next();
            if (path.contains(category.getPath())) {
                isPartofPath = true;
            }
        }
        if (isPartofPath) {
            checkBox.setChecked(true);
        }
        // bild the CmsTreeItem out of the widget and the check box
        CmsTreeItem treeItem = new CmsTreeItem(true, checkBox, dataValue);
        // abb the handler to the check box
        dataValue.addDomHandler(new DataValueClickHander(treeItem), ClickEvent.getType());

        checkBox.addValueChangeHandler(new CheckBoxValueChangeHandler(treeItem));
        // set the right style for the small view
        treeItem.setSmallView(true);
        treeItem.setId(category.getPath());
        return treeItem;
    }

    /**
     * Return true if the given CmsTreeItem or it�s children is selected.<p>
     * @param item The CmsTreeItem to start the check
     * @return true if the given CmsTreeItem or it�s children is selected
     */
    private boolean hasSelectedChildren(CmsTreeItem item) {

        boolean test = false;
        // if this item is selected stop searching and return true
        if (item.getCheckBox().isChecked()) {
            return true;
        } else {
            //iterate about all children of this item
            Iterator<Widget> it = item.getChildren().iterator();
            while (it.hasNext()) {
                // test if one children is selected
                if (hasSelectedChildren((CmsTreeItem)it.next())) {
                    // save this value if a children is selected
                    test = true;
                }

            }
        }

        return test;
    }

    /**
     * Helper function to selected all selected values.<p>
     * @param result list of all selected values
     * @param item the parent where the children have to be checked
     * */
    private void selectedChildren(List<String> result, CmsTreeItem item) {

        Iterator<Widget> it = item.getChildren().iterator();
        while (it.hasNext()) {
            CmsTreeItem test = (CmsTreeItem)it.next();
            if (test.getCheckBox().isChecked()) {
                result.add(test.getId());
                selectedChildren(result, test);
            }
        }

    }

}
