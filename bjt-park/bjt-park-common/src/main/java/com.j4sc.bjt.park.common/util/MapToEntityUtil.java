package com.j4sc.bjt.park.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * 将map转换为对应的实体工具类
 * (注：这个只转换map中的名称写实体字段相同)
 * @author chengyz
 */
public class MapToEntityUtil {
	public static Object mapToEntity(Map<String, Object> paramsMap, Class<?> c){
		Object p = null;
		try {
			p = c.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e1) {
			e1.printStackTrace();
		}
		if (p == null)
			return null;
		Field[] fields = c.getDeclaredFields();
		String fieldName;
		boolean isOK = false;//判断是否取到对应的字段值
		for (Field field : fields) {
			fieldName = field.getName();
			if ("serialVersionUID".equals(field.getName()))//自动生成的一个serialVersionUID序列化版本比较值
				continue;
			Class<?> type = field.getType();
			
			Object v = paramsMap.get(fieldName);
			if (v == null)
				continue;
			Object setterValue;
			try {
				setterValue = parseTypeParam(fieldName, type, v);
				field.setAccessible(true);
				field.set(p, setterValue);
				isOK=true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(isOK){
			return p;//至少转化了一个变量值
		}else{
			return null;
		}
		
	}
	
	/**
	 * 转化参数值
	 * @param name 参数名
	 * @param valueType 参数类型
	 * @return
	 * <li>参数值与指定类型匹配时，返回转化后的参数值</li>
	 * <li>其它，返回指定类型的默认值</li>
	 * @throws Exception 发生异常
	 */
	private static Object parseTypeParam(String name, Class<?> valueType,Object v) throws Exception {
		if (valueType == String.class) {
			return v.toString();
		} else if (valueType == int.class || valueType == Integer.class) {
			return Integer.parseInt(v.toString());
		} else if (valueType == short.class || valueType == Short.class) {
			return Short.parseShort(v.toString());
		} else if (valueType == long.class || valueType == Long.class) {
			return Long.parseLong(v.toString());
		} else if (valueType == byte.class || valueType == Byte.class) {
			return Byte.parseByte(v.toString());
		} else if (valueType == boolean.class || valueType == Boolean.class) {
			return Boolean.parseBoolean(v.toString());
		} else if (valueType == float.class || valueType == Float.class) {
			return Float.parseFloat(v.toString());
		} else if (valueType == double.class || valueType == Double.class) {
			return Double.parseDouble(v.toString());
		} else if (valueType == BigInteger.class) {
			return BigInteger.valueOf(Long.valueOf(v.toString()));
		} else if (valueType == BigDecimal.class) {
			return (new BigDecimal(v.toString()));
		} else {
			return null;
		}
	}
}
