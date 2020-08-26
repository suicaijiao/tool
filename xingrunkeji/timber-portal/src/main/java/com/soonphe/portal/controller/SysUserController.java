package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.SysUser;
import com.soonphe.portal.service.ISysUserService;
import com.soonphe.portal.util.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.soonphe.portal.commons.golbal.result.ResultCode.*;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-06-05
 */
@RestController
@RequestMapping("/portal")
public class SysUserController {

    private static final Logger logger = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private ISysUserService service;


    /**
     * 登录校验
     *
     * @param username
     * @param password
     * @param verifyCode
     */
    @RequestMapping("/checkLogin")
    public ResponseResult<SysUser> checkLogin(@RequestParam(value = "username") String username,
                                                @RequestParam(value = "password") String password,
                                                @RequestParam(value = "verifyCode", required = false) String verifyCode) {
        SysUser sysUser = service.getOne(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getUserName, username));
        if (sysUser == null) {
            return ResponseResult.failed(USER_IS_NULL);
        } else if (!StringUtil.getMD5(password).equals(sysUser.getPassword())) {
            return ResponseResult.failed(PASSWORD_FAIL);
        } else {
            if (!sysUser.getStatus()) {
                return ResponseResult.failed(AUDITFAIL);
            } else {
//                UserSession userSession = new UserSession();
//                userSession.setUserId(sysUser.getId());
//                userSession.setUserName(sysUser.getUsername());
////                userSession.setUser_ip(StrUtil.getIp(request));
//                userSession.setRoleId(sysUser.getRoleid());
//                userSession.setRoleName(sysUser.getRolename());
//                userSession.setName(sysUser.getName());
//                request.getSession().setAttribute("userSession", userSession);
//                request.getSession().setMaxInactiveInterval(1000 * 60 * 30);// 设置过期时间30分钟
                //每次登录生成角色菜单
//                sysMenuService.createMenuStr(userSession.getRoleId());
                return ResponseResult.success(sysUser);
            }
        }
    }

    /**
     * 退出,session失效
     *
     * @param request
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "forward:/portal/login";
    }

}
