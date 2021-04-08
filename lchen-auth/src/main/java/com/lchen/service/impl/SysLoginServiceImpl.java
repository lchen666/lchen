package com.lchen.service.impl;

import com.lchen.common.core.constant.Constants;
import com.lchen.common.core.dto.R;
import com.lchen.common.core.exception.BaseException;
import com.lchen.common.core.utils.CookieUtil;
import com.lchen.common.core.utils.PasswordUtil;
import com.lchen.common.core.utils.RSAUtil;
import com.lchen.common.core.utils.StringUtil;
import com.lchen.entity.dto.LoginFormDto;
import com.lchen.entity.vo.SysUserVO;
import com.lchen.service.SysLoginService;
import com.lchen.service.ValidateCodeService;
import com.lchen.user.entity.SysUser;
import com.lchen.user.feign.SysUserFeign;
import com.lchen.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
public class SysLoginServiceImpl implements SysLoginService {

    @Autowired
    private SysUserFeign sysUserFeign;
    @Autowired
    private ValidateCodeService codeService;
    @Autowired
    private TokenUtil tokenUtil;

    @Value("${rsa.private.key}")
    private String privateKey;

    @Override
    public SysUserVO login(LoginFormDto formDto) {
        if (StringUtil.isAnyBlank(formDto.getUserName(), formDto.getPassword())){
            throw new BaseException("用户名/密码不能为空！", 101);
        }
        //验证码校验
        codeService.checkCaptcha(formDto.getUuId(), formDto.getVerCode());
        //获取登录信息
        R<SysUser> resultData = sysUserFeign.getSysUserByUserName(formDto.getUserName());
        if (resultData.getCode() == R.FAIL) {
            throw new BaseException(resultData.getMsg(), resultData.getCode());
        }
        SysUser sysUser = resultData.getData();
        if (sysUser == null){
            throw new BaseException("用户不存在", 103);
        }
        String decryptPassword = RSAUtil.decrypt(formDto.getPassword(), privateKey);
        // 密码如果不在指定范围内 错误
        if (decryptPassword.length() < Constants.PASSWORD_MIN_LENGTH
                || decryptPassword.length() > Constants.PASSWORD_MAX_LENGTH){
            throw new BaseException("用户密码不在指定范围", 102);
        }
        String cryptPassword = PasswordUtil.cryptPassword(sysUser.getUserId().toString(), decryptPassword);
        if (!cryptPassword.equals(sysUser.getPassword())){
            throw new BaseException("密码错误！", 104);
        }
        //登陆成功 生成token
        tokenUtil.createToken(sysUser);
        SysUserVO sysUserVO = new SysUserVO();
        sysUserVO.setUserId(sysUser.getUserId());
        sysUserVO.setNickName(sysUser.getNickName());
        sysUserVO.setAvatar(sysUser.getAvatar());
        sysUserVO.setUserName(sysUser.getUserName());
        return sysUserVO;
    }

    @Override
    public void loginOut(HttpServletRequest request, HttpServletResponse response) {
        try {
            CookieUtil.delCookie(request, response, Constants.WEB_AUTH);
        }catch (Exception e){
            throw new BaseException("刪除cookie失败！");
        }

    }
}
