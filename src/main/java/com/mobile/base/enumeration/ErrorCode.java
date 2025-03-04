package com.mobile.base.enumeration;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NO_HANDLER_FOUND("ERROR", "No handler found"),
    RESOURCE_NOT_FOUND("ERROR", "Resource not found"),
    INVALID_FORM("ERROR", "Invalid form"),
    BUSINESS_ERROR("ERROR", "Business error"),
    UNAUTHENTICATED("ERROR", "Unauthenticated"),
    AUTHENTICATION_ERROR("ERROR", "Authentication error"),
    ACCESS_DENIED("ERROR", "Access denied"),
    UNKNOWN_ERROR("ERROR", "Unknown error"),

    /**
     * ACCOUNT error codes
     */
    ACCOUNT_NOT_FOUND("ERROR-ACCOUNT-0001", "Account not found");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
