//package com.soonphe.portal.controller;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.core.toolkit.Assert;
//import com.baomidou.mybatisplus.extension.api.R;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.soonphe.framework.entity.constant.ResultCodeEnum;
//import com.soonphe.portal.ErrorCode;
//import com.soonphe.portal.entity.TmAdvert;
//import com.soonphe.portal.service.ITmAdvertService;
//import com.soonphe.portal.util.redis.RedisUtil;
//import com.soonphe.portal.util.redis.StringRedisUtil;
//import com.soonphe.portal.vo.TmAdvertVo;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * <p>
// * 前端控制器
// * </p>
// *
// * @author soonphe
// * @since 2019-06-10
// */
//@RestController
//@RequestMapping("/tm-advert")
//@Api(tags = "【广告管理】广告")
//public class TmAdvertController {
//
//    //使用默认打印
//    private static final Logger logger = LoggerFactory.getLogger(TmAdvertController.class);
//    //使用自定义logger
//    private static Logger sysLog = LoggerFactory.getLogger("sysLog");
//
//    @Autowired
//    private ITmAdvertService service;
//
//    /**
//     * api默认格式返回
//     * {"code":1000,"data":null,"msg":"测试错误编码"}
//     *
//     * @param test
//     * @return
//     */
//    @ApiOperation("测试API")
//    @GetMapping("/api")
//    public R<String> testError(String test) {
//
//        //测试默认日志
//        logger.debug("默认logger：debug");
//        logger.info("默认logger：info");
//        logger.warn("默认logger：warn");
//        logger.error("默认logger：error");
//
//        //测试自定义日志
//        sysLog.warn("自定义sysLog：warn");
//        sysLog.error("自定义sysLog：error");
//
//        Assert.notNull(ResultCodeEnum.FAILED, test);
//        return R.failed(ErrorCode.TEST);
////        return R.failed(test);
//    }
//
//    @ApiOperation("测试Redis")
//    @GetMapping("/testRedis")
//    public R<String> testRedis(String key, String value) {
//        StringRedisUtil.set(key, value);
//        TmAdvert tmAdvert = new TmAdvert();
//        tmAdvert.setName(value);
//        tmAdvert.setContent(value);
//        tmAdvert.setPicUrl(value);
//        RedisUtil.set(key + 1, tmAdvert);
//        return R.ok("ok");
//    }
//
//    @ApiOperation("测试Redis取值")
//    @GetMapping("/testRedisGetKey")
//    public R<TmAdvert> testRedisGetKey(String key) {
//        StringRedisUtil.get(key);
//        TmAdvert tmAdvert = RedisUtil.get(key + 1);
//        return R.ok(tmAdvert);
//    }
//
//    @ApiOperation("测试Redis取值")
//    @GetMapping("/testRedisGetKeyString")
//    public R<String> testRedisGetKeyString(String key) {
//        return R.ok(StringRedisUtil.get(key));
//    }
//
//    /**
//     * 测试全局异常拦截与AOP
//     *
//     * @param test
//     * @return
//     */
//    @ApiOperation("测试异常拦截")
//    @GetMapping("/exception")
//    public R<String> testException(String test) throws Exception {
//        throw new Exception();
//    }
//
//
//    /**
//     * 分页查询
//     *
//     * @param baseVo 查询实体
//     * @return
//     */
//    @ApiOperation("分页获取List")
//    @GetMapping("getList")
//    public R getList(Page<TmAdvert> page, TmAdvertVo baseVo) {
//
//        IPage<TmAdvert> page1 = service.selectByAdvert(page, baseVo);
//        return R.ok(page1);
//    }
//
//    /**
//     * 分页条件查询
//     *
//     * @return
//     */
//    @ApiOperation("分页获取List")
//    @GetMapping("getPage")
//    public R getPage() {
////        IPage<TmAdvert> page = service.page(new Page<>(0, 12), null);
//        IPage<TmAdvert> page = service.page(new Page<>(0, 12),
////                null
////                new QueryWrapper<TmAdvert>().like("name", "毛")  //普通查询
//                new QueryWrapper<TmAdvert>().lambda().like(TmAdvert::getName, "毛")  //同上
////                new LambdaQueryWrapper<TmAdvert>().like(TmAdvert::getName, "毛")   //同上
////                Wrappers.<TmAdvert>lambdaQuery().like(TmAdvert::getName, "毛") //同上
////                new QueryWrapper<TmAdvert>().inSql("name", "select id from role where id = 2")  //携带子查询
////                new QueryWrapper<TmAdvert>().nested(i -> i.eq("role_id", 2L).or().eq("role_id", 3L))   //嵌套查询
//
//        );
//        return R.ok(page);
//    }
//
//    /**
//     * @return
//     */
//    @ApiOperation("逻辑删除")
//    @GetMapping("/logicDel")
//    public R logicDel() {
//        TmAdvert user = new TmAdvert("阿三");
//        boolean result = service.save(user);
//
//        System.err.println("逻辑删除一条数据：" + service.removeById(user.getId()));
//        return R.ok("删除成功");
//    }
//
//    /**
//     * AR部分测试
//     *
//     * @return
//     */
//    @ApiOperation("AR部分测试")
//    @GetMapping("testAR")
//    public IPage<TmAdvert> testAR() {
//
//        TmAdvert user = new TmAdvert("testAr");
//        System.err.println("删除所有：" + user.delete(null));
//        user.insert();
//        System.err.println("查询插入结果：" + user.selectById().toString());
//        user.setName("mybatis-plus-ar");
//        System.err.println("更新：" + user.updateById());
//        return user.selectPage(new Page<TmAdvert>(0, 12), null);
//    }
//
//    /**
//     * 增删改查 CRUD
//     * http://localhost:8080/user/test2
//     */
//    @ApiOperation("增删改查 CRUD")
//    @GetMapping("/testCrud")
//    public TmAdvert testCrud() {
//        System.err.println("删除一条数据：" + service.removeById(1L));
//        System.err.println("插入一条数据：" + service.save(new TmAdvert(1L, "张三")));
//        TmAdvert user = new TmAdvert("张三");
//        boolean result = service.save(user);
//        // 自动回写的ID
//        Long id = user.getId();
//        System.err.println("插入一条数据：" + result + ", 插入信息：" + user.toString());
//
//        System.err.println("查询：" + service.getById(id).toString());
//        System.err.println("更新一条数据：" + service.updateById(new TmAdvert(1L, "三毛")));
//        for (int i = 0; i < 5; ++i) {
//            service.save(new TmAdvert(Long.valueOf(100 + i), "张三" + i));
//        }
//        IPage<TmAdvert> userListPage = service.page(new Page<TmAdvert>(1, 5), new QueryWrapper<TmAdvert>());
//        System.err.println("total=" + userListPage.getTotal() + ", current list size=" + userListPage.getRecords().size());
//
//        userListPage = service.page(new Page<TmAdvert>(1, 5), new QueryWrapper<TmAdvert>().orderByDesc("name"));
//        System.err.println("total=" + userListPage.getTotal() + ", current list size=" + userListPage.getRecords().size());
//        return service.getById(1L);
//    }
//
//    /**
//     * 条件构造器 + 自定义SQL混合使用——拼接使用
//     * 使用条件：（①mapper注解SQL中添加： ${ew.customSqlSegment}  ②xml中SQL添加：${ew.customSqlSegment}）
//     * 注意：不支持以及不赞成在 RPC 调用中把 Wrapper 进行传输
//     * http://localhost:8080/user/select_wrapper
//     */
//    @GetMapping("/select_wrapper")
//    public Object getUserByWrapper() {
//        return service.selectListByWrapper(new QueryWrapper<TmAdvert>()
//                .lambda().like(TmAdvert::getName, "毛")
//                .or(e -> e.like(TmAdvert::getName, "张")));
//    }
//
//    /**
//     * 测试事务
//     * 启动  Application 加上 @EnableTransactionManagement 注解其实可无默认貌似就开启了<br>
//     * 需要事物的方法加上 @Transactional 必须的哦！！
//     */
//    @ApiOperation("测试事务")
//    @Transactional(rollbackFor = Exception.class)
//    @GetMapping("/test_transactional")
//    public void testTransactional() {
//        TmAdvert user = new TmAdvert(1000L, "测试事务");
//        service.save(user);
//        System.out.println(" 这里手动抛出异常，自动回滚数据");
//        throw new RuntimeException();
//    }
//}
