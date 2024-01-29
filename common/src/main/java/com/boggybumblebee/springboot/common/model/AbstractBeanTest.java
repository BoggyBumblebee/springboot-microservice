/* Copyright (c) 2008-2012. JP Morgan Chase & Co. All rights reserved. */
package com.boggybumblebee.springboot.common.model;

import static com.boggybumblebee.springboot.common.reflection.ReflectionHelper.getPropertyValue;
import static com.boggybumblebee.springboot.common.reflection.ReflectionHelper.hasGetter;
import static com.boggybumblebee.springboot.common.reflection.ReflectionHelper.hasSetter;
import static com.boggybumblebee.springboot.common.reflection.ReflectionHelper.setPropertyValue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractBeanTest {

    /**
     * Test Case that needs to a concrete implementation in the subclass.
     * <p>
     * This tests that every non-static, private Field has a Getter and Setter, and that the Getter returns what the Setter was provided with.
     */
    // @Test
    public abstract void testPojoContractMet();

    /**
     * Test Case that needs to a concrete implementation in the subclass.
     * <p>
     * This tests that every non-static, private Field that is set with a new value produces a different result for the .equals(Object) method.
     */
    // @Test
    public abstract void testEqualsContractMet();

    /**
     * Test Case that needs to a concrete implementation in the subclass.
     * <p>
     * This tests that every non-static, private Field that is set with a new value produces a different result for the .hashCode() method.
     */
    // @Test
    public abstract void testHashCodeContractMet();

    /**
     * Test Case that needs to a concrete implementation in the subclass.
     * <p>
     * This test that the toString() method returns a string - very simplistic.
     */
    // @Test
    public abstract void testToStringContractMet();

    /**
     * Asserts whether the Class (under Test) is a valid POJO / Java Bean.
     *
     * @param classUnderTest the Class Under Test
     */
    protected void assertMeetsPojoContract(Class<?> classUnderTest) {

        Object instance;

        try {

            instance = classUnderTest.getDeclaredConstructor().newInstance();
        } catch (Exception exception) {

            throw new AssertionError(exception);
        }

        Class<?> currentClass = classUnderTest;

        while (currentClass != Object.class) {

            for (Field field : currentClass.getDeclaredFields()) {

                // Ignore Fields that are not accessible (with a Getter/Setter)
                if (Modifier.isPrivate(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())
                        && !field.getType().isInterface()) {

                    assertThat(hasGetter(field.getName(), instance)).isTrue();
                    assertThat(hasSetter(field.getName(), instance, field.getType())).isTrue();
                }
            }

            // Move to the Super Class, so we test all accessible Fields
            currentClass = currentClass.getSuperclass();
        }
    }

    /**
     * Asserts whether the Class (under Test)'s Equals contract is valid.
     *
     * @param classUnderTest the Class Under Test
     */
    protected void assertMeetsEqualsContract(Class<?> classUnderTest) {

        Object instance1;
        Object instance2;

        try {
            instance1 = classUnderTest.getDeclaredConstructor().newInstance();
            instance2 = classUnderTest.getDeclaredConstructor().newInstance();

            assertThat(instance1).isEqualTo(instance2);
            assertThat(instance2).isEqualTo(instance1);

            Class<?> currentClass = classUnderTest;

            while (currentClass != Object.class) {

                for (Field field : currentClass.getDeclaredFields()) {

                    // Reset the instances
                    instance1 = classUnderTest.getDeclaredConstructor().newInstance();
                    instance2 = classUnderTest.getDeclaredConstructor().newInstance();

                    // Ignore Fields that are not accessible (with a Getter/Setter)
                    if (Modifier.isPrivate(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())
                            && !field.getType().isInterface()) {

                        setFieldValueByFieldType(field, instance1, Boolean.TRUE);

                        assertThat(instance1).isNotEqualTo(instance2);

                        setPropertyValue(field.getName(), instance2, getPropertyValue(field.getName(), instance1), field.getType());

                        assertThat(getPropertyValue(field.getName(), instance1)).isEqualTo(getPropertyValue(field.getName(), instance2));
                        assertThat(instance1).isEqualTo(instance2);

                        setFieldValueByFieldType(field, instance2, Boolean.FALSE);

                        assertThat(instance1).isNotEqualTo(instance2);
                    }
                }

                // Move to the Super Class, so we test all accessible Fields
                currentClass = currentClass.getSuperclass();
            }
        } catch (Exception exception) {

            throw new AssertionError(exception);
        }
    }

    /**
     * Asserts whether the Class (under Test)'s HashCode contract is valid.
     *
     * @param classUnderTest the Class Under Test
     */
    protected void assertMeetsHashCodeContract(Class<?> classUnderTest) {

        try {

            Class<?> currentClass = classUnderTest;

            while (currentClass != Object.class) {

                for (Field field : currentClass.getDeclaredFields()) {

                    Object instance = classUnderTest.getDeclaredConstructor().newInstance();
                    int initialHashCode = instance.hashCode();

                    // Ignore Fields that are not accessible (with a Getter/Setter)
                    if (Modifier.isPrivate(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())
                            && !field.getType().isInterface()) {

                        setFieldValueByFieldType(field, instance, Boolean.TRUE);

                        int updatedHashCode = instance.hashCode();

                        assertThat(initialHashCode == updatedHashCode).isFalse();
                    }
                }

                // Move to the Super Class, so we test all accessible Fields
                currentClass = currentClass.getSuperclass();
            }
        } catch (Exception exception) {

            throw new AssertionError(exception);
        }
    }

    /**
     * Asserts whether the Class (under Test)'s HashCode contract is valid.
     *
     * @param classUnderTest the Class Under Test
     */
    protected void assertToStringContract(Class<?> classUnderTest) {

        Object instance;

        try {

            instance = classUnderTest.getDeclaredConstructor().newInstance();
        } catch (Exception exception) {

            throw new AssertionError(exception);
        }

        assertThat(instance.toString()).isNotEmpty();
    }

    /**
     * Set the Field Value using the Field Type and whether we should use the Minimum Value (or Maximum).
     *
     * @param field           the Field
     * @param instance        the Instance
     * @param useMinimumValue the Use Minimum Value Flag
     */
    private void setFieldValueByFieldType(Field field, Object instance, boolean useMinimumValue) {

        try {

            switch (field.getType().getName()) {
                case "java.lang.String":
                    injectString(field, instance, useMinimumValue);
                    break;
                case "boolean", "java.lang.boolean":
                    injectBoolean(field, instance, useMinimumValue);
                    break;
                case "byte", "java.lang.Byte":
                    injectByte(field, instance, useMinimumValue);
                    break;
                case "short", "java.lang.Short":
                    injectShort(field, instance, useMinimumValue);
                    break;
                case "long", "java.lang.Long":
                    injectLong(field, instance, useMinimumValue);
                    break;
                case "int", "java.lang.Integer":
                    injectInteger(field, instance, useMinimumValue);
                    break;
                case "float", "java.lang.Float":
                    injectFloat(field, instance, useMinimumValue);
                    break;
                case "double", "java.lang.Double":
                    injectDouble(field, instance, useMinimumValue);
                    break;
                case "java.lang.BigDecimal":
                    injectBigDecimal(field, instance, useMinimumValue);
                    break;
                case "java.lang.TimeStamp":
                    injectTimeStamp(field, instance, useMinimumValue);
                    break;

                default:
                    if (field.getType().isEnum()) {
                        if (useMinimumValue) {
                            setPropertyValue(field.getName(), instance, field.getType().getEnumConstants()[0], field.getType());
                        } else {
                            setPropertyValue(field.getName(), instance, field.getType().getEnumConstants()[field.getType().getEnumConstants().length - 1],
                                    field.getType());
                        }
                    } else if (Object.class.isAssignableFrom(field.getType())) {
                        if (useMinimumValue) {
                            setPropertyValue(field.getName(), instance, field.getType().getDeclaredConstructor().newInstance(), field.getType());
                        } else {
                            setPropertyValue(field.getName(), instance, null, field.getType());
                        }
                    }
                    break;
            }


        } catch (Exception exception) {

            throw new AssertionError(exception);
        }
    }

    private void injectString(Field field, Object instance, boolean useMinimumValue) {
        if (useMinimumValue) {
            setPropertyValue(field.getName(), instance, Integer.toHexString(Integer.MIN_VALUE), field.getType());
        } else {
            setPropertyValue(field.getName(), instance, Integer.toHexString(Integer.MAX_VALUE), field.getType());
        }
    }

    private void injectBoolean(Field field, Object instance, boolean useMinimumValue) {
        if (useMinimumValue) {
            setPropertyValue(field.getName(), instance, Boolean.TRUE, field.getType());
        } else {
            setPropertyValue(field.getName(), instance, Boolean.FALSE, field.getType());
        }
    }

    private void injectShort(Field field, Object instance, boolean useMinimumValue) {
        if (useMinimumValue) {
            setPropertyValue(field.getName(), instance, Short.MIN_VALUE, field.getType());
        } else {
            setPropertyValue(field.getName(), instance, Short.MAX_VALUE, field.getType());
        }
    }

    private void injectLong(Field field, Object instance, boolean useMinimumValue) {
        if (useMinimumValue) {
            setPropertyValue(field.getName(), instance, Long.MIN_VALUE, field.getType());
        } else {
            setPropertyValue(field.getName(), instance, Long.MAX_VALUE, field.getType());
        }
    }

    private void injectFloat(Field field, Object instance, boolean useMinimumValue) {
        if (useMinimumValue) {
            setPropertyValue(field.getName(), instance, Float.MIN_VALUE, field.getType());
        } else {
            setPropertyValue(field.getName(), instance, Float.MAX_VALUE, field.getType());
        }
    }

    private void injectInteger(Field field, Object instance, boolean useMinimumValue) {
        if (useMinimumValue) {
            setPropertyValue(field.getName(), instance, Integer.MIN_VALUE, field.getType());
        } else {
            setPropertyValue(field.getName(), instance, Integer.MAX_VALUE, field.getType());
        }
    }

    private void injectByte(Field field, Object instance, boolean useMinimumValue) {
        if (useMinimumValue) {
            setPropertyValue(field.getName(), instance, Byte.MIN_VALUE, field.getType());
        } else {
            setPropertyValue(field.getName(), instance, Byte.MAX_VALUE, field.getType());
        }
    }

    private void injectDouble(Field field, Object instance, boolean useMinimumValue) {
        if (useMinimumValue) {
            setPropertyValue(field.getName(), instance, Double.MIN_VALUE, field.getType());
        } else {
            setPropertyValue(field.getName(), instance, Double.MAX_VALUE, field.getType());
        }
    }

    private void injectBigDecimal(Field field, Object instance, boolean useMinimumValue) {
        if (useMinimumValue) {
            setPropertyValue(field.getName(), instance, BigDecimal.ONE, field.getType());
        } else {
            setPropertyValue(field.getName(), instance, BigDecimal.TEN, field.getType());
        }
    }

    private void injectTimeStamp(Field field, Object instance, boolean useMinimumValue) {
        if (useMinimumValue) {
            setPropertyValue(field.getName(), instance, new Timestamp(Integer.MIN_VALUE), field.getType());
        } else {
            setPropertyValue(field.getName(), instance, new Timestamp(Integer.MAX_VALUE), field.getType());
        }
    }


}
