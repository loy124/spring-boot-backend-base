package com.loy124.myapp.core.util.mail;

import java.security.SecureRandom;

public class TemporaryPasswordGenerator {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_+-=[]|,./?><";

    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;

    private static SecureRandom random = new SecureRandom();

    public static void main(String[] args) {
        int length = 12;
        System.out.println(generateTemporaryPassword(length));
    }

    public static String generateTemporaryPassword(int length) {
        if (length < 4) {
            throw new IllegalArgumentException("비밀번호 길이는 최소 4 이상이어야 합니다.");
        }

        StringBuilder password = new StringBuilder(length);
        String allowBase = PASSWORD_ALLOW_BASE;

        if (length > PASSWORD_ALLOW_BASE.length()) {
            allowBase += PASSWORD_ALLOW_BASE;
        }

        for (int i = 0; i < length; i++) {
            int randomCharIndex = random.nextInt(allowBase.length());
            password.append(allowBase.charAt(randomCharIndex));
        }

        return password.toString();
    }
}
