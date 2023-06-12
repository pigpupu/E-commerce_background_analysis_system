package com.example.businessanalysis.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.api.domain.GoodsDetail;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.businessanalysis.common.exception.BusinessException;
import com.example.businessanalysis.domain.Brand;
import com.example.businessanalysis.domain.Category;
import com.example.businessanalysis.domain.Good;
import com.example.businessanalysis.mapper.BrandMapper;
import com.example.businessanalysis.mapper.CategoryMapper;
import com.example.businessanalysis.mapper.GoodMapper;
import com.example.businessanalysis.service.GoodService;
import com.example.businessanalysis.utils.MyExcelUtils;
import com.example.businessanalysis.utils.UpGitUtils;
import com.example.businessanalysis.vo.GoodVo;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 74707
 * @description 针对表【t_good(商品表)】的数据库操作Service实现
 * @createDate 2022-10-26 12:01:20
 */
@Service
public class GoodServiceImpl extends ServiceImpl<GoodMapper, Good>
    implements GoodService {

  @Resource
  public GoodMapper goodMapper;

  @Resource
  public BrandMapper brandMapper;

  @Resource
  public CategoryMapper categoryMapper;

  private String getGoodeImageUrl(MultipartFile multipartFile) {
    String targetUrl = UpGitUtils.createUploadFileUrl(multipartFile.getOriginalFilename(),
        UpGitUtils.GOODPATH);
    Map<String, Object> postBody = new HashMap<>();
    try {
      postBody = UpGitUtils.getUploadBodyMap(multipartFile.getBytes());
    }catch (Exception e){
      throw new BusinessException("图片解析服务错误，请稍后再试");
    }
    String JSONResult = HttpUtil.post(targetUrl, postBody);
    JSONObject jsonObj = JSONUtil.parseObj(JSONResult);
    if (jsonObj == null || jsonObj.getObj("commit") == null) {
      throw new BusinessException("图片上传服务错误，请稍后再试");
    }
    JSONObject content = JSONUtil.parseObj(jsonObj.getObj("content"));
    String downloadUrl = content.getObj("download_url").toString();
    return downloadUrl;
  }

  @Override
  public List<String> findAllGoodIdByCatId(List<Integer> ALlFitCategoryId) {
    return this.list(Wrappers.<Good>lambdaQuery().in(ALlFitCategoryId.size() > 0,
            Good::getCategoryId, ALlFitCategoryId)).stream().map(Good::getId)
        .collect(Collectors.toList());
  }

  @Override
  public List<GoodVo> queryAllNoPage() {
    List<Good> re = goodMapper.selectList(Wrappers.<Good>lambdaQuery().
        eq(Good::getDelFlag, 0));
    if (CollectionUtil.isEmpty(re)) {
      throw new BusinessException("系统数据无此类商品");
    }
    List<GoodVo> res = re.stream().map(v -> new GoodVo(v)).collect(Collectors.toList());
    return res;
  }

  @Override
  public Map<String, Object> queryAllByPage(Integer nowPage, Integer pageSize) {
    Map<String, Object> re = new HashMap<>();
    List<GoodVo> res;
    IPage<Good> raw = goodMapper.selectPage(new Page<>(nowPage, pageSize),
        Wrappers.<Good>lambdaQuery().eq(Good::getDelFlag, 0));
    if (Objects.isNull(raw) || CollectionUtil.isEmpty(raw.getRecords())) {
      throw new BusinessException("目前暂无该需求的商品信息");
    }
    System.out.println("record" + raw.getRecords());
    res = raw.getRecords().stream().map(v -> new GoodVo(v)).collect(Collectors.toList());
    re.put("total", raw.getTotal());
    re.put("pages", raw.getPages());
    re.put("pageSize", raw.getSize());
    re.put("current", raw.getCurrent());
    re.put("records", res);
    return re;
  }

  @Override
  public List<GoodVo> queryByName(String goodName) {
    List<Good> rawRes = goodMapper.selectList(
        Wrappers.<Good>lambdaQuery().eq(Good::getGoodName, goodName).
            eq(Good::getDelFlag, 0));
    if (CollectionUtil.isEmpty(rawRes)) {
      throw new BusinessException("目前暂无该需求的商品信息");
    }
    return rawRes.stream().map(u -> new GoodVo(u)).collect(Collectors.toList());
  }

  public Map<String, Object> querySelect(Integer nowPage, Integer pageSize,
      Integer type, String inputStr, Integer mini, Integer maxi) {
    Map<String, Object> re = new HashMap<>();
    List<GoodVo> res = new ArrayList<>();
    IPage<Good> raw = new Page<Good>();
    //商品编号
    LambdaQueryWrapper lqw = new LambdaQueryWrapper();

    //inputStr不为空时，type=0时通过商品id号（模糊） type=1时通过商品名称（模糊）
    if (StringUtils.isNotBlank(inputStr)) {
      if (type == 0) {
        if (mini != null && maxi != null) {
          lqw = Wrappers.<Good>lambdaQuery().like(Good::getId, inputStr).
              between(Good::getPrice, mini, maxi).orderByAsc(Good::getPrice)
              .eq(Good::getDelFlag, 0);
        } else if (mini != null) {
          lqw = Wrappers.<Good>lambdaQuery().like(Good::getId, inputStr).
              ge(Good::getPrice, mini).orderByAsc(Good::getPrice).eq(Good::getDelFlag, 0);
        } else if (maxi != null) {
          lqw = Wrappers.<Good>lambdaQuery().like(Good::getId, inputStr).
              le(Good::getPrice, maxi).orderByAsc(Good::getPrice).eq(Good::getDelFlag, 0);
        } else {
          lqw = Wrappers.<Good>lambdaQuery().like(Good::getId, inputStr).orderByAsc(Good::getPrice)
              .eq(Good::getDelFlag, 0);
        }
      }
      //type=1时通过商品名称（模糊）
      else if (type == 1) {
        if (mini != null && maxi != null) {
          lqw = Wrappers.<Good>lambdaQuery().like(Good::getGoodName, inputStr).
              between(Good::getPrice, mini, maxi).orderByAsc(Good::getPrice)
              .eq(Good::getDelFlag, 0);
        } else if (mini != null) {
          lqw = Wrappers.<Good>lambdaQuery().like(Good::getGoodName, inputStr).
              ge(Good::getPrice, mini).orderByAsc(Good::getPrice).eq(Good::getDelFlag, 0);
        } else if (maxi != null) {
          lqw = Wrappers.<Good>lambdaQuery().like(Good::getGoodName, inputStr).
              le(Good::getPrice, maxi).orderByAsc(Good::getPrice).eq(Good::getDelFlag, 0);
        } else {
          lqw = Wrappers.<Good>lambdaQuery().like(Good::getGoodName, inputStr)
              .orderByAsc(Good::getPrice)
              .eq(Good::getDelFlag, 0);
        }
      }
    }
    //inputStr为空时 直接看价格区间
    else {
      if (mini != null && maxi != null) {
        lqw = Wrappers.<Good>lambdaQuery().between(Good::getPrice, mini, maxi)
            .orderByAsc(Good::getPrice)
            .eq(Good::getDelFlag, 0);
      } else if (mini != null) {
        lqw = Wrappers.<Good>lambdaQuery().ge(Good::getPrice, mini).orderByAsc(Good::getPrice)
            .eq(Good::getDelFlag, 0);
      } else if (maxi != null) {
        lqw = Wrappers.<Good>lambdaQuery().le(Good::getPrice, maxi).orderByAsc(Good::getPrice)
            .eq(Good::getDelFlag, 0);
      }
    }

    raw = goodMapper.selectPage(new Page<>(nowPage, pageSize), lqw);
    if (Objects.isNull(raw) || CollectionUtil.isEmpty(raw.getRecords())) {
      throw new BusinessException("目前暂无该需求的商品信息");
    }
    System.out.println("record" + raw.getRecords());
    res = raw.getRecords().stream().map(v -> new GoodVo(v)).collect(Collectors.toList());
    re.put("total", raw.getTotal());
    re.put("pages", raw.getPages());
    re.put("pageSize", raw.getSize());
    re.put("current", raw.getCurrent());
    re.put("records", res);
    return re;
  }

  @Override
  public Boolean createGood(Good good, String newBrandName, MultipartFile multipartFile) {
    Good newGood = new Good();

//      改成不可以创建同名商品
//    Good conflict = goodMapper.selectOne(Wrappers.<Good>lambdaQuery().
//        eq(Good::getGoodName, good.getGoodName()).eq(Good::getDelFlag, 0));
//    if (conflict != null) {
//      return false;
//    }

    /**--处理新创建商品的品牌信息--**/
    //如果传入的信息自带了brandName（下拉框）--》优先
    if (StringUtils.isNotBlank(good.getBrandName())) {
      List<Brand> re = brandMapper.selectList(Wrappers.<Brand>lambdaQuery().
          eq(Brand::getBrandName, good.getBrandName()).eq(Brand::getDelFlag, 0));
      if (CollectionUtil.isNotEmpty(re)) {
        newGood.setBrandId(re.get(0).getId());
      }
    } //如果传入的信息来自输入框
    else {
      Brand newbrand = new Brand();
      if (StringUtils.isNotBlank(newBrandName)) {
        newbrand.setBrandName(newBrandName);
        newbrand.setAddTime(new Date());
        List<Brand> sameNameList = brandMapper.selectList
            (Wrappers.<Brand>lambdaQuery().eq(Brand::getBrandName, newBrandName));
        //这里我们现在品牌不能重名，所有最多只会找到1个，确实是get0
        //所写的品牌名称在列表中和不在列表
        if (CollectionUtil.isNotEmpty(sameNameList)) {
          newGood.setBrandId(sameNameList.get(0).getId());
        } else {
          brandMapper.insert(newbrand);
          List<Brand> brandList = brandMapper.selectList(
              Wrappers.<Brand>lambdaQuery().eq(Brand::getBrandName, newBrandName)
                  .eq(Brand::getDelFlag, 0));
          if (CollectionUtil.isNotEmpty(brandList)) {
            newGood.setBrandId(brandList.get(0).getId());
          }
        }
      }
    }
    /**--处理新创建商品的种类信息--**/
    if (StringUtils.isNotBlank(good.getCategoryName())) {
      List<Category> re = categoryMapper.selectList(Wrappers.<Category>
          lambdaQuery().eq(Category::getCategoryName, good.getCategoryName()));
      if (CollectionUtil.isNotEmpty(re)) {
        newGood.setCategoryId(re.get(0).getId());
      }

    }
    /**--处理新创建商品的其他信息--**/
    newGood.setId(IdUtil.fastSimpleUUID());
    newGood.setGoodName(good.getGoodName());
    newGood.setPrice(good.getPrice());
    newGood.setProductHeightCm(good.getProductHeightCm());
    newGood.setProductLengthCm(good.getProductLengthCm());
    newGood.setProductWidthCm(good.getProductWidthCm());
    newGood.setProductWeightG(good.getProductWeightG());
    newGood.setAddTime(new Date());
    if (multipartFile != null) {
      newGood.setImage(getGoodeImageUrl(multipartFile));
    }
    newGood.setDelFlag(0);
    /**--插入新商品--**/
    Integer res = goodMapper.insert(newGood);
    return res != 0;
  }

  @Override
  public Boolean updateGood(Good good, MultipartFile multipartFile){
    Good updateGood = new Good();
    /**--处理更改商品的品牌信息--**/
    if (StringUtils.isNotBlank(good.getBrandName())) {
      List<Brand> brandList = brandMapper.selectList(Wrappers.<Brand>lambdaQuery().
          eq(Brand::getBrandName, good.getBrandName()).eq(Brand::getDelFlag, 0));
      if (CollectionUtil.isNotEmpty(brandList)) {
        updateGood.setBrandId(brandList.get(0).getId());
      }
    }
    /**--处理更改商品的种类信息--**/
    if (StringUtils.isNotBlank(good.getCategoryName())) {
      List<Category> categoryList = categoryMapper.selectList(Wrappers.<Category>
          lambdaQuery().eq(Category::getCategoryName, good.getCategoryName()));
      if (CollectionUtil.isNotEmpty(categoryList)) {
        updateGood.setCategoryId(categoryList.get(0).getId());
      }
    }
    /**--处理更改商品的其他信息（可改名）--**/
    updateGood.setGoodName(good.getGoodName());
    updateGood.setPrice(good.getPrice());
    updateGood.setProductHeightCm(good.getProductHeightCm());
    updateGood.setProductLengthCm(good.getProductLengthCm());
    updateGood.setProductWidthCm(good.getProductWidthCm());
    updateGood.setProductWeightG(good.getProductWeightG());
    updateGood.setUpdateTime(new Date());
    if (multipartFile != null) {
      updateGood.setImage(getGoodeImageUrl(multipartFile));
    }
    /**--通过id更新商品--**/
    Integer res = goodMapper.update(updateGood,
        Wrappers.<Good>lambdaUpdate().eq(Good::getId, good.getId()).
            eq(Good::getDelFlag, 0));
    return res != 0;
  }

  @Override
  public Boolean deleteGoodByNames(List<String> goodNames) {
    if (CollectionUtil.isEmpty(goodNames)) {
      throw new BusinessException("提交了为空的删除项");
    }
    Good delGood = new Good();
    delGood.setDelFlag(1);
    Integer res = goodMapper.update(delGood,
        Wrappers.<Good>lambdaQuery().in(Good::getGoodName, goodNames).eq(Good::getDelFlag, 0));
    return res != 0;
  }

  @Override
  public Boolean deleteGoodByIds(List<String> Ids) {
    if (CollectionUtil.isEmpty(Ids)) {
      throw new BusinessException("提交了为空的删除项");
    }
    Good delGood = new Good();
    delGood.setDelFlag(1);
    Integer res = goodMapper.update(delGood,
        Wrappers.<Good>lambdaQuery().in(Good::getId, Ids).eq(Good::getDelFlag, 0));
    return res != 0;
  }

  @Override
  public ResponseEntity<FileSystemResource> download() {
    List<GoodVo> queryAll = queryAllNoPage();
    if (CollectionUtil.isEmpty(queryAll)) {
      throw new BusinessException("系统中没有可打印的内容");
    }
    List<Map<String, Object>> list = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    for (GoodVo goodVo : queryAll) {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("商品id", goodVo.getId());
      map.put("商品名称", goodVo.getGoodName());
      map.put("商品类别", goodVo.getCategoryName());
      //商品长宽高懒得写~~~~
      map.put("品牌名称", goodVo.getBrandName());
      map.put("商品价格", goodVo.getPrice());
      map.put("图片地址", goodVo.getImage());
      map.put("上次更新时间", Optional.ofNullable(goodVo.getUpdateTime()).
          map(u -> sdf.format(u)).orElse("未修改"));
      list.add(map);
    }
    ResponseEntity<FileSystemResource> res = MyExcelUtils.downloadExcel(list);
    return res;
  }
}