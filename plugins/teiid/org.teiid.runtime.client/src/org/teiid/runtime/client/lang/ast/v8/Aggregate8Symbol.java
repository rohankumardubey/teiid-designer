/* Generated By:JJTree: Do not edit this line. AggregateSymbol.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.teiid.runtime.client.lang.ast.v8;

import java.util.HashMap;
import java.util.Map;
import org.teiid.runtime.client.lang.ast.AggregateSymbol;
import org.teiid.runtime.client.lang.ast.Expression;
import org.teiid.runtime.client.lang.ast.Function;
import org.teiid.runtime.client.lang.ast.OrderBy;
import org.teiid.runtime.client.lang.ast.Teiid8ParserVisitor;
import org.teiid.runtime.client.lang.parser.v8.Teiid8Parser;
import org.teiid.runtime.client.types.DataTypeManagerService;
import org.teiid.runtime.client.types.DataTypeManagerService.DefaultDataTypes;

/**
 * From Teiid Version 8 onwards the AggregateSymbol extends Function
 */
public class Aggregate8Symbol extends Function implements AggregateSymbol {

    private boolean distinct;

    private OrderBy orderBy;

    private Expression condition;

    private Type aggregate;

    private boolean isWindowed;

    private static final Class<?> COUNT_TYPE = DataTypeManagerService.DefaultDataTypes.INTEGER.getTypeClass();
    private static final Map<Class<?>, Class<?>> SUM_TYPES;
    private static final Map<Class<?>, Class<?>> AVG_TYPES;

    static {
        Class<?> byteClass = DataTypeManagerService.DefaultDataTypes.BYTE.getTypeClass();
        Class<?> longClass = DataTypeManagerService.DefaultDataTypes.LONG.getTypeClass();
        Class<?> shortClass = DataTypeManagerService.DefaultDataTypes.SHORT.getTypeClass();
        Class<?> integerClass = DataTypeManagerService.DefaultDataTypes.INTEGER.getTypeClass();
        Class<?> doubleClass = DataTypeManagerService.DefaultDataTypes.DOUBLE.getTypeClass();
        Class<?> bigDecimalClass = DataTypeManagerService.DefaultDataTypes.BIG_DECIMAL.getTypeClass();
        Class<?> bigIntegerClass = DataTypeManagerService.DefaultDataTypes.BIG_INTEGER.getTypeClass();
        Class<?> floatClass = DataTypeManagerService.DefaultDataTypes.FLOAT.getTypeClass();

        SUM_TYPES = new HashMap<Class<?>, Class<?>>();        
        SUM_TYPES.put(byteClass, longClass);
        SUM_TYPES.put(shortClass, longClass);
        SUM_TYPES.put(integerClass, longClass);
        SUM_TYPES.put(longClass, longClass);        
        SUM_TYPES.put(bigIntegerClass, bigIntegerClass);
        SUM_TYPES.put(floatClass, doubleClass);
        SUM_TYPES.put(doubleClass, doubleClass);        
        SUM_TYPES.put(bigDecimalClass, bigDecimalClass);
        
        AVG_TYPES = new HashMap<Class<?>, Class<?>>();
        DataTypeManagerService dataTypeManager = DataTypeManagerService.getInstance();
        if(dataTypeManager.isDecimalAsDouble()) {
            AVG_TYPES.put(byteClass, doubleClass);
            AVG_TYPES.put(shortClass, doubleClass);
            AVG_TYPES.put(integerClass, doubleClass);
            AVG_TYPES.put(longClass, doubleClass);
        } else {
            AVG_TYPES.put(byteClass, bigDecimalClass);
            AVG_TYPES.put(shortClass, bigDecimalClass);
            AVG_TYPES.put(integerClass, bigDecimalClass);
            AVG_TYPES.put(longClass, bigDecimalClass);
        }

        AVG_TYPES.put(bigIntegerClass, bigDecimalClass);
        AVG_TYPES.put(floatClass, doubleClass);
        AVG_TYPES.put(doubleClass, doubleClass);
        AVG_TYPES.put(bigDecimalClass, bigDecimalClass);
    }

    public Aggregate8Symbol(int id) {
        super(id);
    }

    public Aggregate8Symbol(Teiid8Parser p, int id) {
        super(p, id);
    }

    private boolean isBoolean() {
        return this.aggregate == Type.EVERY 
                || this.aggregate == Type.SOME 
                || this.aggregate == Type.ANY;
    }

    private boolean isEnhancedNumeric() {
        return this.aggregate == Type.STDDEV_POP 
        || this.aggregate == Type.STDDEV_SAMP
        || this.aggregate == Type.VAR_SAMP
        || this.aggregate == Type.VAR_POP;
    }

    private boolean isAnalytical() {
        switch (this.aggregate) {
        case RANK:
        case ROW_NUMBER:
        case DENSE_RANK:
            return true;
        default:
            return false;
        }
    }

    /**
    * Get the type of the symbol, which depends on the aggregate function and the
    * type of the contained expression
    * @return Type of the symbol
    */
    public Class<?> getType() {
        switch (this.aggregate) {
            case COUNT:
                return COUNT_TYPE;
            case SUM:
                Class<?> expressionType = this.getArg(0).getType();
                return SUM_TYPES.get(expressionType);
            case AVG:
                expressionType = this.getArg(0).getType();
                return AVG_TYPES.get(expressionType);
            case ARRAY_AGG:
                if (this.getArg(0) == null) {
                    return null;
                }
                
                DataTypeManagerService dataTypeManager = DataTypeManagerService.getInstance();
                DefaultDataTypes dataType = dataTypeManager.getDataType(this.getArg(0).getType());
                return dataType.getTypeArrayClass();
            case TEXTAGG:
                return DataTypeManagerService.DefaultDataTypes.BLOB.getTypeClass();
            case USER_DEFINED:
                // TODO
                // Dont want to bring in function descriptors if one can help it
                // May need this for resolving

//                if (this.getFunctionDescriptor() == null) {
//                    return null;
//                }
//                return this.getFunctionDescriptor().getReturnType();
            case JSONARRAY_AGG:
                return DataTypeManagerService.DefaultDataTypes.CLOB.getTypeClass();
            case STRING_AGG:
                return super.getType();
            default:
                // ignore and carry on
        }

        if (isBoolean()) {
            return DataTypeManagerService.DefaultDataTypes.BOOLEAN.getTypeClass();
        }

        if (isEnhancedNumeric()) {
            return DataTypeManagerService.DefaultDataTypes.DOUBLE.getTypeClass();
        }

        if (isAnalytical()) {
            return DataTypeManagerService.DefaultDataTypes.INTEGER.getTypeClass();
        }

        if (this.getArgs().length == 0) {
            return null;
        }

        return this.getArg(0).getType();
    }

    /**
     * Set the aggregate function.  If the aggregate function is an invalid value, an
     * IllegalArgumentException is thrown.
     * @param aggregateFunction Aggregate function type
     * @see org.teiid.language.SQLConstants.NonReserved#COUNT
     * @see org.teiid.language.SQLConstants.NonReserved#SUM
     * @see org.teiid.language.SQLConstants.NonReserved#AVG
     * @see org.teiid.language.SQLConstants.NonReserved#MIN
     * @see org.teiid.language.SQLConstants.NonReserved#MAX
     */
    public void setAggregateFunction(Type aggregateFunction) {
        this.aggregate = aggregateFunction;
    }

    /**
     * Get the aggregate function type - this will map to one of the reserved words
     * for the aggregate functions.
     * @return Aggregate function type
     */
    public Type getAggregateFunction() {
        return this.aggregate;
    }

    /**
     * Get the distinct flag.  If true, aggregate symbol will remove duplicates during
     * computation.
     * @return True if duplicates should be removed during computation
     */
    public boolean isDistinct() {
        return this.distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public Expression getCondition() {
        return condition;
    }
    
    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public boolean isWindowed() {
        return isWindowed;
    }

    public void setWindowed(boolean isWindowed) {
        this.isWindowed = isWindowed;
    }

    @Override
    public String getCanonicalName() {
        // Only applicable to 7.7.0
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCanonicalName(String canonicalName) {
        // Only applicable to 7.7.0
        throw new UnsupportedOperationException();
    }

    /** Accept the visitor. **/
    public void jjtAccept(Teiid8ParserVisitor visitor, Object data) {
        visitor.visit((AggregateSymbol)this, data);
    }
}
/* JavaCC - OriginalChecksum=7efdc98eb15b01c236003d9dc5fca445 (do not edit this line) */
