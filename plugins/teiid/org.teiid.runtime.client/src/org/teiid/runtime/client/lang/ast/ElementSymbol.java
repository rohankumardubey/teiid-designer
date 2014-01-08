/* Generated By:JJTree: Do not edit this line. ElementSymbol.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.teiid.runtime.client.lang.ast;

import org.teiid.runtime.client.lang.parser.TeiidParser;

@SuppressWarnings( "unused" )
public class ElementSymbol extends Symbol implements SingleElementSymbol, Expression {

    private Class type;
    
    private Object metadataID;

    public ElementSymbol(int id) {
        super(id);
    }

    public ElementSymbol(TeiidParser p, int id) {
        super(p, id);
    }

    /**
     * Get the type of the symbol
     * @return Type of the symbol, may be null before resolution
     */
    public Class getType() {
        return this.type;
    }   
    
    /**
     * Set the type of the symbol
     * @param type New type
     */
    public void setType(Class type) {
        this.type = type;
    }

    /**
     * Get the metadata ID reference
     * @return Metadata ID reference, may be null before resolution
     */
    public Object getMetadataID() {
        return this.metadataID;
    }

    /**
     * Set the metadata ID reference for this element
     * @param metadataID Metadata ID reference
     */
    public void setMetadataID(Object metadataID) {
        this.metadataID = metadataID;
    }

    /** Accept the visitor. **/
    public void jjtAccept(Teiid8ParserVisitor visitor, Object data) {
        visitor.visit(this, data);
    }
}
/* JavaCC - OriginalChecksum=77f5180826dcbc69682360fac6b1d467 (do not edit this line) */
