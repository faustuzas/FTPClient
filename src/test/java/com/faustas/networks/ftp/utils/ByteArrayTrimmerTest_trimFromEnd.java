package com.faustas.networks.ftp.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ByteArrayTrimmerTest_trimFromEnd {

    @Test
    public void givenEmptyArray_returnsEmptyArray() {
        byte[] array = new byte[0];

        byte[] trimmed = ByteArrayTrimmer.trimFromEnd(array);

        assertArrayEquals(array, trimmed);
    }

    @Test
    public void givenOnlyTrashByte__returnsEmptyArray() {
        byte[] array = new byte[] { 1, 1, 1, 1, 1, 1 };

        byte[] trimmed = ByteArrayTrimmer.trimFromEnd(array, (byte)1);

        assertArrayEquals(new byte[0], trimmed);
    }

    @Test
    public void givenAllGoodArray_doesntChangeAnything() {
        byte[] array = new byte[] { 1, 2, 3, 4, 5, 6 };

        byte[] trimmed = ByteArrayTrimmer.trimFromEnd(array);

        assertArrayEquals(array, trimmed);
    }

    @Test
    public void givenArrayWithGoodAndBadBytes__trimsCorrectly() {
        byte[] array = new byte[] { 1, 2, 3, 4, 5, 6, 0, 0, 0, 0 };

        byte[] trimmed = ByteArrayTrimmer.trimFromEnd(array);

        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6 }, trimmed);
    }
}