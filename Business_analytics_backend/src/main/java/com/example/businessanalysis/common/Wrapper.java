package com.example.businessanalysis.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

/**
 * @ClassName Wrapper
 * @Description 返回包装类
 * @Author ljq
 * @Date 2022/10/13 15:38
 * @Version V1.0
 */
public class Wrapper<T> implements Serializable {

  /**
   * 成功码.
   */
  public static final int SUCCESS_CODE = 200;

  /**
   * 成功信息.
   */
  public static final String SUCCESS_MESSAGE = "操作成功";

  /**
   * 错误码.
   */
  public static final int ERROR_CODE = 500;

  /**
   * 错误信息.
   */
  public static final String ERROR_MESSAGE = "结果为空值或系统有误";

  /**
   * 错误码：参数非法
   */
  public static final int ILLEGAL_ARGUMENT_CODE_ = 400;

  /**
   * 错误信息：参数非法
   */
  public static final String ILLEGAL_ARGUMENT_MESSAGE = "请求参数非法，请核查！";

  /**
   * 错误码：参数非法
   */
  public static final int AUTHORIZATION_CODE = 403;

  /**
   * 错误信息：参数非法
   */
  public static final String AUTHORIZATION_MESSAGE = "用户凭证已过期，请重新登录！";

  /**
   * 编号.
   */
  private int code;

  /**
   * 信息.
   */
  private String message;

  /**
   * 结果数据
   */
  private T result;

  /**
   * @Description Instantiates a new wrapper. default code=200
   */
  public Wrapper() {
    this(SUCCESS_CODE, SUCCESS_MESSAGE);
  }

  /**
   * @param code    the code
   * @param message the message
   * @Description Instantiates a new wrapper.
   */
  public Wrapper(int code, String message) {
    this.code(code).message(message);
  }

  /**
   * @param code    the code
   * @param message the message
   * @param result  the result
   * @Description Instantiates a new wrapper.
   */
  public Wrapper(int code, String message, T result) {
    super();
    this.code(code).message(message).result(result);
  }

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

  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }

  public Wrapper<T> code(int code) {
    this.setCode(code);
    return this;
  }

  /**
   * @param message the new 信息
   * @return the wrapper
   * @Description 设置信息 ，返回自身的引用.
   */
  public Wrapper<T> message(String message) {
    this.setMessage(message);
    return this;
  }

  /**
   * @param result the new 结果数据
   * @return the wrapper
   * @Description 结果数据，返回自身的引用.
   */
  public Wrapper<T> result(T result) {
    this.setResult(result);
    return this;
  }

  @Override
  public String toString() {
    return "Wrapper{" +
        "code=" + code +
        ", message='" + message + '\'' +
        ", result=" + result +
        '}';
  }

  @JsonIgnore
  public boolean isSuccess() {
    return this.getCode() == Wrapper.SUCCESS_CODE;
  }

  @JsonIgnore
  public boolean isFail() {
    return !isSuccess();
  }

}
