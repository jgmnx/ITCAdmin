package com.itc.admin.view;

import com.itc.admin.entity.Category;
import com.itc.admin.entity.Line;
import com.itc.admin.entity.Product;
import com.itc.admin.entity.ProductSpec;
import com.itc.admin.session.CategoryFacade;
import com.itc.admin.session.LineFacade;
import com.itc.admin.session.ProductFacade;
import com.itc.admin.session.ProductSpecFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author jgmnx
 */
@ManagedBean(name = "productSpecsView")
@ViewScoped
public class ProductSpecsView implements Serializable {

    private static final String SELECT_OPTION = JsfUtil.getMessage("product_specs_select_option");
    private static final String CATEGORY_ALL = "Todos";
    private static final String CATEGORY_PROMOS = "Paquetes y promociones";
    
    
    @EJB
    private LineFacade m_lineFacade;
    @EJB
    private CategoryFacade m_categoryFacade;
    @EJB
    private ProductFacade m_productsFacade;
    @EJB
    private ProductSpecFacade m_productSpecsFacade;
    
    private List<Line> m_lines;
    private Map<Integer, List<Category>> m_categoriesByLine;
    private List<Category> m_categories;
    private List<Product> m_products;
    private List<ProductSpec> m_productSpecs;
    
    private Integer m_selectedLineId;
    private Integer m_selectedCategoryId;
    private String m_selectedProductId;
    private List<Integer> m_categoriesAll;
    private List<Integer> m_categoriesPromos;

    @PostConstruct
    public void init() {
        m_lines = m_lineFacade.findAll();
        m_categoriesAll = new ArrayList<Integer>();
        m_categoriesPromos = new ArrayList<Integer>();
        m_categoriesByLine = new HashMap<Integer, List<Category>>();
        
        for(Category category : m_categoryFacade.findAll()) {
            if (m_categoriesByLine.get(category.getLine().getId()) == null) {
                m_categoriesByLine.put(category.getLine().getId(), new ArrayList<Category>());
            }
            m_categoriesByLine.get(category.getLine().getId()).add(category);
            if(CATEGORY_ALL.equals(category.getText())) {
                m_categoriesAll.add(category.getId());
            } else if (CATEGORY_PROMOS.equals(category.getText())) {
                m_categoriesPromos.add(category.getId());
            }
        }
        m_productSpecs = new ArrayList<ProductSpec>();
    }

    public List<Line> getLines() {
        return m_lines;
    }
    
    public List<Category> getCategories() {
        return m_categories;
    }
    
    public List<Product> getProducts() {
        return m_products;
    }
    
    public List<ProductSpec> getProductSpecs() {
        return m_productSpecs;
    }

    public void setSelectedLineId(Integer selectedLineId) {
        m_selectedLineId = selectedLineId;
    }

    public Integer getSelectedLineId() {
        return m_selectedLineId;
    }
    
    public void setSelectedCategoryId(Integer selectedCategoryId) {
        m_selectedCategoryId = selectedCategoryId;
    }

    public Integer getSelectedCategoryId() {
        return m_selectedCategoryId;
    }
    
    public void setSelectedProductId(String selectedProductId) {
        m_selectedProductId = selectedProductId;
    }

    public String getSelectedProductId() {
        return m_selectedProductId;
    }
    
    public void changeLineListener() {
        m_categories = m_categoriesByLine.get(m_selectedLineId);
        System.out.println("");
        m_products = null;
        m_productSpecs = null;
        m_selectedCategoryId = null;
        m_selectedProductId = null;
    }
    
    public void changeCategoryListener() {
        if(m_categoriesAll.contains(m_selectedCategoryId)) {
            m_products = m_productsFacade.findByLine(m_selectedLineId);
        } else if (m_categoriesPromos.contains(m_selectedCategoryId)) {
            m_products = m_productsFacade.findByLineWithPromo(m_selectedLineId);
        } else {
            m_products = m_productsFacade.findByCategory(m_selectedCategoryId);
        }
        m_productSpecs = null;
        m_selectedProductId = null;
    }
    
    public void changeProductListener() {
        m_productSpecs = m_productSpecsFacade.findByProductId(m_selectedProductId);
    }
    
}
