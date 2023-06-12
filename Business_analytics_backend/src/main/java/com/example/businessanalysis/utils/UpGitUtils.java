package com.example.businessanalysis.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * @ClassName UploadGiteeImgUtils
 * @Description 上传图片到gitee
 * @Author 74707
 * @Date 2022/11/10 8:25
 * @Version V1.0
 */
public class UpGitUtils {

  public static final String ACCESS_TOKEN = "f31b499e78915b25e7e7dc8d5b35a8bc";
  public static final String OWNER = "EchoAndNova";
  public static final String REPO = "picture-bed";
  public static final String USERPATH = "uploadUserimg/" + DateUtil.format(new Date(), "YYYY-MM-dd") + "/";
  public static final String GOODPATH = "uploadGoodImg/" + DateUtil.format(new Date(), "YYYY-MM-dd") + "/";

  /**
   * 用于提交描述
   */
  public static final String ADD_MESSAGE = "add img";
  public static final String DEL_MESSAGE = "DEL img";

  public static final String API_CREATE_POST = "https://gitee.com/api/v5/repos/%s/%s/contents/%s";

  /**
   * 生成创建(获取 、 删除)的指定文件路径
   *
   * @param originalFilename
   */
  public static String createUploadFileUrl(String originalFilename,String dir) {
    //获取文件后缀
    String suffix = originalFilename.contains(".") ?
        originalFilename.substring(originalFilename.indexOf('.')) : null;
    //拼接存储的图片名称 通过时间戳和UUID重新命名文件
    String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + suffix;
    //填充请求路径
    String url = String.format(UpGitUtils.API_CREATE_POST,
        UpGitUtils.OWNER,
        UpGitUtils.REPO,
        dir + fileName);
    return url;
  }

  /**
   * 获取创建文件的请求体map集合：access_token、message、content
   *
   * @param multipartFile 文件字节数组
   * @return 封装成map的请求体集合
   */
  public static Map<String, Object> getUploadBodyMap(byte[] multipartFile) {
    HashMap<String, Object> bodyMap = new HashMap<>(3);
    bodyMap.put("access_token", UpGitUtils.ACCESS_TOKEN);
    bodyMap.put("message", UpGitUtils.ADD_MESSAGE);
    bodyMap.put("content", Base64.encode(multipartFile));
    return bodyMap;
  }

}
