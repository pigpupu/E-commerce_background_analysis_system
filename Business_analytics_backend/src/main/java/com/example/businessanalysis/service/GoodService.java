package com.example.businessanalysis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.businessanalysis.domain.Good;
import com.example.businessanalysis.vo.GoodVo;
import com.example.businessanalysis.vo.UserVo;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xpath.operations.Bool;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 74707
 * @description 针对表【t_good(商品表)】的数据库操作Service
 * @createDate 2022-10-26 12:01:20
 */
public interface GoodService extends IService<Good> {

  /**
   * 一个类别下（包含子类）的商品id
   *
   * @param ALlFitCategoryId
   * @return List<Integer> 所有满足的商品id
   */
  List<String> findAllGoodIdByCatId(List<Integer> ALlFitCategoryId);

  /**
   * 查询所有商品信息用于导出
   * @return
   */
  List<GoodVo> queryAllNoPage();

  /**
   * 所有商品的信息 返回的内容中包含了商品名字，商品的删除需要商品名字
   * @param nowPage
   * @param pageSize
   * @return
   */
  Map<String, Object> queryAllByPage(Integer nowPage,Integer pageSize);

  /**
   * 通过商品名称获取一个商品的信息，用于更改时显示的初始值？？
   * @param goodName
   * @return
   */
  List<GoodVo> queryByName(String goodName);

  /**
   * 按照价格姓名等元素选择查询
   * @param nowPage
   * @param pageSize
   * @param type
   * @param input
   * @param mini
   * @param maxi
   * @return
   */
  Map<String, Object> querySelect(Integer nowPage,Integer pageSize,Integer type,
      String input,Integer mini,Integer maxi);

  /**
   * 创造商品，已经防止商品重名
   * @param good
   * @param multipartFile
   * @param newBrandName
   * @return
   * @throws Exception
   */
  Boolean createGood(Good good,String newBrandName,MultipartFile multipartFile);

  /**
   * 更新商品 已经防止重名
   * @param good
   * @param multipartFile
   * @return
   * @throws Exception
   */
  Boolean updateGood(Good good,MultipartFile multipartFile);

  /**
   *
   * @param Ids
   * @return
   */
  public Boolean deleteGoodByIds(List<String> Ids);

  /**
   *
   * @param goodNames
   * @return
   */
  public Boolean deleteGoodByNames(List<String> goodNames);

  /**
   * 导出所有商品的信息
   */
  public ResponseEntity<FileSystemResource> download();

}
