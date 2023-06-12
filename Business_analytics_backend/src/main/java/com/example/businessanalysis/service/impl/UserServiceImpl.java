package com.example.businessanalysis.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.stream.CollectorUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.businessanalysis.common.WrapMapper;
import com.example.businessanalysis.common.Wrapper;
import com.example.businessanalysis.common.exception.BusinessException;
import com.example.businessanalysis.domain.User;
import com.example.businessanalysis.mapper.UserMapper;
import com.example.businessanalysis.pojo.normalUserPojo.LoginUser;
import com.example.businessanalysis.pojo.normalUserPojo.UserLoginPojo;
import com.example.businessanalysis.service.UserService;
import com.example.businessanalysis.utils.MyDateUtils;
import com.example.businessanalysis.utils.MyExcelUtils;
import com.example.businessanalysis.utils.MyJwtUtil;
import com.example.businessanalysis.utils.UpGitUtils;
import com.example.businessanalysis.utils.VerCodeGenerateUtils;
import com.example.businessanalysis.vo.GoodVo;
import com.example.businessanalysis.vo.UserVo;
import io.lettuce.core.dynamic.annotation.CommandNaming;
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
import org.apache.catalina.security.SecurityUtil;
import org.apache.poi.util.StringUtil;
import org.apache.xpath.operations.Bool;
import org.bouncycastle.jcajce.provider.symmetric.AES.Wrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 74707
 * @description 针对表【t_user(用户表)】的数据库操作Service实现
 * @createDate 2022-10-26 12:01:20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

  @Resource
  public UserMapper userMapper;

  @Resource
  BCryptPasswordEncoder passwordEncoder;

  @Resource
  AuthenticationManager authenticationManager;

  @Resource
  RedisTemplate<Object, Object> redisTemplate;

  @Autowired
  JavaMailSenderImpl mailSender;

  @Value("${spring.mail.username}")
  private String from;

  /**
   * 共有的工具方法
   */
  @Override
  public boolean isOnline(String account) {
    // 查询account用户是否在线
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("account", account);
    User user = userMapper.selectOne(queryWrapper);
    if (user == null) {
      throw new RuntimeException(account + "用户不存在");
    }
    return user.getIsOnline() == 1;
  }

  @Override
  public void setLogin(String account) {
    //设置登录相关的状态
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("account", account);
    queryWrapper.eq("del_flag",0);
    User user = userMapper.selectOne(queryWrapper);
    UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
    userUpdateWrapper.eq("account", account)
        .set("is_online", 1)
        .set("last_login_time", DateTime.now())
        .set("update_time", DateTime.now());
    userMapper.update(user, userUpdateWrapper);
  }

  @Override
  public void setLogout(String account) {
    User user = getUser(account);
    UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
    userUpdateWrapper.eq("account", account)
        .set("is_online", 0);
    userMapper.update(user, userUpdateWrapper);
  }

  @Override
  public User getUser(String account) {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("account", account);
    return userMapper.selectOne(queryWrapper);
  }

  /**
   * 修改密码
   * @param userLoginPojo
   */
  @Override
  public Boolean alterPassword(UserLoginPojo userLoginPojo) {
    String account = userLoginPojo.getAccount();
    String oldPassword = userLoginPojo.getUserPassword();
    String newPassword = userLoginPojo.getNewPassword();
//    System.out.println(passwordEncoder.encode(oldPassword))
    User fitUser = userMapper.selectOne(Wrappers.<User>lambdaQuery().
        eq(User::getAccount, account).eq(User::getDelFlag,0));
    if (Objects.isNull(fitUser)) {
      throw new BusinessException("账户不存在");
    } else if (!passwordEncoder.matches(oldPassword, fitUser.getUserPassword())) {
      throw new BusinessException("输入密码错误");
    }
    userMapper.update(fitUser,
        Wrappers.<User>lambdaUpdate().eq(User::getAccount, fitUser.getAccount())
            .set(User::getUserPassword, passwordEncoder.encode(newPassword)));
    return true;
  }

  @Override
  public Boolean forgetPassword(UserLoginPojo userLoginPojo) {
    User fitUser = userMapper.selectOne(
        Wrappers.<User>lambdaQuery().eq(User::getAccount, userLoginPojo.getAccount())
            .eq(User::getDelFlag, 0));

    if (Objects.isNull(fitUser)) throw new BusinessException("账户不存在");
    String tos = userMapper.selectOne(Wrappers.<User>lambdaQuery().
        eq(User::getAccount,userLoginPojo.getAccount()).eq(User::getDelFlag,0)).getEmail();
    if(StringUtils.isBlank(tos)) throw new BusinessException("未绑定邮箱，无法重置密码");

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(from);
    message.setTo(tos);
    message.setSubject("您本次的验证码是");
    String verCode = VerCodeGenerateUtils.generateVerCode();
    message.setText("亲爱的用户,您好:\n"
        + "\n本次请求的邮件验证码为:" + verCode + ",请及时输入。有效时间5分钟（请勿泄露此验证码）\n"
        + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复）");
    mailSender.send(message);
    userLoginPojo.setVerCode(verCode);
    userLoginPojo.setSendTime(new Date());
    ValueOperations<Object, Object> objectObjectValueOperations = redisTemplate.opsForValue();
    objectObjectValueOperations.set(userLoginPojo.getAccount(),userLoginPojo);
    return true;
  }

  @Override
  public Boolean resetPassword(UserLoginPojo user) {
    UserLoginPojo nowInRedis=(UserLoginPojo) redisTemplate.opsForValue().get(user.getAccount());
    System.out.println(nowInRedis);

    if(Objects.isNull(nowInRedis)) throw new BusinessException("请先获取验证码");
    if(!nowInRedis.getVerCode().equals(user.getVerCode())){
      throw new BusinessException("验证码错误");
    }else if(MyDateUtils.calculatetimeGapMinute(nowInRedis.getSendTime(),new Date())>=5){
      throw new BusinessException("验证码失效");
    }
    User updateUser = new User();
    updateUser.setUserPassword(passwordEncoder.encode(user.getNewPassword()));
    Integer res=userMapper.update(updateUser,Wrappers.<User>lambdaUpdate().
        eq(User::getAccount,user.getAccount()).eq(User::getDelFlag,0));
    return res!=0;
  }

  /**
   * 注册
   *
   * @param userLoginPojo
   */
  @Override
  public Wrapper<String> register(UserLoginPojo userLoginPojo) {
    HashMap<String, Object> map = new HashMap<>();
    map.put("account", userLoginPojo.getAccount());
    List<User> users = userMapper.selectByMap(map);
    if (users.isEmpty()) {
      User user = new User();
      user.setAccount(userLoginPojo.getAccount());
      user.setUserPassword(passwordEncoder.encode(userLoginPojo.getUserPassword()));
      user.setIsOnline(0);
      user.setLastLoginTime(DateTime.now());
      user.setRoleId(2); // 默认权限为普通用户
      userMapper.insert(user);
      LoginUser loginUser = new LoginUser(user, user.getRoleId());
      String token = MyJwtUtil.createToken(userLoginPojo.getAccount());
      //写入redis数据库
      ValueOperations<Object, Object> objectObjectValueOperations = redisTemplate.opsForValue();
      objectObjectValueOperations.set(userLoginPojo.getAccount(), loginUser);
      return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "注册成功", token);
    }
    return WrapMapper.wrap(Wrapper.ERROR_CODE, "注册失败", "用户已存在");
  }

  /**
   * 登录
   *
   * @param userLoginPojo
   */
  @Override
  public Wrapper<String> login(UserLoginPojo userLoginPojo) {
    // 认证用户;
    System.out.println(userLoginPojo.getUserPassword());
    User fitUser = userMapper.selectOne(
        Wrappers.<User>lambdaQuery().eq(User::getAccount, userLoginPojo.getAccount())
            .eq(User::getDelFlag, 0));
    if(Objects.isNull(fitUser)) throw new BusinessException("账户不存在");
    Authentication authenticate = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(userLoginPojo.getAccount(),
            userLoginPojo.getUserPassword()));
    System.out.println("--------auth-----------" + authenticate);
    if (authenticate == null) {
      throw new BusinessException("用户密码错误");
    }
    //认证通过,将user 的 account 和 password 封装成jwt 返回
    LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
    String account = userLoginPojo.getAccount();

    // TODO 查看用户是否已经登录,不可以多次登录以获取多个token
//    if (this.isOnline(account)) {
//      return WrapMapper.wrap(Wrapper.ERROR_CODE,"用户已登录","用户已登录,请切换账号或重试");
//    }
    // 生成JWT
    //如果点了记住我
    String token = null;
    if (userLoginPojo.getRememberMe() == 1) {
      token = MyJwtUtil.createTokenLongtime(account);
    } else {
      token = MyJwtUtil.createToken(account);
    }
    // 利用userName 写入redis缓存
    ValueOperations<Object, Object> objectObjectValueOperations = redisTemplate.opsForValue();
    objectObjectValueOperations.set(account, loginUser);
    // 将用户登录信息设为已登录
    this.setLogin(account);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "登录成功", token);
  }

  @Override
  public Boolean updateImage(MultipartFile multipartFile, String account) throws IOException {
    String targetUrl = UpGitUtils.createUploadFileUrl(multipartFile.getOriginalFilename(),
        UpGitUtils.USERPATH);
    Map<String, Object> postBody = UpGitUtils.getUploadBodyMap(multipartFile.getBytes());
    String JSONResult = HttpUtil.post(targetUrl, postBody);
    JSONObject jsonObj = JSONUtil.parseObj(JSONResult);
    if (jsonObj == null || jsonObj.getObj("commit") == null) {
      throw new BusinessException("上传图片服务失效，请稍后再试");
    }
    JSONObject content = JSONUtil.parseObj(jsonObj.getObj("content"));
    String downloadUrl = content.getObj("download_url").toString();
    System.out.println(downloadUrl);
    User user = new User();
    user.setImage(downloadUrl);
    Integer res = userMapper.update(user,
        Wrappers.<User>lambdaUpdate().eq(User::getAccount, account));
    return res != 0;
  }

  @Override
  public List<UserVo> queryByAccount(String account) {
    User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, account).
        eq(User::getDelFlag, 0));
    if (Objects.isNull(user)) {
      throw new BusinessException("账户名为" + account + "的用户不存在");
    }
    List<UserVo> res = new ArrayList<>();
    res.add(new UserVo(user));
    return res;
  }

  @Override
  public Map<String, Object> queryByName(Integer nowPage, Integer pageSize, String userName,
      Integer type) {
    Map<String, Object> re = new HashMap<>();
    List<UserVo> res = new ArrayList<>();
    IPage<User> raw;
    if (type == 3) {
      raw = userMapper.selectPage(new Page<>(nowPage, pageSize), Wrappers.<User>lambdaQuery().
          eq(User::getUserName, userName).eq(User::getDelFlag, 0));
    } else {
      raw = userMapper.selectPage(new Page<>(nowPage, pageSize), Wrappers.<User>lambdaQuery().
          eq(User::getUserName, userName).eq(User::getDelFlag, 0).eq(User::getRoleId, type));
    }
    if (Objects.isNull(raw) || CollectionUtil.isEmpty(raw.getRecords())) {
      throw new BusinessException("姓名为" + userName + "的对应级别用户不存在");
    }
    res = raw.getRecords().stream().map(v -> new UserVo(v)).collect(Collectors.toList());
    re.put("total", raw.getTotal());
    re.put("pages", raw.getPages());
    re.put("pageSize", raw.getSize());
    re.put("current", raw.getCurrent());
    re.put("records", res);
    return re;
  }

  @Override
  public List<UserVo> queryByNA(String userName, String account, Integer type) {
    List<User> re = new ArrayList<>();
    if (type == 3) {
      re = userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getAccount, account).
          eq(User::getUserName, userName).eq(User::getDelFlag, 0));
    } else {
      re = userMapper.selectList(Wrappers.<User>lambdaQuery().
          eq(User::getUserName, userName).eq(User::getDelFlag, 0).eq(User::getRoleId, type));
    }
    if (CollectionUtil.isEmpty(re)) {
      throw new BusinessException(""
          + "不存在账户名为" + account + "且用户名为" + userName + "的对应级别用户");
    }
    List<UserVo> res = new ArrayList<>();
    res.add(new UserVo(re.get(0)));
    return res;
  }

  @Override
  public List<UserVo> queryAllNoPage() {
    List<UserVo> res = new ArrayList<>();
    List<User> raw = userMapper.selectList(Wrappers.<User>lambdaQuery().
        eq(User::getDelFlag, 0));
    for (User user : raw) {
      res.add(new UserVo(user));
    }
    return res;
  }

  @Override
  public Map<String, Object> queryAllByPage(Integer nowPage, Integer pageSize, Integer type) {
    //type=2只看普通用户 type=1只看高级用户 type=3 全部用户
    Map<String, Object> re = new HashMap<>();
    List<UserVo> res = new ArrayList<>();

    IPage<User> raw;
    if (type == 3) {
      raw = userMapper.selectPage(new Page<>(nowPage, pageSize), Wrappers.<User>lambdaQuery().
          eq(User::getDelFlag, 0));
    } else {
      raw = userMapper.selectPage(new Page<>(nowPage, pageSize), Wrappers.<User>lambdaQuery().
          eq(User::getDelFlag, 0).eq(User::getRoleId, type));
    }

    if (Objects.isNull(raw) || CollectionUtil.isEmpty(raw.getRecords())) {
      throw new BusinessException("目前暂无该需求的用户信息");
    }
    res = raw.getRecords().stream().map(v -> new UserVo(v)).collect(Collectors.toList());
    re.put("total", raw.getTotal());
    re.put("pages", raw.getPages());
    re.put("pageSize", raw.getSize());
    re.put("current", raw.getCurrent());
    re.put("records", res);
    return re;
  }

  @Override
  public Boolean createUser(User user) {
    /**不允许账户重复**/
    User user1 = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getAccount,
        user.getAccount()).eq(User::getDelFlag, 0));
    if (Objects.nonNull(user1)) {
      throw new BusinessException("账户重复啦");
    }
    /**不允许手机号重复**/
    if (StringUtils.isNotBlank(user.getMobile())) {
      List<User> userList = userMapper.selectList(Wrappers.<User>lambdaQuery().
          eq(User::getMobile, user.getMobile()).eq(User::getDelFlag, 0));
      if (CollectionUtil.isNotEmpty(userList)) {
        throw new BusinessException("手机号重复啦");
      }
    }
    /**其他信息**/
    if (user.getBirthdayStr() != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      try {
        System.out.println(user.getBirthdayStr());
        System.out.println(sdf.parse(user.getBirthdayStr()));
        user.setBirthday(sdf.parse(user.getBirthdayStr()));
      } catch (Exception e) {
        throw new BusinessException("日期格式错误");
      }
    }
    /**设置用户信息**/
    user.setUserPassword(passwordEncoder.encode("111111"));
    //基本信息已经在user中，下面是不能为null，需要转化的部分
    if (user.getIsOnline() == null) {
      user.setIsOnline(0);
    }
    if (user.getRoleId() == null) {
      user.setRoleId(2);
    }
    user.setDelFlag(0);
    Integer res = userMapper.insert(user);
    return res != 0;
  }

  @Override
  public Boolean updateUser(User user) {
    /**账户存在性验证**/
    List<User> re = userMapper.selectList(Wrappers.<User>lambdaQuery()
        .eq(User::getAccount, user.getAccount()).eq(User::getDelFlag, 0));
    if (CollectionUtil.isEmpty(re)) {
      throw new BusinessException("不存在该账户");
    }
    /**不允许手机号重复**/
    if (StringUtils.isNotBlank(user.getMobile())) {
      List<User> userList = userMapper.selectList(Wrappers.<User>lambdaQuery().
          eq(User::getMobile, user.getMobile()).eq(User::getDelFlag, 0));
      if (CollectionUtil.isNotEmpty(userList) && !userList.get(0).getAccount().
          equals(user.getAccount())) {
        throw new BusinessException("手机号重复啦");
      }
    }
    /**其他信息**/
    if (user.getBirthdayStr() != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      try {
        System.out.println(user.getBirthdayStr());
        System.out.println(sdf.parse(user.getBirthdayStr()));
        user.setBirthday(sdf.parse(user.getBirthdayStr()));
      } catch (Exception e) {
        throw new BusinessException("日期格式错误");
      }
    }
    user.setUpdateTime(new java.util.Date());
    Integer res = userMapper.update(user, Wrappers.<User>lambdaUpdate().
        eq(User::getAccount, user.getAccount()).eq(User::getDelFlag, 0));
    return res != 0;
  }

  @Override
  public Boolean deleteUser(List<String> accounts) {
    if (CollectionUtil.isEmpty(accounts)) {
      throw new BusinessException("提交了为空的删除项");
    }
    User delUser = new User();
    delUser.setDelFlag(1);
    userMapper.update(delUser,
        Wrappers.<User>lambdaQuery().in(User::getAccount, accounts).eq(User::getDelFlag, 0));
    return true;
  }

  @Override
  public ResponseEntity<FileSystemResource> download() {
    List<UserVo> queryAll = queryAllNoPage();
    if (CollectionUtil.isEmpty(queryAll)) {
      throw new BusinessException("系统中没有可打印的内容");
    }
    List<Map<String, Object>> list = new ArrayList<>();
    for (UserVo userVo : queryAll) {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("用户姓名", userVo.getUserName());
      map.put("账户名", userVo.getAccount());
      map.put("性别", userVo.getGender());
      map.put("手机", userVo.getMobile());
      map.put("邮件", userVo.getEmail());
      map.put("上次登录时间", userVo.getLastLoginTime());
      map.put("用户类别", userVo.getRoleName());
      list.add(map);
    }
    ResponseEntity<FileSystemResource> res = MyExcelUtils.downloadExcel(list);
    return res;
  }
}




