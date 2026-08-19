package com.ruoyi.common.core.domain;

import java.util.HashMap;
import com.ruoyi.common.utils.StringUtils;

/**
 * 小程序接口返回结果（与AjaxResult区分：code 0表示成功，非0表示失败，字段为message）
 *
 * @author ruoyi
 */
public class ApiResult extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /** 成功状态码 */
    public static final int CODE_SUCCESS = 0;

    /** 默认失败状态码 */
    public static final int CODE_FAIL = 500;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "message";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 ApiResult 对象，使其表示一个空消息。
     */
    public ApiResult()
    {
    }

    /**
     * 初始化一个新创建的 ApiResult 对象
     *
     * @param code 状态码
     * @param message 返回内容
     */
    public ApiResult(int code, String message)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, message);
    }

    /**
     * 初始化一个新创建的 ApiResult 对象
     *
     * @param code 状态码
     * @param message 返回内容
     * @param data 数据对象
     */
    public ApiResult(int code, String message, Object data)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, message);
        if (StringUtils.isNotNull(data))
        {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static ApiResult success()
    {
        return ApiResult.success(null);
    }

    /**
     * 返回成功数据
     *
     * @param data 数据对象
     * @return 成功消息
     */
    public static ApiResult success(Object data)
    {
        return ApiResult.success("ok", data);
    }

    /**
     * 返回成功消息
     *
     * @param message 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static ApiResult success(String message, Object data)
    {
        return new ApiResult(CODE_SUCCESS, message, data);
    }

    /**
     * 返回错误消息
     *
     * @param message 返回内容
     * @return 错误消息
     */
    public static ApiResult error(String message)
    {
        return ApiResult.error(CODE_FAIL, message);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param message 返回内容
     * @return 错误消息
     */
    public static ApiResult error(int code, String message)
    {
        return new ApiResult(code, message, null);
    }

    /**
     * 是否为成功消息
     *
     * @return 结果
     */
    public boolean isSuccess()
    {
        return Integer.valueOf(CODE_SUCCESS).equals(this.get(CODE_TAG));
    }

    /**
     * 方便链式调用
     *
     * @param key 键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public ApiResult put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }
}
