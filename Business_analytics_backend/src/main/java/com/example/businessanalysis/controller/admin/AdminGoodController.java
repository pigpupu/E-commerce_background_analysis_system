package com.example.businessanalysis.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.businessanalysis.common.WrapMapper;
import com.example.businessanalysis.common.Wrapper;
import com.example.businessanalysis.domain.Brand;
import com.example.businessanalysis.domain.Category;
import com.example.businessanalysis.domain.Good;
import com.example.businessanalysis.mapper.BrandMapper;
import com.example.businessanalysis.mapper.CategoryMapper;
import com.example.businessanalysis.service.GoodService;
import com.example.businessanalysis.vo.GoodVo;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName AdminGoodController
 * @Description 管理员商品管理
 * @Author 74707
 * @Date 2022/11/20 16:19
 * @Version V1.0
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/admin/good")
public class AdminGoodController {

  @Resource
  public GoodService goodService;

  @Resource
  public CategoryMapper categoryMapper;

  @Resource
  public BrandMapper brandMapper;

  /**
   * 查询所有商品信息
   * @param nowPage
   * @param pageSize
   * @return
   */
  @RequestMapping(value = "/queryAll")
  public Wrapper<Map<String ,Object>> queryAll(Integer nowPage, Integer pageSize) {
    Map<String, Object> res = goodService.queryAllByPage(nowPage, pageSize);
    return WrapMapper.wrap(res);
  }

  /**
   * 通过商品名称查询商品（暂时商品可以重名，但没分页）
   * @param goodName
   * @return
   */
  @RequestMapping(value = "/queryByName")
  public Wrapper<List<GoodVo>> queryByName(String goodName){
    List<GoodVo> res=goodService.queryByName(goodName);
    return WrapMapper.wrap(res);
  }

  /**
   * 条件查询分页商品 id或者编号 + 价格范围升序
   * @param nowPage
   * @param pageSize
   * @param type 为0是按id（模糊） 为1是按name（模糊）
   * @param input
   * @param mini
   * @param maxi
   * @return
   */
  @RequestMapping(value = "/querySelect")
  public Wrapper<Map<String ,Object>> querySelect(Integer nowPage,Integer pageSize,Integer type,
      String input,Integer mini,Integer maxi){
    Map<String ,Object> res=goodService.querySelect(nowPage,pageSize,type,input,mini,maxi);
    return WrapMapper.wrap(res);
  }


  /**
   * 得到所有种类键值对（之后会写分级别获取）
   * @return
   */
  @RequestMapping(value = "/queryAllCategory")
  public List<Map<String,Object>> queryAllCategory(){
    LambdaQueryWrapper<Category>lqw = Wrappers.<Category>lambdaQuery().
        select(Category::getCategoryName,Category::getId).orderByAsc(Category::getId);
    return categoryMapper.selectMaps(lqw);
  }

  /**
   * 得到所有品牌键值对
   * @return
   */
  @RequestMapping(value = "/queryAllBrand")
  public List<Map<String,Object>> queryAllBrand(){
    LambdaQueryWrapper<Brand>lqw = Wrappers.<Brand>lambdaQuery().
        select(Brand::getBrandName,Brand::getId).orderByAsc(Brand::getId);
    return brandMapper.selectMaps(lqw);
  }

  /**
   * 创建新商品 a.下拉框选中商品种类和品牌（成为good对象一部分）
   * b.或者直接输入商品的品牌名称（系统处理，无则新建）
   * @param good
   * @param newBrandName
   * @param multipartFile
   * @return
   */
  @RequestMapping(value = "/create")
  public Wrapper<Boolean> create(Good good,String newBrandName,MultipartFile multipartFile){
    Boolean res=goodService.createGood(good,newBrandName,multipartFile);
    return WrapMapper.wrap(res);
  }

  /**
   * 更新商品 值可以为空不修改 需要商品id（因为商品名称可以修改）（queryOne和queryAll都返回这个值）
   * 可以修改商品的名称并且不与其他商品重复
   * @param good
   * @param multipartFile
   * @return
   */
  @RequestMapping(value = "/update")
  public Wrapper<Boolean> update(Good good, MultipartFile multipartFile){
    Boolean res=goodService.updateGood(good,multipartFile);
    return WrapMapper.wrap(res);
  }

  /**
   * 批量删除，传入商品id的数组
   * @param Ids
   * @return
   */
  @RequestMapping(value = "/deleteByIds")
  public Wrapper<Boolean> deleteByIds(String[] Ids){
    Boolean res=goodService.deleteGoodByIds(Arrays.asList(Ids));
    return WrapMapper.wrap(res);
  }

  /**
   * 批量删除，传入商品名称的数组
   * @param goodNames
   * @return
   */
  @RequestMapping(value = "/deleteByNames")
  public Wrapper<Boolean> deleteByNames(String[] goodNames){
    Boolean res=goodService.deleteGoodByNames(Arrays.asList(goodNames));
    return WrapMapper.wrap(res);
  }

  /**
   * 导出所有的商品信息excel,包含图片的Url地址
   * @return
   */
  @RequestMapping(value="/download")
  public ResponseEntity<FileSystemResource> download(){
    ResponseEntity<FileSystemResource> res=goodService.download();
    return res;
  }
}
