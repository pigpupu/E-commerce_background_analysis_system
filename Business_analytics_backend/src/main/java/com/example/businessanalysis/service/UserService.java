package com.example.businessanalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.businessanalysis.common.Wrapper;
import com.example.businessanalysis.domain.User;
import com.example.businessanalysis.pojo.normalUserPojo.UserLoginPojo;
import com.example.businessanalysis.vo.UserVo;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author 74707
 * @description 针对表【t_user(用户表)】的数据库操作Service
 * @createDate 2022-10-26 12:01:20
 */
public interface UserService extends IService<User> {

  /**
   * 判断用户是否处于登录状态
   */
  boolean isOnline(String account);

  /**
   * 将用户设置为登录状态
   * @param account
   */
  void setLogin(String account);

  /**
   * 将用户设置为登出状态
   * @param account
   */
  void setLogout(String account);;

  /**
   * 利用account获取用户
   * @param account
   * @return
   */
  User getUser(String account);


  /**
   * 修改密码
   *
   * @param user
   */
  Boolean alterPassword(UserLoginPojo user);

  Boolean forgetPassword(UserLoginPojo user);

  Boolean resetPassword(UserLoginPojo user);

  /**
   * 注册
   *
   * @param user
   */
  Wrapper<String> register(UserLoginPojo user);

  /**
   * 登录
   *
   * @param user
   */
  Wrapper<String> login(UserLoginPojo user);


  /**
   * 修改头像
   *
   * @param multipartFile 文件
   * @param account       账户
   */
  Boolean updateImage(MultipartFile multipartFile, String account) throws IOException;


  /**
   * 根据账户名查询
   * @return /
   */
  List<UserVo> queryByAccount(String account);
  /**
   * 根据用户名查询
   * @param userName /
   * @return /
   */
  Map<String, Object> queryByName(Integer nowPage,Integer pageSize,String userName,Integer type);
  /**
   * 根据账户名+用户名查询
   * @param userName /
   * @return /
   */
  List<UserVo> queryByNA(String userName,String account,Integer type);

  /**
   * 查询所有用户信息不分页
   * @return /
   */
  List<UserVo> queryAllNoPage();

  /**
   * 查询所有用户信息分页
   * @return /
   */
  Map<String, Object> queryAllByPage(Integer nowPage,Integer pageSize,Integer type);

  /**
   * 新增用户
   */
  Boolean createUser(User user);

  /**
   * 编辑用户
   * @throws Exception /
   */
  Boolean updateUser(User user);

  /**
   * 删除用户
   * @param accounts /
   */
  Boolean deleteUser(List<String> accounts);


  /**
   * 导出所用用户信息
   */
  public ResponseEntity<FileSystemResource> download();

}
