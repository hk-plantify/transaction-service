package com.plantify.transaction.global.exception.errorcode;

import com.plantify.transaction.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum TransactionErrorCode implements ErrorCode {

    // 거래
    TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "거래를 찾을 수 없습니다."),
    INVALID_TRANSACTION_AMOUNT(HttpStatus.BAD_REQUEST, "거래 금액이 잘못되었습니다."),
    TRANSACTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "거래 처리에 실패하였습니다."),
    DUPLICATE_TRANSACTION(HttpStatus.CONFLICT, "중복 거래가 감지되었습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "거래에 대한 접근 권한이 없습니다."),
    INVALID_TRANSACTION_STATUS(HttpStatus.BAD_REQUEST, "잘못된 거래 상태입니다."),
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "시스템 오류가 발생하였습니다."),

    // 환불
    REFUND_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "해당 거래는 환불이 허용되지 않습니다."),
    REFUND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "환불 처리 중 오류가 발생하였습니다."),
    REFUND_ALREADY_PROCESSED(HttpStatus.CONFLICT, "이미 환불된 거래입니다."),
    REFUND_AMOUNT_EXCEEDS(HttpStatus.BAD_REQUEST, "환불 금액이 거래 금액을 초과합니다."),

    // 취소
    CANCELLATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "해당 거래는 취소할 수 없습니다."),
    CANCELLATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "거래 취소 처리 중 오류가 발생하였습니다."),
    DUPLICATE_CANCELLATION_REQUEST(HttpStatus.CONFLICT, "중복된 취소 요청이 감지되었습니다."),

    // 동시성 제어
    CONCURRENT_UPDATE(HttpStatus.CONFLICT, "동시에 처리 중인 요청이 있습니다.");

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

