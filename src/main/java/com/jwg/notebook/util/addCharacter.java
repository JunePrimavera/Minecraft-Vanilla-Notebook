package com.jwg.notebook.util;

public class addCharacter {
    public static String add(String str, int position, String keyStr) {
        int len = str.length();
        char[] updatedArr = new char[len + 1];
        try {
            char ch = keyStr.charAt(0);
            str.getChars(0, len, updatedArr, 0);
            updatedArr[len - position] = ch;
            str.getChars(len - position, len, updatedArr, len - position + 1);
        } catch (IndexOutOfBoundsException e) {
            add(str, 0, keyStr);
        }
        return new String(updatedArr)+"";
    }
}
