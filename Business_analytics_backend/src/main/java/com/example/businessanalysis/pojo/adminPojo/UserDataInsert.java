package com.example.businessanalysis.pojo.adminPojo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName : UserDataInsert
 * @Description : 用来导入用户数据的一个专用类
 * @Author : 桓
 * @Date: 2022/11/9  10:44
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDataInsert {

  private String id;
  private String userName;
  private String account;
  private String userPassword;
  private String gender;
  private String birthday;
  private String mobile;
  private String email;
  // 如果导入图片的话,需要将图片存在一个网站上,再访问网站的网址获取图片
  private String image;
  private String lastLoginTime;
  private String isOnline;
  private String addTime;
  private String updateTime;
  private String delFlag;
  private String roleId;

  public UserDataInsert(List<String> list) {
    this(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6),
        list.get(7),
        list.get(8), list.get(9), list.get(10), list.get(11), list.get(12), list.get(13),
        list.get(14)
    );
  }
}
