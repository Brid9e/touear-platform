package com.touear.common.utils;

public class R<T> {
    private int code;       // 状态码
    private String message; // 提示信息
    private T data;         // 具体数据

    // 默认构造函数
    public R() {
    }

    // 带参数的构造函数
    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 返回成功的响应
    public static <T> R<T> data(T data) {
        return new R<>(200, "操作成功", data);
    }

    public static <T> R<T> success(String message) {
        return new R<>(200, message,null);
    }

    public static <T> R<T> success(int code,String message) {
        return new R<>(code, message,null);
    }



    // 返回失败的响应
    public static <T> R<T> fail(int code, String message) {
        return new R<>(code, message, null);
    }

    // 错误响应，使用默认错误码 400
    public static <T> R<T> fail(String message) {
        return new R<>(400, message, null);
    }

    // Getter 和 Setter 方法
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "R{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
