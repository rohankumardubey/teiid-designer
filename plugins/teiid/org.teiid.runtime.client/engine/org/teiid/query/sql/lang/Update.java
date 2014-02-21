/* Generated By:JJTree: Do not edit this line. Update.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.teiid.query.sql.lang;

import java.util.List;
import org.teiid.designer.query.sql.lang.IUpdate;
import org.teiid.query.parser.LanguageVisitor;
import org.teiid.query.parser.TeiidNodeFactory.ASTNodes;
import org.teiid.query.parser.TeiidParser;
import org.teiid.query.sql.symbol.ElementSymbol;
import org.teiid.query.sql.symbol.Expression;
import org.teiid.query.sql.symbol.GroupSymbol;

/**
 *
 */
public class Update extends ProcedureContainer implements IUpdate<Expression, LanguageVisitor> {

    private SetClauseList changeList;

    /** Identifies the group to be updated. */
    private GroupSymbol group;

    /** optional criteria defining which row get updated. */
    private Criteria criteria;

    /**
     * @param p
     * @param id
     */
    public Update(TeiidParser p, int id) {
        super(p, id);
    }

    /**
     * Return type of command.
     * @return TYPE_UPDATE
     */
    @Override
    public int getType() {
        return TYPE_UPDATE;
    }

    /**
     * @return the changeList
     */
    public SetClauseList getChangeList() {
        return changeList;
    }

    /**
     * Add change to change list - a change is represented by a CompareCriteria
     * internally but can be added here as an element and an expression
     * @param id Element to be changed
     * @param value Expression, often a value, being set
     */
    public void addChange(ElementSymbol id, Expression value) {
        if (changeList == null)
            changeList = parser.createASTNode(ASTNodes.SET_CLAUSE_LIST);

        SetClause setClause = parser.createASTNode(ASTNodes.SET_CLAUSE);
        setClause.setSymbol(id);
        setClause.setValue(value);
        changeList.addClause(setClause);
    }

    /**
     * @param changeList the changeList to set
     */
    public void setChangeList(SetClauseList changeList) {
        this.changeList = changeList;
    }

    /**
     * Returns the group being updated
     * @return Group being updated
     */
    public GroupSymbol getGroup() {
        return group;
    }

    /**
     * Set the group being updated
     * @param group Group being updated
     */
    public void setGroup(GroupSymbol group) {
        this.group = group;
    }

    /**
     * Returns the criteria object for this command, may be null
     * @return Criteria, may be null
     */
    public Criteria getCriteria() {
        return this.criteria;
    }

    /**
     * Set the criteria for this Update command
     * @param criteria Criteria to be associated with this command
     */
    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public List<Expression> getProjectedSymbols(){
        return getUpdateCommandSymbol();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.changeList == null) ? 0 : this.changeList.hashCode());
        result = prime * result + ((this.criteria == null) ? 0 : this.criteria.hashCode());
        result = prime * result + ((this.group == null) ? 0 : this.group.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        Update other = (Update)obj;
        if (this.changeList == null) {
            if (other.changeList != null) return false;
        } else if (!this.changeList.equals(other.changeList)) return false;
        if (this.criteria == null) {
            if (other.criteria != null) return false;
        } else if (!this.criteria.equals(other.criteria)) return false;
        if (this.group == null) {
            if (other.group != null) return false;
        } else if (!this.group.equals(other.group)) return false;
        return true;
    }

    /** Accept the visitor. **/
    @Override
    public void acceptVisitor(LanguageVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Update clone() {
        Update clone = new Update(this.parser, this.id);

        if(getCriteria() != null)
            clone.setCriteria(getCriteria().clone());
        if(getGroup() != null)
            clone.setGroup(getGroup().clone());
        if(getChangeList() != null)
            clone.setChangeList(getChangeList().clone());
        if(getSourceHint() != null)
            clone.setSourceHint(getSourceHint());
        if(getOption() != null)
            clone.setOption(getOption().clone());

        copyMetadataState(clone);
        return clone;
    }

}
/* JavaCC - OriginalChecksum=ce325d62b951bcd63dec74a5c48397e7 (do not edit this line) */
