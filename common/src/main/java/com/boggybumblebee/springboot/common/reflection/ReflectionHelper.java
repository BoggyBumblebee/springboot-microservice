/* Copyright (c) 2008-2012. JP Morgan Chase & Co. All rights reserved. */
package com.boggybumblebee.springboot.common.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ReflectionHelper {

	private ReflectionHelper() {

	}

	/**
	 * Get the Property Value using the Getter method and reflection.
	 * 
	 * @param propertyName the Property Name
	 * @param bean the Bean
	 * @return the Property Value
	 */
	public static Object getPropertyValue(final String propertyName, final Object bean) {
		try {
			String getterMethodName = getGetterMethodName(propertyName);
			Method method = bean.getClass().getMethod(getterMethodName);
			return method.invoke(bean);
		}
		catch (Exception ignore) {
			return null;
		}
	}

	/**
	 * Set the Property Value using the Setter method and reflection.
	 * 
	 * @param propertyName the Property Name
	 * @param bean the Bean
	 * @param value the Property Value
	 * @param klass the Property Class
	 * @return true if property value is set correctly
	 */
	public static boolean setPropertyValue(final String propertyName, final Object bean, final Object value, final Class<?> klass) {
		try {
			String setterMethodName = getSetterMethodName(propertyName);
			Method method = bean.getClass().getMethod(setterMethodName, klass);
			method.invoke(bean, value);

			return Boolean.TRUE;
		}
		catch (Exception ignored) {
			return Boolean.FALSE;
		}
	}

	public static String getGetterMethodName(final String propertyName) {
		return "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}

	public static String getSetterMethodName(final String propertyName) {
		return "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}

	/**
	 * Checks if the property (X) given for a class has a getX method defined with public visibility.
	 * 
	 * @param propertyName the property's name whose getter method needs to be checked
	 * @param bean the Object class in which getter method will be checked
	 * @return true if class contains a method getX with visibility public
	 */
	public static boolean hasGetter(final String propertyName, final Object bean) {
		try {
			String getterMethodName = getGetterMethodName(propertyName);
			Method method = bean.getClass().getMethod(getterMethodName);

			if (Modifier.isPublic(method.getModifiers())) {
				return Boolean.TRUE;
			}
		}
		catch (Exception ignored) {
			return Boolean.FALSE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Checks if the property (X) in a class has a setX method defined with public visibility.
	 * 
	 * @param propertyName the property's name whose setter method needs to be checked.
	 * @param bean the Object class in which getter method will be checked
	 * @param klass the Enum class in which setter method will be checked
	 * @return if class contains a method setX with visibility public
	 */
	public static boolean hasSetter(final String propertyName, final Object bean, final Class<?> klass) {
		try {
			String setterMethodName = getSetterMethodName(propertyName);
			Method method = bean.getClass().getMethod(setterMethodName, klass);

			if (Modifier.isPublic(method.getModifiers())) {
				return Boolean.TRUE;
			}
		}
		catch (Exception ignored) {
			return Boolean.FALSE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Checks if the property (X) in a class has a getX and setX methods defined with public visibility.
	 * 
	 * @param propertyName the property's name whose setter method needs to be checked
	 * @param bean the Object class in which getter method will be checked
	 * @param klass the Enum class in which setter method will be checked
	 * @return if class contains getX and setX methods with visibility public
	 */
	public static boolean hasGetterSetter(final String propertyName, final Object bean, final Class<?> klass) {
		return hasGetter(propertyName, bean) && hasSetter(propertyName, bean, klass);
	}
}
