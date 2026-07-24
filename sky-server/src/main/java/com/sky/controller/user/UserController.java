package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody UserLoginDTO dto) {
        User user = userService.wxLogin(dto);
        String token = jwtUtil.createJWT(user.getId(), jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl());
        EmployeeLoginVO vo = EmployeeLoginVO.builder()
                .id(user.getId()).name(user.getName()).userName(user.getPhone()).token(token).build();
        return Result.success(vo);
    }
}
