package com.ypcxpt.fish.library.util;

public class HexString {

    private HexString() {
        // Utility class.
    }

    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String toHexString(int i) {
        return String.format("%02x", i);
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static byte[] hexToBytes(String hexRepresentation) {
        if (hexRepresentation.length() % 2 == 1) {
            throw new IllegalArgumentException("hexToBytes requires an even-length String parameter");
        }

        int len = hexRepresentation.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexRepresentation.charAt(i), 16) << 4)
                    + Character.digit(hexRepresentation.charAt(i + 1), 16));
        }

        return data;
    }

    public static int hexStringToInt(String str) {
        try {
            return Integer.parseInt(str, 16);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static short hexStringToShort(String str) {
        try {
            return Short.parseShort(str, 16);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
