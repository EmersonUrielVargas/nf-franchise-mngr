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
    BRANCH_CREATION_FAIL("0-005","An error occurred while creating or updating the branch office", ""),
    BRANCH_NOT_FOUND("0-006","The branch office was not found", ""),
    PRODUCT_ALREADY_EXIST("0-007","Already exist a product in this branch office with the provided information", ""),
    PRODUCT_CREATION_FAIL("0-008","An error occurred while creating or updating the product", ""),
    PRODUCT_NOT_FOUND("0-009","The product was not found", ""),
    PRODUCT_DELETE_FAIL("0-010","An error occurred while removing the product", ""),
    PRODUCT_UPDATE_FAIL("0-011","An error occurred while updating the product data", ""),
    STOCK_INVALID("1-000","the stock must be a positive value", ""),
    PARAM_REQUIRED("0-012","The params required cannot be null or empty", ""),
    FRANCHISE_WITHOUT_BRANCH_OFFICES("0-013","The franchise is empty and not contains branch offices", ""),
    FRANCHISE_NAME_ALREADY_EXIST("0-014","Already exist a franchise with the provided name", ""),
    BRANCH_NAME_ALREADY_EXIST("0-015","Already exist a branch with the provided name", ""),
    PRODUCT_NAME_ALREADY_EXIST("0-016","Already exist a product with the provided name", "");

    private final String code;
    private final String message;
    private final String param;
}