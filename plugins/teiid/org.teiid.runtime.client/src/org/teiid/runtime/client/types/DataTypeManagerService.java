/*
 * JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.teiid.runtime.client.types;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.teiid.core.types.BinaryType;
import org.teiid.core.types.BlobType;
import org.teiid.core.types.ClobType;
import org.teiid.core.types.NullType;
import org.teiid.core.types.Transform;
import org.teiid.core.types.XMLType;
import org.teiid.core.types.basic.AnyToObjectTransform;
import org.teiid.core.types.basic.AnyToStringTransform;
import org.teiid.core.types.basic.BooleanToNumberTransform;
import org.teiid.core.types.basic.FixedNumberToBigDecimalTransform;
import org.teiid.core.types.basic.FixedNumberToBigIntegerTransform;
import org.teiid.core.types.basic.FloatingNumberToBigDecimalTransform;
import org.teiid.core.types.basic.FloatingNumberToBigIntegerTransform;
import org.teiid.core.types.basic.NullToAnyTransform;
import org.teiid.core.types.basic.NumberToBooleanTransform;
import org.teiid.core.types.basic.NumberToByteTransform;
import org.teiid.core.types.basic.NumberToDoubleTransform;
import org.teiid.core.types.basic.NumberToFloatTransform;
import org.teiid.core.types.basic.NumberToIntegerTransform;
import org.teiid.core.types.basic.NumberToLongTransform;
import org.teiid.core.types.basic.NumberToShortTransform;
import org.teiid.core.types.basic.ObjectToAnyTransform;
import org.teiid.designer.annotation.Since;
import org.teiid.designer.type.IDataTypeManagerService;
import org.teiid.runtime.client.Messages;
import org.teiid.runtime.client.TeiidClientException;
import org.teiid.runtime.client.util.ArgCheck;
import org.teiid.runtime.client.util.PropertiesUtil;

/**
 *
 */
public class DataTypeManagerService implements IDataTypeManagerService {

    private static final String ARRAY_SUFFIX = "[]"; //$NON-NLS-1$

    public enum DefaultDataTypes {

        STRING ("string", DataTypeName.STRING, String.class, 256), //$NON-NLS-1$

        VARCHAR ("varchar", DataTypeName.VARCHAR , String.class, 256), //$NON-NLS-1$
        
        BOOLEAN ("boolean", DataTypeName.BOOLEAN, Boolean.class), //$NON-NLS-1$

        BYTE ("byte", DataTypeName.BYTE, Byte.class, 3, "0123456789-"), //$NON-NLS-1$ //$NON-NLS-2$

        TINYINT ("tinyint", DataTypeName.TINYINT, Byte.class, 3, "0123456789-"), //$NON-NLS-1$ //$NON-NLS-2$
        
        SHORT ("short", DataTypeName.SHORT, Short.class, 5, "0123456789-"), //$NON-NLS-1$ //$NON-NLS-2$

        SMALLINT ("smallint", DataTypeName.SMALLINT, Short.class, 5, "0123456789-"), //$NON-NLS-1$ //$NON-NLS-2$

        CHAR ("char", DataTypeName.CHAR, Character.class, 1), //$NON-NLS-1$

        INTEGER ("integer", DataTypeName.INTEGER, Integer.class, 10, "0123456789-"), //$NON-NLS-1$ //$NON-NLS-2$

        /* Not to be confused with BIG_INTEGER which comes from a different domain */
        BIGINT ("bigint", DataTypeName.BIGINT, Long.class, 19, "0123456789-"), //$NON-NLS-1$ //$NON-NLS-2$

        LONG ("long", DataTypeName.LONG, Long.class, 19, "0123456789-"), //$NON-NLS-1$ //$NON-NLS-2$

        BIG_INTEGER ("biginteger", DataTypeName.BIG_INTEGER, BigInteger.class, 30, "0123456789-"), //$NON-NLS-1$ //$NON-NLS-2$

        FLOAT ("float", DataTypeName.FLOAT, Float.class, 30, "0123456789-+.eE"), //$NON-NLS-1$ //$NON-NLS-2$

        REAL ("real", DataTypeName.REAL, Float.class, 30, "0123456789-+.eE"), //$NON-NLS-1$ //$NON-NLS-2$
        
        DOUBLE ("double", DataTypeName.DOUBLE, Double.class, 30, "0123456789-+.eE"), //$NON-NLS-1$ //$NON-NLS-2$

        DECIMAL ("decimal", DataTypeName.DECIMAL, BigDecimal.class, 30, "0123456789-.eE"), //$NON-NLS-1$ //$NON-NLS-2$

        BIG_DECIMAL ("bigdecimal", DataTypeName.BIG_DECIMAL, BigDecimal.class, 30, "0123456789-.eE"), //$NON-NLS-1$ //$NON-NLS-2$

        DATE ("date", DataTypeName.DATE, Date.class), //$NON-NLS-1$

        TIME ("time", DataTypeName.TIME, Time.class), //$NON-NLS-1$

        TIMESTAMP ("timestamp", DataTypeName.TIMESTAMP, Timestamp.class), //$NON-NLS-1$

        OBJECT ("object", DataTypeName.OBJECT, Object.class), //$NON-NLS-1$

        NULL ("null", DataTypeName.NULL, NullType.class), //$NON-NLS-1$

        BLOB ("blob", DataTypeName.BLOB, BlobType.class), //$NON-NLS-1$

        CLOB ("clob", DataTypeName.CLOB, ClobType.class), //$NON-NLS-1$

        XML ("xml", DataTypeName.XML, XMLType.class), //$NON-NLS-1$

        @Since("8.0.0")
        VARBINARY ("varbinary", DataTypeName.VARBINARY, BinaryType.class); //$NON-NLS-1$

        private String id;

        private DataTypeName dataTypeName;

        private Class<?> klazz;

        private int limit = -1;

        private String validChars = null;

        private Class<?> arrayKlazz;

        DefaultDataTypes(String id, DataTypeName dataTypeName, Class<?> klazz) {
            this.id = id;
            this.dataTypeName = dataTypeName;
            this.klazz = klazz;
            this.arrayKlazz = Array.newInstance(klazz, 0).getClass();
        }

        DefaultDataTypes(String id, DataTypeName dataTypeName, Class<?> klazz, int limit) {
            this(id, dataTypeName, klazz);
            this.limit = limit;
        }

        DefaultDataTypes(String id, DataTypeName dataTypeName, Class<?> klazz, int limit, String validChars) {
            this(id, dataTypeName, klazz, limit);
            this.validChars = validChars;
        }

        public String getId() {
            return this.id;
        }

        public DataTypeName getDataTypeName() {
            return dataTypeName;
        }

        public Class<?> getTypeClass() {
            return klazz;
        }

        public Class<?> getTypeArrayClass() {
            return arrayKlazz;
        }

        public int getLimit() {
            return this.limit;
        }

        public String getValidChars() {
            return validChars;
        }
    }

    /**
     * Doubly-nested map of String srcType --> Map of String targetType -->
     * Transform
     */
    private static Map<DefaultDataTypes, Map<DefaultDataTypes, Transform>> transforms = new HashMap<DefaultDataTypes, Map<DefaultDataTypes, Transform>>(128);

    private static DataTypeManagerService instance = null;

    /**
     * @return the singleton instance
     */
    public static DataTypeManagerService getInstance() {
        if (instance == null) {
            instance = new DataTypeManagerService();
            instance.loadBasicTransforms();
        }

        return instance;
    }

    /**
     * Load all basic {@link Transform}s into the  This standard
     * set is always installed but may be overridden.
     */
    private void loadBasicTransforms() {
        Class<?> byteClass = DataTypeManagerService.DefaultDataTypes.BYTE.getTypeClass();
        Class<?> longClass = DataTypeManagerService.DefaultDataTypes.LONG.getTypeClass();
        Class<?> shortClass = DataTypeManagerService.DefaultDataTypes.SHORT.getTypeClass();
        Class<?> integerClass = DataTypeManagerService.DefaultDataTypes.INTEGER.getTypeClass();
        Class<?> doubleClass = DataTypeManagerService.DefaultDataTypes.DOUBLE.getTypeClass();
        Class<?> bigDecimalClass = DataTypeManagerService.DefaultDataTypes.BIG_DECIMAL.getTypeClass();
        Class<?> bigIntegerClass = DataTypeManagerService.DefaultDataTypes.BIG_INTEGER.getTypeClass();
        Class<?> floatClass = DataTypeManagerService.DefaultDataTypes.FLOAT.getTypeClass();

        addTransform(new BooleanToNumberTransform(Byte.valueOf((byte)1), Byte.valueOf((byte)0)));
        addTransform(new BooleanToNumberTransform(Short.valueOf((short)1), Short.valueOf((short)0)));
        addTransform(new BooleanToNumberTransform(Integer.valueOf(1), Integer.valueOf(0)));
        addTransform(new BooleanToNumberTransform(Long.valueOf(1), Long.valueOf(0)));
        addTransform(new BooleanToNumberTransform(BigInteger.valueOf(1), BigInteger.valueOf(0)));
        addTransform(new BooleanToNumberTransform(Float.valueOf(1), Float.valueOf(0)));
        addTransform(new BooleanToNumberTransform(Double.valueOf(1), Double.valueOf(0)));
        addTransform(new BooleanToNumberTransform(BigDecimal.valueOf(1), BigDecimal.valueOf(0)));
        addTransform(new AnyToStringTransform(DefaultDataTypes.BOOLEAN.getTypeClass()));

        addTransform(new NumberToBooleanTransform(Byte.valueOf((byte)0)));
        addTransform(new NumberToShortTransform(byteClass, false));
        addTransform(new NumberToIntegerTransform(byteClass, false));
        addTransform(new NumberToLongTransform(byteClass, false, false));
        addTransform(new FixedNumberToBigIntegerTransform(byteClass));
        addTransform(new NumberToFloatTransform(byteClass, false, false));
        addTransform(new NumberToDoubleTransform(byteClass, false, false));
        addTransform(new FixedNumberToBigDecimalTransform(byteClass));
        addTransform(new AnyToStringTransform(byteClass));

        addTransform(new AnyToStringTransform(DefaultDataTypes.CHAR.getTypeClass()));

        addTransform(new NumberToBooleanTransform(Short.valueOf((short)0)));
        addTransform(new NumberToByteTransform(shortClass));
        addTransform(new NumberToIntegerTransform(shortClass, false));
        addTransform(new NumberToLongTransform(shortClass, false, false));
        addTransform(new FixedNumberToBigIntegerTransform(shortClass));
        addTransform(new NumberToFloatTransform(shortClass, false, false));
        addTransform(new NumberToDoubleTransform(shortClass, false, false));
        addTransform(new FixedNumberToBigDecimalTransform(shortClass));
        addTransform(new AnyToStringTransform(shortClass));

        addTransform(new NumberToBooleanTransform(Integer.valueOf(0)));
        addTransform(new NumberToByteTransform(integerClass));
        addTransform(new NumberToShortTransform(integerClass, true));
        addTransform(new NumberToLongTransform(integerClass, false, false));
        addTransform(new FixedNumberToBigIntegerTransform(integerClass));
        addTransform(new NumberToFloatTransform(integerClass, false, true)); //lossy, but not narrowing
        addTransform(new NumberToDoubleTransform(integerClass, false, false));
        addTransform(new FixedNumberToBigDecimalTransform(integerClass));
        addTransform(new AnyToStringTransform(integerClass));

        addTransform(new NumberToBooleanTransform(Long.valueOf(0)));
        addTransform(new NumberToByteTransform(longClass));
        addTransform(new NumberToShortTransform(longClass, true));
        addTransform(new NumberToIntegerTransform(longClass, true));
        addTransform(new FixedNumberToBigIntegerTransform(longClass));
        addTransform(new NumberToFloatTransform(longClass, false, true)); //lossy, but not narrowing
        addTransform(new NumberToDoubleTransform(longClass, false, true)); //lossy, but not narrowing
        addTransform(new FixedNumberToBigDecimalTransform(longClass));
        addTransform(new AnyToStringTransform(longClass));

        addTransform(new NumberToBooleanTransform(BigInteger.valueOf(0)));
        addTransform(new NumberToByteTransform(bigIntegerClass));
        addTransform(new NumberToShortTransform(bigIntegerClass, true));
        addTransform(new NumberToIntegerTransform(bigIntegerClass, true));
        addTransform(new NumberToLongTransform(bigIntegerClass, true, false));
        addTransform(new NumberToFloatTransform(bigIntegerClass, true, false));
        addTransform(new NumberToDoubleTransform(bigIntegerClass, true, false));
        addTransform(new org.teiid.core.types.basic.BigIntegerToBigDecimalTransform());
        addTransform(new AnyToStringTransform(bigIntegerClass));

        addTransform(new NumberToBooleanTransform(BigDecimal.valueOf(0)));
        addTransform(new NumberToByteTransform(bigDecimalClass));
        addTransform(new NumberToShortTransform(bigDecimalClass, true));
        addTransform(new NumberToIntegerTransform(bigDecimalClass, true));
        addTransform(new NumberToLongTransform(bigDecimalClass, true, false));
        addTransform(new org.teiid.core.types.basic.BigDecimalToBigIntegerTransform());
        addTransform(new NumberToFloatTransform(bigDecimalClass, true, false));
        addTransform(new NumberToDoubleTransform(bigDecimalClass, true, false));
        addTransform(new AnyToStringTransform(bigDecimalClass));

        addTransform(new NumberToBooleanTransform(Float.valueOf(0)));
        addTransform(new NumberToByteTransform(floatClass));
        addTransform(new NumberToShortTransform(floatClass, true));
        addTransform(new NumberToIntegerTransform(floatClass, true));
        addTransform(new NumberToLongTransform(floatClass, false, true)); //lossy, but not narrowing
        addTransform(new FloatingNumberToBigIntegerTransform(floatClass));
        addTransform(new NumberToDoubleTransform(floatClass, false, false));
        addTransform(new FloatingNumberToBigDecimalTransform(floatClass));
        addTransform(new AnyToStringTransform(floatClass));

        addTransform(new NumberToBooleanTransform(Double.valueOf(0)));
        addTransform(new NumberToByteTransform(doubleClass));
        addTransform(new NumberToShortTransform(doubleClass, true));
        addTransform(new NumberToIntegerTransform(doubleClass, true));
        addTransform(new NumberToLongTransform(doubleClass, false, true)); //lossy, but not narrowing
        addTransform(new FloatingNumberToBigIntegerTransform(doubleClass));
        addTransform(new NumberToFloatTransform(doubleClass, true, false));
        addTransform(new FloatingNumberToBigDecimalTransform(doubleClass));
        addTransform(new AnyToStringTransform(doubleClass));

        addTransform(new org.teiid.core.types.basic.DateToTimestampTransform());
        addTransform(new AnyToStringTransform(DefaultDataTypes.DATE.getTypeClass()));

        addTransform(new org.teiid.core.types.basic.TimeToTimestampTransform());
        addTransform(new AnyToStringTransform(DefaultDataTypes.TIME.getTypeClass()));

        addTransform(new org.teiid.core.types.basic.TimestampToTimeTransform());
        addTransform(new org.teiid.core.types.basic.TimestampToDateTransform());
        addTransform(new AnyToStringTransform(DefaultDataTypes.TIMESTAMP.getTypeClass()));

        addTransform(new org.teiid.core.types.basic.StringToBooleanTransform());
        addTransform(new org.teiid.core.types.basic.StringToByteTransform());
        addTransform(new org.teiid.core.types.basic.StringToShortTransform());
        addTransform(new org.teiid.core.types.basic.StringToIntegerTransform());
        addTransform(new org.teiid.core.types.basic.StringToLongTransform());
        addTransform(new org.teiid.core.types.basic.StringToBigIntegerTransform());
        addTransform(new org.teiid.core.types.basic.StringToFloatTransform());
        addTransform(new org.teiid.core.types.basic.StringToDoubleTransform());
        addTransform(new org.teiid.core.types.basic.StringToBigDecimalTransform());
        addTransform(new org.teiid.core.types.basic.StringToTimeTransform());
        addTransform(new org.teiid.core.types.basic.StringToDateTransform());
        addTransform(new org.teiid.core.types.basic.StringToTimestampTransform());
        addTransform(new org.teiid.core.types.basic.StringToCharacterTransform());
        addTransform(new org.teiid.core.types.basic.StringToClobTransform());
        addTransform(new org.teiid.core.types.basic.StringToSQLXMLTransform());

        addTransform(new org.teiid.core.types.basic.BinaryToBlobTransform());

        addTransform(new org.teiid.core.types.basic.ClobToStringTransform());

        addTransform(new org.teiid.core.types.basic.BlobToBinaryTransform());

        addTransform(new org.teiid.core.types.basic.SQLXMLToStringTransform());

        for (Class<?> type : getAllDataTypeClasses()) {
            if (type != DefaultDataTypes.OBJECT.getTypeClass()) {
                addTransform(new AnyToObjectTransform(type));
                addTransform(new ObjectToAnyTransform(type));
            }
            if (type != DefaultDataTypes.NULL.getTypeClass()) {
                addTransform(new NullToAnyTransform(type));
            }
        }

        addTransform(new AnyToStringTransform(DefaultDataTypes.OBJECT.getTypeClass()) {
            @Override
            public boolean isExplicit() {
                return true;
            }
        });
    }

    /** Utility to get Transform given srcType and targetType */
    private Transform getTransformFromMaps(DefaultDataTypes srcType, DefaultDataTypes targetType) {
        Map<DefaultDataTypes, Transform> innerMap = transforms.get(srcType);
        if (innerMap != null) {
            return innerMap.get(targetType);
        }
        return null;
    }

    private DefaultDataTypes findDefaultDataType(String id) {
        for (DefaultDataTypes defaultDataType : DefaultDataTypes.values()) {
            if (defaultDataType.getId().equals(id)) {
                return defaultDataType;
            }
        }

        return null;
    }
    
    private DefaultDataTypes findDefaultDataType(DataTypeName dataTypeName) {
        for (DefaultDataTypes defaultDataType : DefaultDataTypes.values()) {
            if (defaultDataType.getDataTypeName().equals(dataTypeName)) {
                return defaultDataType;
            }
        }

        return null;
    }

    private DefaultDataTypes findDefaultDataType(Class<?> typeClass) {
        for (DefaultDataTypes defaultDataType : DefaultDataTypes.values()) {
            if (defaultDataType.getTypeClass().equals(typeClass) || defaultDataType.getTypeArrayClass().equals(typeClass)) {
                return defaultDataType;
            }
        }

        return null;
    }

    private boolean isArrayType(String name) {
        return name.endsWith(ARRAY_SUFFIX);
    }

    private String getComponentType(String name) {
        return name.substring(0, name.lastIndexOf(ARRAY_SUFFIX));
    }
    
    @Override
    public String getDefaultDataType(DataTypeName dataTypeName) {
        ArgCheck.isNotNull(dataTypeName);

        DefaultDataTypes dataType = findDefaultDataType(dataTypeName);
        if (dataType != null)
            return dataType.getId();

        throw new RuntimeException();
    }

    /**
     * Get the data type with the given name.
     * 
     * @param name
     *      Data type name
     *      
     * @return Data type class
     */
    public DefaultDataTypes getDataType(String name) {
        if (name == null) {
            return DefaultDataTypes.NULL;
        }

        DefaultDataTypes dataType = null;
        if (isArrayType(name)) {
            String compName = getComponentType(name);
            dataType = findDefaultDataType(compName);
        } else {
            dataType = findDefaultDataType(name);
        }

        if (dataType == null)
            dataType = DefaultDataTypes.OBJECT;

        return dataType;
    }

    @Override
    public Class<?> getDataTypeClass(String name) {
        if (name == null) {
            return DefaultDataTypes.NULL.getTypeClass();
        }

        DefaultDataTypes dataType = getDataType(name);
        if (isArrayType(name)) {
            return dataType.getTypeArrayClass();
        }

        return dataType.getTypeClass();
    }

    @Override
    public Class<?> getDefaultDataClass(DataTypeName dataTypeName) {
        ArgCheck.isNotNull(dataTypeName);

        DefaultDataTypes dataType = findDefaultDataType(dataTypeName);
        if (dataType != null)
            return dataType.getTypeClass();

        throw new RuntimeException();
    }

    public DefaultDataTypes getDataType(Class<?> typeClass) {
        ArgCheck.isNotNull(typeClass);

        DefaultDataTypes dataType = findDefaultDataType(typeClass);
        if (dataType != null)
            return dataType;

        return DefaultDataTypes.OBJECT;
    }
   
    @Override
    public String getDataTypeName(Class<?> typeClass) {
        ArgCheck.isNotNull(typeClass);

        DefaultDataTypes dataType = findDefaultDataType(typeClass);
        if (dataType != null)
            return dataType.getId();

        return DefaultDataTypes.OBJECT.getId();
    }

    @Override
    public Set<String> getAllDataTypeNames() {
        Set<String> dataTypeNames = new HashSet<String>();
        for (DefaultDataTypes defaultDataType : DefaultDataTypes.values()) {
            dataTypeNames.add(defaultDataType.getId());
        }

        return dataTypeNames;
    }


    /**
     * Get a set of all data type classes.
     *
     * @return Set of data type classes (Class)
     */
    public Set<Class<?>> getAllDataTypeClasses() {
        Set<Class<?>> dataTypeNames = new HashSet<Class<?>>();
        for (DefaultDataTypes defaultDataType : DefaultDataTypes.values()) {
            dataTypeNames.add(defaultDataType.getTypeClass());
        }

        return dataTypeNames;
    }

    @Override
    public Integer getDataTypeLimit(String dataType) {
        ArgCheck.isNotNull(dataType);

        for (DefaultDataTypes defaultDataType : DefaultDataTypes.values()) {
            if (defaultDataType.getId().equals(dataType)) {
                return defaultDataType.getLimit();
            }
        }

        return -1;
    }

    @Override
    public String getDataTypeValidChars(String dataType) {
        ArgCheck.isNotNull(dataType);
        
        for (DefaultDataTypes defaultDataType : DefaultDataTypes.values()) {
            if (defaultDataType.getId().equals(dataType)) {
                return defaultDataType.getValidChars();
            }
        }

        return null;
    }

    @Override
    public String getDataSourceType(DataSourceTypes dataSourceType) {
        if (dataSourceType == null)
            return DataSourceTypes.UNKNOWN.id();

        return dataSourceType.id();
    }

    private Transform getTransform(String sourceTypeName, String targetTypeName) {
        if (sourceTypeName == null || targetTypeName == null) {
            throw new IllegalArgumentException(Messages.getString(Messages.ERR.ERR_003_029_0002, sourceTypeName, targetTypeName));
        }
        
        DefaultDataTypes sourceType = findDefaultDataType(sourceTypeName);
        DefaultDataTypes targetType = findDefaultDataType(targetTypeName);

        return getTransformFromMaps(sourceType, targetType);
    }

    @Override
    public boolean isTransformable(String sourceTypeName, String targetTypeName) {
        if (sourceTypeName == null || targetTypeName == null)
            throw new IllegalArgumentException(Messages.getString(Messages.ERR.ERR_003_029_0002, sourceTypeName, targetTypeName));

        return getTransform(sourceTypeName, targetTypeName) != null;
    }

    @Override
    public boolean isExplicitConversion(String sourceTypeName, String targetTypeName) {
        Transform t = getTransform(sourceTypeName, targetTypeName);
        if (t != null) {
            return t.isExplicit();
        }
        return false;
    }
    
    @Override
    public boolean isImplicitConversion(String sourceTypeName, String targetTypeName) {
        Transform t = getTransform(sourceTypeName, targetTypeName);
        if (t != null) {
            return !t.isExplicit();
        }

        if (DefaultDataTypes.NULL.getId().equals(sourceTypeName) && !DefaultDataTypes.NULL.getId().equals(targetTypeName)) {
            return true;
        }

        if (DefaultDataTypes.OBJECT.getId().equals(targetTypeName) && !DefaultDataTypes.OBJECT.getId().equals(sourceTypeName)) {
            return true;
        }

        if (isArrayType(sourceTypeName) && isArrayType(targetTypeName)) {
            return isImplicitConversion(getComponentType(sourceTypeName), getComponentType(targetTypeName));
        }

        return false;
    }

    /**
     * Add a new transform to the known transform types.
     * 
     * @param transform
     *      New transform to add
     */
    public void addTransform(Transform transform) {
        ArgCheck.isNotNull(transform);
        String sourceName = transform.getSourceTypeName();
        String targetName = transform.getTargetTypeName();

        DefaultDataTypes sourceDataType = findDefaultDataType(sourceName);
        DefaultDataTypes targetDataType = findDefaultDataType(targetName);
        
        Map<DefaultDataTypes, Transform> innerMap = transforms.get(sourceDataType);
        if (innerMap == null) {
            innerMap = new LinkedHashMap<DefaultDataTypes, Transform>();
            transforms.put(sourceDataType, innerMap);
        }
        innerMap.put(targetDataType, transform);
    }

    @SuppressWarnings("unchecked")
    public <T> T transformValue(Object value, DefaultDataTypes defaultDataType) throws Exception {
        if (value == null) {
            return (T)value;
        }
        return transformValue(value, value.getClass(), defaultDataType);
    }

    @SuppressWarnings("unchecked")
    public <T> T transformValue(Object value, Class<?> sourceType, DefaultDataTypes targetDataType) throws Exception {
        if (value == null || sourceType == targetDataType.getTypeClass() || DefaultDataTypes.OBJECT == targetDataType) {
            return (T) value;
        }

        DefaultDataTypes sourceDataType = findDefaultDataType(sourceType);
        Transform transform = getTransformFromMaps(sourceDataType, targetDataType);
        if (transform == null) {
            Object[] params = new Object[] { sourceType, targetDataType, value};
            throw new TeiidClientException(Messages.gs(Messages.TEIID.TEIID10076, params));
        }

        T result = (T) transform.transform(value);
        return result;
    }

    public boolean isDecimalAsDouble() {
        return PropertiesUtil.getBooleanProperty(System.getProperties(), "org.teiid.decimalAsDouble", false); //$NON-NLS-1$
    }
}
