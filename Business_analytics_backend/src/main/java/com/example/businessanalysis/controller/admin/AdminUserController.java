package com.example.businessanalysis.controller.admin;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.businessanalysis.common.WrapMapper;
import com.example.businessanalysis.common.Wrapper;
import com.example.businessanalysis.common.exception.BusinessException;
import com.example.businessanalysis.domain.User;
import com.example.businessanalysis.mapper.UserMapper;
import com.example.businessanalysis.service.UserService;
import com.example.businessanalysis.vo.UserVo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AdminUserController
 * @Description 管理员管理用户
 * @Author 74707
 * @Date 2022/11/19 14:03
 * @Version V1.0
 */

@CrossOrigin
@RestController
@RequestMapping(value = "/admin/user")
public class AdminUserController {

  @Resource
  private UserMapper userMapper;
  @Resource
  private UserService userService;

  /**
   * 分页的用户信息，需要参数 当前页 每页条数 类型（普通，高级，全部）
   * @param nowPage
   * @param pageSize
   * @param type
   * @return
   */
  @RequestMapping(value = "/queryAll")
  public Wrapper<Map<String,Object>> queryAll(Integer nowPage,Integer pageSize,Integer type){
    Map<String,Object> res=userService.queryAllByPage(nowPage,pageSize,type);
    return WrapMapper.wrapRes(res);
  }

  /**
   * 按照账户查询
   * @param account
   * @return
   */
  @RequestMapping(value = "/queryByAccount")
  public Wrapper<List<UserVo>> queryByAccount(String account){
    List<UserVo> res=userService.queryByAccount(account);
    return WrapMapper.wrapRes(res);
  }

  /**
   * 按照用户姓名查询，已经考虑重名的多个结果分页
   * @param nowPage
   * @param pageSize
   * @param userName
   * @param type
   * @return
   */
  @RequestMapping(value = "/queryByName")
  public Wrapper<Map<String,Object>> queryByName(Integer nowPage,Integer pageSize,String userName,Integer type){
    Map<String,Object> res=userService.queryByName(nowPage,pageSize,userName,type);
    return WrapMapper.wrap(res);
  }

  /**
   * 按照账户和姓名查询
   * @param account
   * @return
   */
  @RequestMapping(value = "/queryByNA")
  public Wrapper<List<UserVo>> queryByNameAccount(String account,String userName,Integer type){
      List<UserVo> res=userService.queryByNA(userName,account,type);
      return WrapMapper.wrap(res);
  }

  /**
   * 创建新用户,账户名可防止重复 用户名，账户名。性别，手机号，邮箱号，用户类别
   * @param user
   * @return
   */
  @RequestMapping(value = "/create")
  public Wrapper<Boolean> create(User user){
    Boolean res=userService.createUser(user);
    return WrapMapper.wrapRes(res);
  }

  /**
   * 更新用户信息，可更改选项：用户名，性别，手机号，邮箱号，生日,用户类别，传的信息必须有account
   * @param user
   * @return
   */
  @RequestMapping(value = "/update")
  public Wrapper<Boolean> update(User user){
    Boolean res=userService.updateUser(user);
    return WrapMapper.wrap(res);
  }

  /**
   * 删除用户,传入用户账户名的set
   * @param accounts
   * @return
   */

  @RequestMapping(value = "/delete")
  public Wrapper<Boolean> delete(String[] accounts){
    Boolean res=userService.deleteUser(Arrays.asList(accounts));
    return WrapMapper.wrapRes(res);
  }

  /**
   * 导出所有的用户信息excel
   * @return
   */
  @RequestMapping(value="/download")
  public ResponseEntity<FileSystemResource> download(){
    ResponseEntity<FileSystemResource> res=userService.download();
    return res;
  }

}
