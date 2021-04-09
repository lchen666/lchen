package com.lchen.service;

import com.lchen.entity.vo.UserVO;

import java.util.Map;

public interface UserLoginService {

    UserVO login(Map<String, Object> map);

}
