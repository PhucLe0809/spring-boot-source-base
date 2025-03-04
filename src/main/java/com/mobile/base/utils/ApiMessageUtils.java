package com.mobile.base.utils;

import com.mobile.base.dto.ApiMessageDto;
import com.mobile.base.enumeration.ErrorCode;

/**
 * Utility class for generating API response messages.
 */
public final class ApiMessageUtils {

    private ApiMessageUtils() {
        // Prevent instantiation
    }

    /**
     * Base method to create an API response.
     */
    private static <T> ApiMessageDto<T> baseResponse(boolean isSuccess, String code, T data, String message) {
        ApiMessageDto<T> response = new ApiMessageDto<>();
        response.setResult(isSuccess);
        response.setCode(code);
        response.setData(data);
        response.setMessage(message);
        return response;
    }

    /**
     * Generates a successful API response.
     */
    public static <T> ApiMessageDto<T> success(T data, String message) {
        return baseResponse(true, null, data, message);
    }

    /**
     * Generates an error API response based on ErrorCode.
     */
    public static <T> ApiMessageDto<T> error(ErrorCode errorCode) {
        return baseResponse(false, errorCode.getCode(), null, errorCode.getMessage());
    }
}
