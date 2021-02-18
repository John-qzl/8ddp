package com.cssrc.ibms.core.util.json.support;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.lang.reflect.Field;

/**
 * 
 * @author Yangbo 2016-7-22
 *
 */
public class SuperclassExclusionStrategy implements ExclusionStrategy {
	public boolean shouldSkipClass(Class<?> arg0) {
		return false;
	}

	public boolean shouldSkipField(FieldAttributes fieldAttributes) {
		String fieldName = fieldAttributes.getName();
		Class theClass = fieldAttributes.getDeclaringClass();
		return isFieldInSuperclass(theClass, fieldName);
	}

	private boolean isFieldInSuperclass(Class<?> subclass, String fieldName) {
		Class superclass = subclass.getSuperclass();

		while (superclass != null) {
			Field field = getField(superclass, fieldName);
			if (field != null)
				return true;
			superclass = superclass.getSuperclass();
		}
		return false;
	}

	private Field getField(Class<?> theClass, String fieldName) {
		try {
			return theClass.getDeclaredField(fieldName);
		} catch (Exception e) {
		}
		return null;
	}
}
