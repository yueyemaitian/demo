package com.tmall.buy.structure;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * ����ռ���ֽڴ�С������
 *
 * @author tianmai.fh
 * @date 2014-03-18 11:29
 */
public class SizeOfObject {
    static Instrumentation inst;

    public static void premain(String args, Instrumentation instP) {
        inst = instP;
    }

    /**
     * ֱ�Ӽ��㵱ǰ����ռ�ÿռ��С��������ǰ�༰����Ļ�������ʵ���ֶδ�С��<br></br>
     * ��������ʵ���ֶ����ô�С��ʵ����������������ռ�ÿռ䡢ʵ�����������������ñ���ռ�ÿռ��С;<br></br>
     * ���ǲ���������̳������ĺ͵�ǰ��������ʵ�������ֶεĶ�����Ĵ�С��ʵ�������������õĶ�����Ĵ�С <br></br>
     *
     * @param obj
     * @return
     */
    public static long sizeOf(Object obj) {
        return inst.getObjectSize(obj);
    }

    /**
     * �ݹ���㵱ǰ����ռ�ÿռ��ܴ�С��������ǰ��ͳ����ʵ���ֶδ�С�Լ�ʵ���ֶ����ö����С
     *
     * @param objP
     * @return
     * @throws IllegalAccessException
     */
    public static long fullSizeOf(Object objP) throws IllegalAccessException {
        Set<Object> visited = new HashSet<Object>();
        Deque<Object> toBeQueue = new ArrayDeque<Object>();
        toBeQueue.add(objP);
        long size = 0L;
        while (toBeQueue.size() > 0) {
            Object obj = toBeQueue.poll();
            //sizeOf��ʱ���Ѿ��ƻ������ͺ����õĳ��ȣ���������
            size += skipObject(visited, obj) ? 0L : sizeOf(obj);
            Class<?> tmpObjClass = obj.getClass();
            if (tmpObjClass.isArray()) {
                //[I , [F �����������ֳ�����2
                if (tmpObjClass.getName().length() > 2) {
                    for (int i = 0, len = Array.getLength(obj); i < len; i++) {
                        Object tmp = Array.get(obj, i);
                        if (tmp != null) {
                            //�ǻ���������Ҫ��ȱ��������
                            toBeQueue.add(Array.get(obj, i));
                        }
                    }
                }
            } else {
                while (tmpObjClass != null) {
                    Field[] fields = tmpObjClass.getDeclaredFields();
                    for (Field field : fields) {
                        if (Modifier.isStatic(field.getModifiers())   //��̬����
                                || field.getType().isPrimitive()) {    //�������Ͳ��ظ���
                            continue;
                        }

                        field.setAccessible(true);
                        Object fieldValue = field.get(obj);
                        if (fieldValue == null) {
                            continue;
                        }
                        toBeQueue.add(fieldValue);
                    }
                    tmpObjClass = tmpObjClass.getSuperclass();
                }
            }
        }
        return size;
    }

    /**
     * String.intern�Ķ��󲻼ƣ�������Ĳ��ƣ�Ҳ������ѭ��
     *
     * @param visited
     * @param obj
     * @return
     */
    static boolean skipObject(Set<Object> visited, Object obj) {
        if (obj instanceof String && obj == ((String) obj).intern()) {
            return true;
        }

        return visited.contains(obj);
    }

}
