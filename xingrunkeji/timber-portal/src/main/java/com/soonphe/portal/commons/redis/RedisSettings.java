package com.soonphe.portal.commons.redis;


/**
 * 模块名+功能名+参数
 * 例如：（字典数据）
 * Key：sys:base:dic:item:sex(sex)是参数值
 * Value：[{"code":"1","name":"男"},{"code":"0","name":"女"}]
 *
 * @Author: suicaijiao
 * @Description:
 * @Date:Created in 2019/11/20
 * @Modified By:suicaijiao
 */
public class RedisSettings {

    //无缓存有效期
    public static final long NOT_EXPIRE = 0;
    //缓存过期时间为1分钟
    public static final long EXPIRE_MINUTE = 60;
    //缓存过期时间为5分钟
    public static final long EXPIRE_MINUTE_5 = 60 * 5;
    //默认缓存过期时间为1小时
    public static final long EXPIRE_HOUR = 60 * 60;
    //缓存过期时间为24小时
    public static final long EXPIRE_DAY = 24 * 60 * 60;
    //缓存过期时间为一周
    public static final long EXPIRE_WEEK = 7 * 24 * 60 * 60;
    //缓存过期时间为一个月
    public static final long EXPIRE_MONTH = 30 * 24 * 60 * 60;
    //缓存过期时间为三个月
    public static final long EXPIRE_MONTH_3 = 3 * 30 * 24 * 60 * 60;
    //缓存过期时间为六个月
    public static final long EXPIRE_MONTH_6 = 6 * 30 * 24 * 60 * 60;
    //缓存过期时间为九个月
    public static final long EXPIRE_MONTH_9 = 9 * 30 * 24 * 60 * 60;
    //缓存过期时间为一年
    public static final long EXPIRE_YEAR = 12 * 30 * 24 * 60 * 60;

    /**
     * 项目缓存名称
     */
    public static final String TIMBER_PORTAL = "timber-portal";
    /**
     * 用户游戏申请：参数（%s）
     */
    public static final String MATCHING_GAME_USER = "matching_game_user";

    /**
     * 用户游戏申请：参数（%s）
     */
    public static final String GAME_USER = "game_user:%s";

    //实际存储结构：resourceService:coursePermission:courseId:appkey:orgId
    public static final String GAME_ROOM = "game_room:%s";

    /**
     * 根据原资源Id存储数据.
     */
    public static final String FILE_SERVICE_RESOURCEID = "resource_id:%s";

    /**
     * 根据文件Id存储数据.
     */
    public static final String FILE_SERVICE_FILEID = "file_id:%s";

    /**
     * 论文文件存储机构
     */
    public static final String FILE_SERVICE_PAPER = "paper:file_id:%s";
    /**
     * 平台（base）：配置（config）：应用信息（app_info）
     */
    public static  final  String BASE_CONFIG_APP_INFO="base:config:app:%s";
}
