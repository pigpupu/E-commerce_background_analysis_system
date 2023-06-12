package com.example.businessanalysis.vo;

import com.example.businessanalysis.domain.User;
import java.text.SimpleDateFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UserVo
 * @Description 用户查询返回视图
 * @Author 74707
 * @Date 2022/11/19 10:43
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
  private String userName;
  private String account;
  private String gender;
  private Integer genderNum;
  private String mobile;
  private String email;
  private String image;
  private String lastLoginTime;
  private String birthday;
  private String isOnline;
  private String roleName;

  public UserVo(User user){
    if(user==null) return;
    this.account=user.getAccount();
    this.userName= user.getUserName();
    this.image=user.getImage();

    if(user.getBirthday()!=null){
      SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
      this.birthday=sdf.format(user.getBirthday());
    }

    if(user.getGender()==null) {
      this.gender = "未知";
      this.genderNum=3;
    }
    else{
      this.gender=user.getGender()==0?"男":"女";
      this.genderNum=user.getGender();
    }

    this.genderNum=user.getGender();

    this.mobile=user.getMobile();
    this.email=user.getEmail();

    if(user.getLastLoginTime()==null) this.lastLoginTime=null;
    else{
      SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      this.lastLoginTime=sdf.format(user.getLastLoginTime());
    }

    if(user.getIsOnline()==null) this.isOnline="未知";
    else{
      this.isOnline=user.getIsOnline()==0?"离线":"在线";
    }

    if(user.getRoleId()==null||user.getRoleId()==2){
      this.roleName="普通用户";
    }else if(user.getRoleId()==1){
      this.roleName="高级用户";
    }else if(user.getRoleId()==0){
      this.roleName="管理员";
    }

  }
}
