package com.example.advancedtaskmanagement.common;

public class ErrorMessages {

    public static final String RESOURCE_NOT_FOUND = "Resource not found.";
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String DEPARTMENT_NOT_FOUND = "Department not found.";
    public static final String PROJECT_NOT_FOUND = "Project not found.";
    public static final String TASK_NOT_FOUND = "Task not found.";
    public static final String TASK_ATTACHMENT_NOT_FOUND = "Task attachment not found.";
    public static final String TASK_COMMENT_NOT_FOUND = "Task comment not found.";
    public static final String INVALID_REQUEST = "Invalid request.";
    public static final String USER_ALREADY_ASSIGNED = "User already assigned to this project.";
    public static final String USER_NOT_ASSIGNED = "User is not assigned to this project.";
    public static final String START_DATE_BEFORE_END_DATE = "Start date must be before end date.";
    public static final String UNEXPECTED_ERROR = "Unexpected error.";
    public static final String PROJECT_STATUS_CANNOT_BE_CANCELLED_OR_COMPLETED_ON_CREATE =
            "Project status cannot be 'Cancelled' or 'Completed' when creating a new project.";
    public static final String USER_NOT_AUTHENTICATED = "User is not authenticated.";
    public static final String DEPARTMENT_NAME_BLANK = "Department name cannot be blank.";
    public static final String DEPARTMENT_NAME_SIZE = "Department name must be between 1 and 50 characters.";
    public static final String DEPARTMENT_ALREADY_EXISTS = "Department already exists.";

    public static final String FILE_UPLOAD_FAILED = "File upload failed";
    public static final String FILE_DELETE_FAILED = "File deletion failed";

    // Business Rules
    public static final String STATUS_ALREADY_COMPLETED = "Completed tasks cannot be modified";
    public static final String STATUS_TRANSITION_INVALID = "Invalid status transition";
    public static final String STATUS_REASON_REQUIRED = "A reason must be provided for Cancelled or Blocked status";
    public static final String STATUS_SAME_AS_CURRENT = "The new status is the same as the current status";


    private ErrorMessages() {}

}
