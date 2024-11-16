package com.plantify.transaction.global.exception.errorcode;

import com.plantify.transaction.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum TransactionErrorCode implements ErrorCode {

    TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "거래를 찾을 수 없습니다."),
    INVALID_TRANSACTION_AMOUNT(HttpStatus.BAD_REQUEST, "거래 금액이 잘못되었습니다."),
    TRANSACTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "거래 처리에 실패하였습니다."),
    DUPLICATE_TRANSACTION(HttpStatus.CONFLICT, "중복 거래가 감지되었습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "거래에 대한 접근 권한이 없습니다."),
    INVALID_TRANSACTION_STATUS(HttpStatus.BAD_REQUEST, "잘못된 거래 상태입니다."),
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "시스템 오류가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

