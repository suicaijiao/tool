package com.soonphe.portal.util.redis;

/**
 * @author: soonphe
 * @date: 2019/10/17
 * @description: Redis常量key
 */
public class RedisKeyConstant {

    /**
     * Menu 全部菜单保存key
     */
    public static final String SYSUSER_MENU_KEY_ALL = "SYSUSER:MENU:KEY:ALL";

    /**
     * Menu 角色对应菜单保存key
     */
    public static final String SYSUSER_MENU_KEY = "SYSUSER:MENU:KEY:";

    /**
     * APK最新版本信息
     */
    public static final String VERSION_APK_LATEST = "VERSION:APK:LATEST:";

    /**
     * APK下载地址
     */
    public static final String VERSION_APK_ADDRESS = "VERSION:APK:ADDRESS:";

    /**
     * 资产编号
     */
    public static final String DEVICE_ASSET_NUMBER = "DEVICE:ASSETNUMBER:";

    /**
     * 车组
     */
    public static final String DEVICE_GROUPID = "DEVICE:GROUPID:";
    /**
     * 游戏房间用户ID集合
     */
    public static final String GAME_ROOM = "GAME:ROOM:";
    /**
     * 游戏房间用户NAME集合
     */
    public static final String GAME_ROOM_NAME = "GAME:ROOM:NAME:";
    /**
     * 游戏当前用户ID
     */
    public static final String GAME_ROOM_USER = "GAME:ROOM:USER:";
    /**
     * 最新用户获奖ID
     */
    public static final String GAME_WIN_ID = "GAME:WIN:ID:";
    /**
     * 机器人游戏次数统计
     */
    public static final String GAME_ANDROID_COUNT = "GAME:ANDORID:COUNT:";

    /**
     * 机器人中奖统计
     */
    public static final String GAME_ANDROID_WIN = "GAME:ANDORID:WIN:";

    /**
     * 钱包密码Secret
     */
    public static final String WALLET_SECRET = "WALLET:SECRET:";

    /**
     * 局数ID
     */
    public static final String GAME_NUMBER = "GAME:NUMBER:";

    /**
     * WALLET_LIMIT
     */
    public static final String WALLET_LIMIT = "WALLET:LIMIT:";
}
