/*
 * JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.teiid.runtime.client.sql.v7;

import java.util.List;
import org.teiid.runtime.client.lang.TeiidNodeFactory.ASTNodes;
import org.teiid.runtime.client.lang.ast.AggregateSymbol;
import org.teiid.runtime.client.lang.ast.Block;
import org.teiid.runtime.client.lang.ast.CompareCriteria;
import org.teiid.runtime.client.lang.ast.CreateUpdateProcedureCommand;
import org.teiid.runtime.client.lang.ast.CriteriaSelector;
import org.teiid.runtime.client.lang.ast.Expression;
import org.teiid.runtime.client.lang.ast.ExpressionSymbol;
import org.teiid.runtime.client.lang.ast.HasCriteria;
import org.teiid.runtime.client.lang.ast.IfStatement;
import org.teiid.runtime.client.lang.ast.TranslateCriteria;
import org.teiid.runtime.client.lang.ast.WindowFunction;
import org.teiid.runtime.client.sql.AbstractTestFactory;
import org.teiid.runtime.client.sql.QueryParser;

/**
 *
 */
@SuppressWarnings( { "javadoc", "nls" } )
public class Test7Factory extends AbstractTestFactory {

    /**
     * @param parser
     */
    public Test7Factory(QueryParser parser) {
        super(parser);
    }

    @Override
    public Expression wrapExpression(Expression expr, String... exprName) {
        String name = "expr";
        if (exprName != null && exprName.length > 0)
            name = exprName[0];

        ExpressionSymbol exprSymbol = newNode(ASTNodes.EXPRESSION_SYMBOL);
        exprSymbol.setName(name);
        exprSymbol.setExpression(expr);
        return exprSymbol;
    }

    @Override
    public AggregateSymbol newAggregateSymbol(String name, boolean isDistinct, Expression expression) {
        AggregateSymbol as = newNode(ASTNodes.AGGREGATE_SYMBOL);
        as.setName(name);
        as.setAggregateFunction(name);
        as.setDistinct(isDistinct);
        as.setExpression(expression);
        return as;
    }

    @Override
    public WindowFunction newWindowFunction(String name) {
        WindowFunction windowFunction = newNode(ASTNodes.WINDOW_FUNCTION);
        windowFunction.setName(name);
        return windowFunction;
    }

    public CriteriaSelector newCriteriaSelector() {
        CriteriaSelector cs = newNode(ASTNodes.CRITERIA_SELECTOR);
        return cs;
    }

    public CreateUpdateProcedureCommand newCreateUpdateProcedureCommand() {
        CreateUpdateProcedureCommand cupc = newNode(ASTNodes.CREATE_UPDATE_PROCEDURE_COMMAND);
        return cupc;
    }

    public HasCriteria newHasCriteria(CriteriaSelector critSelector) {
        HasCriteria hasSelector = newNode(ASTNodes.HAS_CRITERIA);
        hasSelector.setSelector(critSelector);
        return hasSelector;
    }

    public TranslateCriteria newTranslateCriteria() {
        TranslateCriteria tc = newNode(ASTNodes.TRANSLATE_CRITERIA);
        return tc;
    }

    public TranslateCriteria newTranslateCriteria(CriteriaSelector critSelector, List<CompareCriteria> critList) {
        TranslateCriteria tc = newTranslateCriteria();
        tc.setSelector(critSelector);
        tc.setTranslations(critList);
        return tc;
    }

    public IfStatement newIfStatement(Block ifBlock, Block elseBlock, HasCriteria hasSelector) {
        IfStatement ifStmt = newNode(ASTNodes.IF_STATEMENT);
        ifStmt.setIfBlock(ifBlock);
        ifStmt.setElseBlock(elseBlock);
        ifStmt.setCondition(hasSelector);
        return ifStmt;
    }

}
