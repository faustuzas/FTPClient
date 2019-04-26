package com.faustas.networks.ftp.utils;

import java.util.Arrays;

public class ByteArrayTrimmer {

    /**
     * Trims specified bytes from the end of the given array
     *
     * @param array the array to be trimmed
     * @param stopByte byte which occurrences will be deleted from the end
     *                 defaults to 0
     * @return trimmed array
     */
    public static byte[] trimFromEnd(byte[] array, byte stopByte) {
        int lastOccurenceOfByte = 0;
        for (int i = array.length - 1; i > 0; --i) {
            if (array[i] != stopByte) {
                /*
                    value is incremented because currently it represents
                    last good byte, so we need to shift the index
                    one position to the right
                 */
                lastOccurenceOfByte = i + 1;
                break;
            }
        }

        return Arrays.copyOf(array, lastOccurenceOfByte);
    }

    public static byte[] trimFromEnd(byte[] array) {
        return trimFromEnd(array, (byte)0);
    }
}
