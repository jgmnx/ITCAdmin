package com.itc.admin.view;

import com.itc.admin.entity.Line;
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
@ManagedBean(name = "linesView")
@ViewScoped
public class LinesView implements Serializable {

    @EJB
    private com.itc.admin.session.LineFacade m_linesFacade;
    private List<Line> m_lines;
    private List<Line> m_filteredLines;
    private Line m_selectedLine;
    
    @PostConstruct
    public void init() {
        m_lines = m_linesFacade.findAll();
    }

    public List<Line> getLines() {
        return m_lines;
    }
    
    public void setSelectedLine(Line selectedLine) {
        m_selectedLine = selectedLine;
    }
    
    public Line getSelectedLine() {
        return m_selectedLine;
    }

    public void setFilteredLines(List<Line> filteredLines) {
        this.m_filteredLines = filteredLines;
    }

    public List<Line> getFilteredLines() {
        return m_filteredLines;
    }

}
