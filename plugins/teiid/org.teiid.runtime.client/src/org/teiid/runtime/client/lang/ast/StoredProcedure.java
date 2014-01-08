/* Generated By:JJTree: Do not edit this line. StoredProcedure.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.teiid.runtime.client.lang.ast;

import java.util.Map;
import java.util.TreeMap;
import org.teiid.client.metadata.ParameterInfo;
import org.teiid.runtime.client.lang.SPParameter;
import org.teiid.runtime.client.lang.parser.TeiidParser;

public class StoredProcedure extends Command {

    /** Used as parameters */
    private Map<Integer, SPParameter> mapOfParameters = new TreeMap<Integer, SPParameter>();

    /** Used to reference result set parameter if there is any */
    private Integer resultSetParameterKey;

    private boolean isCallableStatement;

    private boolean calledWithReturn;

    //whether parameters should be displayed in traditional indexed
    //manor, or as named parameters
    private boolean displayNamedParameters;

    private boolean isProcedureRelational;

    private String procedureName;

    public StoredProcedure(int id) {
        super(id);
    }

    public StoredProcedure(TeiidParser p, int id) {
        super(p, id);
    }

    /**
     * Get this stored procedure's name
     *
     * @return procedureName the stored procedure's name
     */
     public String getProcedureName(){
         return this.procedureName;
     }

    /**
    * Set this stored procedure's name
    *
    * @param procedureName the stored procedure's name
    */
    public void setProcedureName(String procedureName){
        this.procedureName = procedureName;
    }

    public boolean isCallableStatement() {
        return isCallableStatement;
    }

    public void setCallableStatement(boolean isCallableStatement) {
        this.isCallableStatement = isCallableStatement;
    }

    private SPParameter getResultSetParameter(){
        if (this.resultSetParameterKey != null){
            return mapOfParameters.get(resultSetParameterKey);
        }
        return null;
    }

    public SPParameter getParameter(int index){
        return mapOfParameters.get(index);
    }

    public int getParameterCount() {
        return mapOfParameters.size();
    }

    /**
    * Set a stored procedure's parameter
    *
    * @param index the index of the parameter to set
    * @param parameter <code>StoredProcedureParameter</code> the parameter
    * @throws IllegalArgumentExcecption if the parameters (index and parameter)
    *   are invalid.
    */
    public void setParameter(SPParameter parameter){
        if(parameter == null){
            throw new IllegalArgumentException();
        }

        Integer key = parameter.getIndex();
        if(parameter.getParameterType() == ParameterInfo.RESULT_SET){
            resultSetParameterKey = key;
        }

        mapOfParameters.put(key, parameter);
    }

    public void setCalledWithReturn(boolean calledWithReturn) {
        this.calledWithReturn = calledWithReturn;
    }

    public boolean isCalledWithReturn() {
        return calledWithReturn;
    }

    /**
     * @return the displayNamedParameters
     */
    public boolean isDisplayNamedParameters() {
        return this.displayNamedParameters;
    }

    /**
     * @param displayNamedParameters the displayNamedParameters to set
     */
    public void setDisplayNamedParameters(boolean displayNamedParameters) {
        this.displayNamedParameters = displayNamedParameters;
    }

    /**
     * @return the isProcedureRelational
     */
    public boolean isProcedureRelational() {
        return this.isProcedureRelational;
    }

    /**
     * @param isProcedureRelational the isProcedureRelational to set
     */
    public void setProcedureRelational(boolean isProcedureRelational) {
        this.isProcedureRelational = isProcedureRelational;
    }

    /** Accept the visitor. **/
    public void jjtAccept(Teiid8ParserVisitor visitor, Object data) {
        visitor.visit(this, data);
    }
}
/* JavaCC - OriginalChecksum=c312e9c5d62fcc77b0a38cf092591213 (do not edit this line) */
