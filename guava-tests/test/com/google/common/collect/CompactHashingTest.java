package com.google.common.collect;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for CompactHashing class.
 *
 * Testing 4 methods which was not fully covered by the original tests.
 *
 * @Author Johan Henning
 */
public class CompactHashingTest {

    /**
     * Checks if the methods clear the input array of either binary, integer or short.
     * Clears means replace all elements with 0s.
     *
     * The type is defined depending on which size we create the table as.
     */
    @Test
    public void tableClear() {
        //Init the 3 types of arrays created by createTable
        byte[] byteArr = {1,2,3,4,5,6,8,9,0};
        short[] shortArr = {1,2,3,4,5,6,8,9,0};
        int[] intArr = {1,2,3,4,5,6,8,9,0};

        CompactHashing.tableClear(byteArr);
        CompactHashing.tableClear(shortArr);
        CompactHashing.tableClear(intArr);

        for (int i = 0; i < byteArr.length; i++) {
            assertEquals(byteArr[i],0);
        }

        for (int i = 0; i < shortArr.length; i++) {
            assertEquals(shortArr[i],0);
        }

        for (int i = 0; i < intArr.length; i++) {
            assertEquals(intArr[i],0);
        }
    }

    /**
     * checks that for each data type (byte,long,int) that the function tableSet
     * works as it should. tableSet takes in an array, index and a value which
     * then sets the index of the array to the specific value.
     *
     * The type is defined depending on which size we create the table as.
     *
     * Note that the OR operation is needed for byte and short since they
     * will return as signed no matter what.
     */
    @Test
    public void tableSet() {
        //Init the 3 types of arrays created by createTable
        byte[] byteArr = (byte[]) CompactHashing.createTable(256);
        short[] shortArr = (short[]) CompactHashing.createTable(65536);
        int[] intArr = (int[]) CompactHashing.createTable(65536<<1);

        // Checks before and after the change made with tableSet for all types supported.

        for (int i = 0; i < byteArr.length; i++) {
            assertEquals(byteArr[i], 0);
            CompactHashing.tableSet(byteArr,i,i);
            assertEquals(byteArr[i] & 255, i);
        }

        for (int i = 0; i < shortArr.length; i++) {
            assertEquals(shortArr[i], 0);
            CompactHashing.tableSet(shortArr,i,i);
            assertEquals(shortArr[i] & 65535, i);
        }

        for (int i = 0; i < intArr.length; i++) {
            assertEquals(intArr[i], 0);
            CompactHashing.tableSet(intArr,i,i);
            assertEquals(intArr[i], i);
        }
    }

    /**
     * tableGet checks that the correct types (byte,short,int) are working as intended.
     * tableGet method takes in an array and index, and returns the value of the index in
     * the array.
     */
    @Test
    public void tableGet() {
        //Init the 3 types of arrays created by createTable
        byte[] byteArr = (byte[]) CompactHashing.createTable(256);
        short[] shortArr = (short[]) CompactHashing.createTable(65536);
        int[] intArr = (int[]) CompactHashing.createTable(65536<<1);

        //Populate the arrays

        for (int i = 0; i < byteArr.length; i++) {
            byteArr[i] = (byte) i;
        }

        for (int i = 0; i < shortArr.length; i++) {
            shortArr[i] = (short) i;
        }

        for (int i = 0; i < intArr.length; i++) {
            intArr[i] = i;
        }

        // random samples

        assertEquals(byteArr[0], CompactHashing.tableGet(byteArr,0));
        assertEquals(byteArr[7], CompactHashing.tableGet(byteArr,7));

        assertEquals(shortArr[0], CompactHashing.tableGet(shortArr,0));
        assertEquals(shortArr[14], CompactHashing.tableGet(shortArr,14));

        assertEquals(intArr[0], CompactHashing.tableGet(intArr,0));
        assertEquals(intArr[intArr.length-1], CompactHashing.tableGet(intArr,(intArr.length-1)));
    }

    /**
     * Checks the create process of a table, depending on the input integer it creates
     * either a int array, short array or int array. If the input is not in the format of 2^n
     * then an exception will be thrown.
     *
     * The type is defined depending on which size we create the table as.
     *
     * This specific test focuses on the size, that each array type works and the exceptions.
     */
    @Test //(expected = IllegalArgumentException.class)
    public void createTable() {
        //Init exceptions
        Throwable e1 = null;
        Throwable e2 = null;
        Throwable e3 = null;

        //Init the 3 types of arrays created by createTable
        byte[] byteArr = (byte[]) CompactHashing.createTable(256);
        short[] shortArr = (short[]) CompactHashing.createTable(65536);
        int[] intArr = (int[]) CompactHashing.createTable(65536<<1);

        assertEquals(byteArr.length, 1<<Byte.SIZE);
        assertEquals(shortArr.length, 1<<Short.SIZE);
        assertEquals(intArr.length, 1<<(Short.SIZE + 1));

        try {
            // Check bucket size less than 2 statement
            CompactHashing.createTable(0);
        } catch (Throwable ex) {
            e1 = ex;
        }

        try {
            // Check bucket size is larger than size 2^30
            CompactHashing.createTable(1073741824<<1);
        } catch (Throwable ex) {
            e2 = ex;
        }

        try {
            // Check bucket size is not of the size 2^n
            CompactHashing.createTable(7);
        } catch (Throwable ex) {
            e3 = ex;
        }

        assertTrue(e1 instanceof IllegalArgumentException);
        assertTrue(e2 instanceof IllegalArgumentException);
        assertTrue(e3 instanceof IllegalArgumentException);
    }
}