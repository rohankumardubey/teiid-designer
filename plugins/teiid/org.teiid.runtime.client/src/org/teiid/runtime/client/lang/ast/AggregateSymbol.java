/*
 * JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.teiid.runtime.client.lang.ast;

import org.teiid.designer.annotation.Removed;
import org.teiid.designer.annotation.Since;

/**
 *
 */
@SuppressWarnings( "unused" )
public interface AggregateSymbol extends Node, SingleElementSymbol, Expression {

    public enum Type {        
        COUNT,
        SUM,
        AVG,
        MIN,
        MAX,
        XMLAGG,
        TEXTAGG,
        ARRAY_AGG,
        ANY,
        SOME,
        EVERY,
        STDDEV_POP,
        STDDEV_SAMP,
        VAR_POP,
        VAR_SAMP,
        RANK,
        DENSE_RANK,
        ROW_NUMBER,

        @Since("8.0.0")
        JSONARRAY_AGG,

        @Since("8.0.0")
        STRING_AGG,

        @Since("8.0.0")
        USER_DEFINED;
    }

    /**
     * @return name
     */
    String getName();

    /**
     * @param name
     */
    void setName(String name);

    /**
     * @return distinct
     */
    boolean isDistinct();

    /**
     * @param isDistinct
     */
    void setDistinct(boolean isDistinct);

    /**
     * Get function arguments
     *
     * @return Get function arguments
     */
    @Since("8.0.0")
    Expression[] getArgs();

    /**
     * @param arguments
     */
    @Since("8.0.0")
    void setArgs(Expression[] arguments);

    /**
     * @return order by
     */
    public OrderBy getOrderBy();

    /**
     * @param orderBy
     */
    public void setOrderBy(OrderBy orderBy);

    /**
     * @return condition
     */
    public Expression getCondition();
    
    /**
     * @param condition
     */
    void setCondition(Expression condition);
    
    /**
     * @return isWindowed
     */
    boolean isWindowed();
    
    /**
     * @param isWindowed
     */
    void setWindowed(boolean isWindowed);

    /**
     * @return canonicalName
     */
    @Removed("8.0.0")
    String getCanonicalName();

    /**
     * @param canonicalName
     */
    void setCanonicalName(String canonicalName);

}
