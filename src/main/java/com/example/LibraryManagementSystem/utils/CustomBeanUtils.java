package com.example.LibraryManagementSystem.utils;

import org.springframework.beans.BeanUtils;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class CustomBeanUtils {

    public static void copyNonNullProperties(Object source, Object target) {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(source.getClass());
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method getter = propertyDescriptor.getReadMethod();
            Method setter = propertyDescriptor.getWriteMethod();
            if (getter != null && setter != null) {
                try {
                    Object value = getter.invoke(source);
                    if (value != null) {
                        setter.invoke(target, value);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error copying properties", e);
                }
            }
        }
    }
}