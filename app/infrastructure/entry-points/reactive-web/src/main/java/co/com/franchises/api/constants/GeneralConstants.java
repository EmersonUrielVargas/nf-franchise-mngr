package co.com.franchises.api.constants;

public class GeneralConstants {

    public static final String DEFAULT_ERROR_MESSAGE_LOG = "Unexpected error occurred: {}";
    public static final String PATH_PARAMETER_ID_NAME = "id";

    public static final String HTTP_OK = "200";
    public static final String HTTP_CONFLICT = "409";
    public static final String HTTP_CREATED = "201";


    // Descriptions
    // descriptions operations routes
    public static final String FRANCHISE_CREATED_DESC = "Create a new franchise in a system";
    public static final String FRANCHISE_UPDATE_NAME_DESC = "Update name of a existing franchise";
    public static final String BRANCH_OFFICE_CREATE_DESC = "Create a new branch office in a existing franchise";
    public static final String BRANCH_OFFICE_UPDATE_NAME_DESC = "Update name of a exist branch office";
    public static final String PRODUCT_CREATE_DESC = "Create a new product in a existing branch office";
    public static final String PRODUCT_DELETE_DESC = "Delete a product by id";
    public static final String PRODUCT_UPDATE_STOCK_DESC = "Update stock a product by id";
    public static final String PRODUCT_UPDATE_NAME_DESC = "Update name a product by id";
    public static final String PRODUCT_RANK_STOCK_BY_OFFICE_BRANCH_DESC = "List of products with the largest stock in each branch by franchise ";

    //description params
    public static final String FRANCHISE_ID_UPDATE_NAME_DESC = "Id of the franchise will be updated name";
    public static final String FRANCHISE_ID_BRANCH_OFFICE_UPDATE_NAME_DESC = "Id of the franchise where the branch exist";
    public static final String FRANCHISE_ID_BRANCH_OFFICE_CREATE_DESC = "Id of the franchise where the branch will be created";
    public static final String BRANCH_ID_BRANCH_OFFICE_CREATE_DESC = "Id of branch office where the product will be created";
    public static final String PRODUCT_ID_DELETE_DESC = "Id of the product will be deleted";
    public static final String PRODUCT_ID_UPDATE_PARAMS_DESC = "Id of the product will be updated";
    public static final String FRANCHISE_ID_RANK_PRODUCTS_PARAMS_DESC = "Id of the franchise will be get report";


    //description responses
    public static final String PRODUCT_PARAMS_UPDATED_RESPONSE_DESC = "The Product instance with updated values";
    public static final String FRANCHISE_PARAMS_UPDATED_RESPONSE_DESC = "The franchise instance with updated values";
    public static final String BRANCH_OFFICE_PARAMS_UPDATED_RESPONSE_DESC = "The branch office instance with updated values";
    public static final String FRANCHISE_CREATED_RESPONSE_DESC = "Franchise instance created";
    public static final String BRANCH_OFFICE_CREATED_RESPONSE_DESC = "Branch office instance created";
    public static final String PRODUCT_CREATED_RESPONSE_DESC = "Product instance created";
    public static final String PRODUCT_DELETE_RESPONSE_DESC = "The Product has been deleted successful";
    public static final String PRODUCT_RANK_STOCK_FRANCHISE_RESPONSE_OK_DESC = "List of products with branch office with the largest stock for branch office";
    public static final String ERROR_CONFLICT_RESPONSE_DESC = "A error has occurred with the information provided";


}
