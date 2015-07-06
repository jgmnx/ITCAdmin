package com.itc.admin.view;

import com.itc.admin.entity.Category;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author jgmnx
 */
@ManagedBean(name = "categoriesView")
@ViewScoped
public class CategoriesView implements Serializable {

    @EJB
    private com.itc.admin.session.CategoryFacade m_categoriesFacade;
    private List<Category> m_categories;
    private List<Category> m_filteredCategories;
    private Category m_selectedCategory;
    
    @PostConstruct
    public void init() {
        m_categories = m_categoriesFacade.findAll();
    }

    public List<Category> getCategories() {
        return m_categories;
    }
    
    public void setSelectedCategory(Category selectedCategory) {
        m_selectedCategory = selectedCategory;
    }
    
    public Category getSelectedCategory() {
        return m_selectedCategory;
    }

    public void setFilteredCategories(List<Category> filteredCategories) {
        this.m_filteredCategories = filteredCategories;
    }

    public List<Category> getFilteredCategories() {
        return m_filteredCategories;
    }

}
