package com.yinlian.cfra.controller.manage;

import com.yinlian.cfra.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @description
 * @author: suicaijiao
 * @create: 2020-05-26 14:26
 **/
@RestController
@RequestMapping("manage/login")
public class UserLoginController {

    @Autowired
    private UsersService usersService;

    /**
     * 加载登录页面
     *
     * @param model
     * @return
     */
    @GetMapping
    public ModelAndView index(Model model) {
        return new ModelAndView("manage/login");
    }


}
