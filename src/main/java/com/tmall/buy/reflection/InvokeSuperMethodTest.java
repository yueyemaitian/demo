package com.tmall.buy.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 子类实例通过反射调用超类方法的时候，也会动态分派
 * @author fanhua
 *
 */
public class InvokeSuperMethodTest {

	private static int N = 3;

	public static void main(String[] args) throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		System.out.println("getValue:");
		SuperClazz inst = new SubClazz();
		System.out.println(inst.getValue());
		Method m = SuperClazz.class.getMethod("getValue", new Class[0]);
		System.out.println(m.invoke(inst, new Object[0]));

		for (int i = 2; i <= N; i++) {
			String methodName = String.format("getValue%s", i);
			System.out.println(methodName + ":");
			m = SubClazz.class.getDeclaredMethod(methodName, new Class[0]);
			m.setAccessible(true);
			System.out.println(m.invoke(inst, new Object[0]));

			m = SuperClazz.class.getDeclaredMethod(methodName, new Class[0]);
			m.setAccessible(true);
			System.out.println(m.invoke(inst, new Object[0]));
		}

	}

}
