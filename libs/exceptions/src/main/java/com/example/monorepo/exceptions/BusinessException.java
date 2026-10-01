package com.example.monorepo.exceptions;

import java.util.Map;

/** Se incumple una regla de negocio. La peticion era sintacticamente valida. */
public class BusinessException extends BaseApplicationException {

    public BusinessException(String message) {
        super(CommonErrorCode.BUSINESS_RULE_VIOLATION, message);
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(ErrorCode errorCode, String message, Map<String, Object> context) {
        super(errorCode, message, context);
    }
}
