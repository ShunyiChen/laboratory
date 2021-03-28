package com.shunyi.laboratory.schema;

/**
 * @author Jiansheng.Lin
 */
public class SysConstants {

    private static final String UTILITY_CLASS = "Utility class";

    public static final String THREAD_POOL_NAME = "taskExecutorPool";

    public static final String EVENTLOGPREFIX = "{},";

    private SysConstants() {
        throw new IllegalStateException(UTILITY_CLASS);
    }

    /**
     * Session
     */
    public static class Session {

        private Session() {
            throw new IllegalStateException(UTILITY_CLASS);
        }

        public static final String LOGIN_ENTITY = "UserObject";
        public static final String LOGIN_MENU = "UesrMenu";
        public static final String RANDOM_NUM = "RandomNum";
    }

    public static class Response {

        private Response() {
            throw new IllegalStateException(UTILITY_CLASS);
        }

        public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
        public static final String CONTENT_TYPE_TEXT = "text/html";
        public static final String CONTENT_TYPE_PLAIN = "text/plain;charset=utf-8;imeanit=yes";
        public static final String CONTENT_TYPE_OCTET = "application/octet-stream";
        public static final String CONTENT_DISPOSITION_INLINE = "inline";
        public static final String CONTENT_DISPOSITION_ATTACHMENT = "attachment";
    }

    public static class HttpStatus {

        private HttpStatus() {
            throw new IllegalStateException(UTILITY_CLASS);
        }

        public static final String FAILED = "failed";
        public static final String SUCCESS = "success";
        public static final String EXCEPTION = "exceptions";
    }

    public static class HttpCode {

        private HttpCode() {
            throw new IllegalStateException(UTILITY_CLASS);
        }

        public static final int USER_EXISTS_CODE = 501;
        public static final int USER_DISABLED = 503;
        public static final int SESSION_TIMEOUT_CODE = 502;
        public static final int EXCEPTION_CODE = 500;
        public static final int SUCCESS_CODE = 200;
        public static final int RANDOM_CODE = 201;
        public static final int FALSE_CODE = 202;
        public static final int FAILED_CODE = 300;
        public static final int VALIDATE_FAILED_CODE = 301;
        public static final int PARAM_ERROR = 400;
    }

    public static class BusinessType {

        private BusinessType() {
            throw new IllegalStateException(UTILITY_CLASS);
        }

        public static final String HEALTH_CHECK = "HealthCheck";
        public static final String LOG_COLLECTION = "LogCollection";
        public static final String CONFIG_CHANGE = "ConfigChange";
        public static final String BACKUP = "Backup";
        public static final String RESTORE = "Restore";
        public static final String AUTOMATED_TESTING = "AutomatedTesting";
        public static final String CONFIG_TASK = "ConfigTask";
    }

    public static class SchedulerType {

        private SchedulerType() {
            throw new IllegalStateException(UTILITY_CLASS);
        }

        public static final String ONETIME = "Onetime";
    }

    public static class ExceptionType {

        private ExceptionType() {
            throw new IllegalStateException(UTILITY_CLASS);
        }

        public static final String NO_AUTHOR_EXCEPTION = "No authorize to do this operation";
        public static final String DOMAIN_FORBIDDEN = "Domain Forbidden";
        public static final String SAME_USER_EXCEPTION = "The number of current user session has reached its maximum value: 8, Please try again later.";
        public static final String ALL_USER_EXCEPTION = "The number of all user session has reached system's maximum value: 64, Please try again later.";
        public static final String NOT_EXIST_IN_DB = "The original %s [%s] was not found in the DB";
    }

    public static class SpecialWords {

        private SpecialWords() {
            throw new IllegalStateException(UTILITY_CLASS);
        }
        public static final String ENABLED = "enabled";
        public static final String DISABLED = "disabled";
        public static final String YES = "[yes]";
        public static final String NO = "[no]";
        public static final String ALL = "all";
    }

    public static class LLD {
        private LLD() {
            throw new IllegalStateException(UTILITY_CLASS);
        }
        public static final String GROUP_MANAGEMENT = "Group Management";
        public static final String USER_MANAGEMENT = "User Management";
        public static final String PARAMETER_MANAGEMENT = "Parameter Management";
        public static final String NETWORK_FUNCTION_MANAGEMENT = "Network Function Management";
        public static final String NODE_MANAGEMENT = "Node Management";
        public static final String NODE_CREDENTIAL_MANAGEMENT = "Node Credential Management";
        public static final String SCHEDULER_MANAGEMENT = "Scheduler Management";
        public static final String SHEET_NAME = "Configuration Management LLD";
    }

    public static class Commands {
        private Commands() {
            throw new IllegalStateException(UTILITY_CLASS);
        }
        public static final String SCHEMA_VALIDATION_CMD = "yang2dsdl -b ericsson -x -j -v %s $(ls *.yang | grep -vf <(grep -l belongs-to *.yang))";
        public static final String NODE_VALIDATION_CMD = "python node_verification.py %s";
        public static final String STOP_TC_CMD = "python engmain.py %s";
    }

    public static class NodeVerificationType {
        private NodeVerificationType() {
            throw new IllegalStateException(UTILITY_CLASS);
        }
        public static final String SUCCESS = "Success";
        public static final String FAIL = "Fail";
        public static final String ONGOING = "Ongoing";
    }
}
