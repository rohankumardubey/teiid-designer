/* Generated By:JJTree: Do not edit this line. MultipleElementSymbol.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.teiid.query.sql.symbol;

import java.util.LinkedList;
import java.util.List;
import org.teiid.designer.query.sql.symbol.IMultipleElementSymbol;
import org.teiid.query.parser.LanguageVisitor;
import org.teiid.query.parser.TeiidParser;
import org.teiid.query.parser.TeiidNodeFactory.ASTNodes;
import org.teiid.query.sql.lang.SimpleNode;

/**
 *
 */
public class MultipleElementSymbol extends SimpleNode
    implements Expression, IMultipleElementSymbol<ElementSymbol, LanguageVisitor> {

    private List<ElementSymbol> elementSymbols;

    private GroupSymbol group;

    /**
     * @param p
     * @param id
     */
    public MultipleElementSymbol(TeiidParser p, int id) {
        super(p, id);
    }

    /**
     * @return name
     */
    public String getName() {
        return this.group.getName();   
    }

    /** 
     * @param name New name
     */
    public void setName(String name) {
        this.group = this.parser.createASTNode(ASTNodes.GROUP_SYMBOL);
        this.group.setName(name);
    }

    /**
     * @return null if selecting all groups, otherwise the specific group
     */
    public GroupSymbol getGroup() {
        return group;
    }
    
    /**
     * @param group
     */
    public void setGroup(GroupSymbol group) {
        this.group = group;
    }

    /**
     * Get the element symbols referred to by this multiple element symbol
     * @return List of {@link ElementSymbol}s, may be null
     */
    @Override
    public List<ElementSymbol> getElementSymbols(){
        return this.elementSymbols;
    }

    /**
     * Add an element symbol referenced by this multiple element symbol
     * @param symbol Element symbol referenced by this multiple element symbol
     */
    public void addElementSymbol(ElementSymbol symbol) {
        if(getElementSymbols() == null) { 
            setElementSymbols(new LinkedList<ElementSymbol>());
        }
        getElementSymbols().add(symbol);
    }

    /**
     * Set the {@link ElementSymbol}s that this symbol refers to
     * @param elementSymbols List of {@link ElementSymbol}
     */
    public void setElementSymbols(List<ElementSymbol> elementSymbols){
        this.elementSymbols = elementSymbols;
    }

    @Override
    public Class<?> getType() {
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.elementSymbols == null) ? 0 : this.elementSymbols.hashCode());
        result = prime * result + ((this.group == null) ? 0 : this.group.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        MultipleElementSymbol other = (MultipleElementSymbol)obj;
        if (this.elementSymbols == null) {
            if (other.elementSymbols != null) return false;
        } else if (!this.elementSymbols.equals(other.elementSymbols)) return false;
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
    public MultipleElementSymbol clone() {
        MultipleElementSymbol clone = new MultipleElementSymbol(this.parser, this.id);

        if(getGroup() != null)
            clone.setGroup(getGroup().clone());
        if(getElementSymbols() != null)
            clone.setElementSymbols(cloneList(getElementSymbols()));

        return clone;
    }

}
/* JavaCC - OriginalChecksum=ac341c6477c55c5715d1a766627e0a4c (do not edit this line) */
