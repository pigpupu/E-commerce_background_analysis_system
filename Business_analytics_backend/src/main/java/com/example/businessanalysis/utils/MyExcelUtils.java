package com.example.businessanalysis.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * @ClassName MyExcelUtils
 * @Description 生成excel
 * @Author 74707
 * @Date 2022/11/21 22:27
 * @Version V1.0
 */
public class MyExcelUtils {

  public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;

  /**
   * 传的参数为任意的List<Map<String, Object>> list 生成对应的excel
   * @param list
   * @return
   */
  public static ResponseEntity<FileSystemResource> downloadExcel(List<Map<String, Object>> list){
    String tempPath = SYS_TEM_DIR + IdUtil.fastSimpleUUID() + ".xlsx";
    File file =new File(tempPath);
    BigExcelWriter writer = ExcelUtil.getBigWriter(file);
    writer.write(list,true);
    writer.autoSizeColumnAll();
    writer.flush(file);
    HttpHeaders headers= new HttpHeaders();
    headers.add("Content-Disposition","attachment; filename=file.xlsx");
    ResponseEntity<FileSystemResource> res= ResponseEntity.ok().headers(headers).
        contentType(MediaType.parseMediaType("application/octet-stream")).contentLength(file.length())
        .body(new FileSystemResource((file)));
    file.deleteOnExit();
    return res;
  }
}
