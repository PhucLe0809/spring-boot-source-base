package com.mobile.base.utils;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Utility class for generate code.
 */
public final class CodeGeneratorUtils {

    public CodeGeneratorUtils() {
        // Prevent instantiation
    }

    /**
     * Generate code with specified length.
     */
    public static String generateCode(Integer length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
