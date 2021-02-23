package com.google.common.collect;

import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

/**
 * Test class for CompactHashing class.
 * @Author Johan Henning
 */
public class CompactHashingTest {
    /**
     *  Checks that he constructor is private, by doing this the coverage is increased.
     *
     *  Source: https://stackoverflow.com/questions/4520216/how-to-add-test-coverage-to-a-private-constructor
     *  by Javid Jamae
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<CompactHashing> constructor = CompactHashing.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        final CompactHashing table = (CompactHashing) constructor.newInstance();
    }

    /**
     * Checks the correct default table size returns 4.
     */
    @Test
    public void tableSize() {
        CompactHashing.createTable(4);
        int s = CompactHashing.tableSize(2);
        assertEquals(s,4);
    }

    /**
     * Checks if the methods clear the input array of either binary, integer or short.
     * Clears means replace all elements with 0s.
     */
    @Test
    public void tableClear() {
        int[] intArr = {1,2,3,4,5,6,8,9,0};
        short[] shortArr = {1,2,3,4,5,6,8,9,0};
        byte[] byteArr = {1,2,3,4,5,6,8,9,0};
        CompactHashing.tableClear(intArr);
        CompactHashing.tableClear(shortArr);
        CompactHashing.tableClear(byteArr);
        for (int i = 0; i < intArr.length; i++) {
            assertEquals(intArr[i],0);
        }

        for (int i = 0; i < shortArr.length; i++) {
            assertEquals(shortArr[i],0);
        }

        for (int i = 0; i < byteArr.length; i++) {
            assertEquals(byteArr[i],0);
        }
    }

    /**
     * This simply tests the method for calculating the new capacity given a number.
     * If the input is below 32, it should return ((input + 1) * 4) else its ((input + 1) * 2).
     */
    @Test
    public void newCapacity() {
        assertEquals(CompactHashing.newCapacity(-1), 0);
        assertEquals(CompactHashing.newCapacity(0), 4);
        assertEquals(CompactHashing.newCapacity(10), 44);
        assertEquals(CompactHashing.newCapacity(32), 66);
        assertEquals(CompactHashing.newCapacity(Integer.MAX_VALUE/2) - 1, Integer.MAX_VALUE);
    }
}