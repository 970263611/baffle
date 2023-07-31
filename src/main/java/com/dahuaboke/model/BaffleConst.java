package com.dahuaboke.model;

/**
 * @author dahua
 * @time 2023/7/20 18:23
 */
public class BaffleConst {

    public static final String BAFFLE_APPOINT_IP_PORT = "baffle-appoint-ip-port";

    public static final String HTTP_PREFIX = "http://";

    public static final String HTTPS_PREFIX = "https://";

    public static final String SYMBOL_SLASH = "/";

    public static final String SUFFIX_YAML = ".yaml";

    public static final String SUFFIX_YML = ".yml";

    public static final String FORM_SPLIT = "boundary=";

    public static final String STR_FILENAME = "filename";

    public static final String STR_NAME = "name";

    public static final String BAFFLE_BASE_DIR = System.getProperty("user.home") + "/baffle/";

    public static final String EXCEPTION_TIMEOUT_MESSAGE = "异常：请求超时";

    public static final String EXCEPTION_NOT_ALLOW_METHOD_MESSAGE = "异常：该请求方式暂不支持";

    public static final String EXCEPTION_NOT_ALLOW_RESOURCES_MESSAGE = "异常：请勿访问非法资源";

    public static final String EXCEPTION_ERROR_PARAM_MESSAGE = "异常：请求参数错误";

    public static final String EXCEPTION_PARSE_JSON_MESSAGE = "异常：参数解析为JSON时出现错误";

    public static final String EXCEPTION_INTERRUPTED_MESSAGE = "异常：线程打断异常";

    public static final String EXCEPTION_EXECUTION_MESSAGE = "异常：线程提交异常";
}
