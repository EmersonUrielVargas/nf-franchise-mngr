package co.com.franchises.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DomainExceptionsMessage {

    INTERNAL_ERROR("0-000","Something went wrong, please try again", ""),
    FRANCHISE_ALREADY_EXIST("0-001","Already exist a franchise with the provided information", ""),
    FRANCHISE_CREATION_FAIL("0-002","An error occurred while creating a franchise", ""),
    FRANCHISE_NOT_FOUND("0-003","The franchise was not found", ""),
    BRANCH_ALREADY_EXIST("0-004","Already exist a branch office with the provided information", ""),
    BRANCH_CREATION_FAIL("0-005","An error occurred while creating a branch office", "");

    private final String code;
    private final String message;
    private final String param;
}