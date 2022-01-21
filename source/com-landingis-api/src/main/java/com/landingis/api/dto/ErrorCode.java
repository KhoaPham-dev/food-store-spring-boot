package com.landingis.api.dto;

public class ErrorCode {

    /**
     * General error code
     */
    public static final String GENERAL_ERROR_UNAUTHORIZED = "ERROR-GENERAL-000";
    public static final String GENERAL_ERROR_NOT_FOUND = "ERROR-GENERAL-001";
    public static final String GENERAL_ERROR_BAD_REQUEST = "ERROR-GENERAL-002";
    public static final String GENERAL_ERROR_LOGIN_FAILED = "ERROR-GENERAL-003";
    public static final String GENERAL_ERROR_NOT_MATCH = "ERROR-GENERAL-004";
    public static final String GENERAL_ERROR_WRONG_HASH = "ERROR-GENERAL-005";
    public static final String GENERAL_ERROR_LOCKED = "ERROR-GENERAL-006";
    public static final String GENERAL_ERROR_INVALID = "ERROR-GENERAL-007";

    /**
     * Category error code
     */
    public static final String CATEGORY_ERROR_UNAUTHORIZED = "ERROR-CATEGORY-000";
    public static final String CATEGORY_ERROR_NOT_FOUND = "ERROR-CATEGORY-001";

    /**
     * Group error code
     */
    public static final String GROUP_ERROR_UNAUTHORIZED = "ERROR-GROUP-000";
    public static final String GROUP_ERROR_NOT_FOUND = "ERROR-GROUP-001";
    public static final String GROUP_ERROR_EXIST = "ERROR-GROUP-002";
    public static final String GROUP_ERROR_CAN_NOT_DELETED = "ERROR-GROUP-003";

    /**
     * Permission error code
     */
    public static final String PERMISSION_ERROR_UNAUTHORIZED = "ERROR-PERMISSION-000";
    public static final String PERMISSION_ERROR_NOT_FOUND = "ERROR-PERMISSION-001";

    /**
     * News error code
     */
    public static final String NEWS_ERROR_UNAUTHORIZED = "ERROR-NEWS-000";
    public static final String NEWS_ERROR_NOT_FOUND = "ERROR-NEWS-001";
    /**
     * Customer error code
     */
    public static final String CUSTOMER_ERROR_UNAUTHORIZED = "ERROR-CUSTOMER-000";
    public static final String CUSTOMER_ERROR_NOT_FOUND = "ERROR-CUSTOMER-001";
    /**
     * Account error code
     */
    public static final String ACCOUNT_ERROR_UNAUTHORIZED = "ERROR-ACCOUNT-000";
    public static final String ACCOUNT_ERROR_NOT_FOUND = "ERROR-ACCOUNT-001";
    public static final String ACCOUNT_ERROR_EXIST = "ERROR-ACCOUNT-002";

    /**
     * Employee error code
     */
    public static final String EMPLOYEE_ERROR_UNAUTHORIZED = "ERROR-EMPLOYEE-000";
    public static final String EMPLOYEE_ERROR_NOT_FOUND = "ERROR-EMPLOYEE-001";

    /**
     * Province error code
     */
    public static final String PROVINCE_ERROR_UNAUTHORIZED = "ERROR-PROVINCE-000";
    public static final String PROVINCE_ERROR_NOT_FOUND = "ERROR-PROVINCE-001";
    public static final String PROVINCE_ERROR_BAD_REQUEST = "ERROR-PROVINCE-002";

    /**
     * Addresses error code
     */
    public static final String ADDRESSES_ERROR_UNAUTHORIZED = "ERROR-ADDRESSES-000";
    public static final String ADDRESSES_ERROR_NOT_FOUND = "ERROR-ADDRESSES-001";

    /**
     * Product error code
     */
    public static final String PRODUCT_ERROR_UNAUTHORIZED = "ERROR-PRODUCT-000";
    public static final String PRODUCT_ERROR_NOT_FOUND = "ERROR-PRODUCT-001";
    public static final String PRODUCT_ERROR_NOT_HAS_CHILD = "ERROR-PRODUCT-002";

    /**
     * Collaborator error code
     */
    public static final String COLLABORATOR_ERROR_UNAUTHORIZED = "ERROR-COLLABORATOR-000";
    public static final String COLLABORATOR_ERROR_NOT_FOUND = "ERROR-COLLABORATOR-001";

    /**
     * Collaborator-Product error code
     */
    public static final String COLLABORATOR_PRODUCT_ERROR_UNAUTHORIZED = "ERROR-COLLABORATOR_PRODUCT-000";
    public static final String COLLABORATOR_PRODUCT_ERROR_NOT_FOUND = "ERROR-COLLABORATOR-PRODUCT-001";
    public static final String COLLABORATOR_PRODUCT_ERROR_BAD_REQUEST = "ERROR-COLLABORATOR-PRODUCT-002";

    /**
     * Orders error code
     */
    public static final String ORDERS_ERROR_UNAUTHORIZED = "ERROR-ORDERS-000";
    public static final String ORDERS_ERROR_NOT_FOUND = "ERROR-ORDERS-001";
    public static final String ORDERS_ERROR_BAD_REQUEST = "ERROR-ORDERS-002";

    /**
     * Orders Detail error code
     */
    public static final String ORDERS_DETAIL_ERROR_UNAUTHORIZED = "ERROR-DETAIL-ORDERS-000";
    public static final String ORDERS_DETAIL_ERROR_NOT_FOUND = "ERROR-DETAIL-ORDERS-001";
    public static final String ORDERS_DETAIL_ERROR_BAD_REQUEST = "ERROR-DETAIL-ORDERS-002";

    /**
     * Settings error code
     */
    public static final String SETTINGS_ERROR_UNAUTHORIZED = "ERROR-SETTINGS-000";
    public static final String SETTINGS_ERROR_NOT_FOUND = "ERROR-SETTINGS-001";
    public static final String SETTINGS_ERROR_BAD_REQUEST = "ERROR-SETTINGS-002";

    private ErrorCode() { throw new IllegalStateException("Utility class"); }
}
