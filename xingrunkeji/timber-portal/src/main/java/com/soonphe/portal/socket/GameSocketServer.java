package com.soonphe.portal.socket;

import com.alibaba.fastjson.JSONObject;
import com.soonphe.portal.commons.constant.CommonsEnum;
import com.soonphe.portal.model.GameMatchingUser;
import com.soonphe.portal.model.vo.GameEndResultVo;
import com.soonphe.portal.service.ApplicationContextRegister;
import com.soonphe.portal.service.IGmsHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint(value = "/gameSocket/{userId}")
public class GameSocketServer {
    //静态变量，用来记录当前在线连接数，线程安全。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的对象。
    private static CopyOnWriteArraySet<GameSocketServer> webSocketSet = new CopyOnWriteArraySet<GameSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //传入参数 用户id
    private Integer userId;

    private IGmsHistoryService gmsHistoryService;

//    public static SessionCacheManage sessionCacheManage;

    ApplicationContext act = ApplicationContextRegister.getApplicationContext();

    /**
     * 存放游戏用户
     */
    private static Queue<GameMatchingUser> gameUserQueue;

    static {
        if (null == gameUserQueue) {
            //基于链接节点的无界线程安全队列
            gameUserQueue = new ConcurrentLinkedQueue<GameMatchingUser>();
        }
    }

    /**
     * 存放游戏用户
     */
    private static Map<Integer, String> gameMap = new ConcurrentHashMap<>();

    private static final Map<String, List<GameMatchingUser>> rooms = new ConcurrentHashMap();

    //连接打开时执行
    @OnOpen
    public void onOpen(@PathParam("userId") Integer userId, Session session) {
        gmsHistoryService = act.getBean(IGmsHistoryService.class);

        try {
            if (userId != null) {
                this.userId = userId;
                this.session = session;
                webSocketSet.add(this);
                GameMatchingUser gameMatchingUser = gmsHistoryService.checkUserGameValid(userId);
                gameUserQueue.add(gameMatchingUser);
                int size = gameUserQueue.size();
                List<GameMatchingUser> matchingGameUserList = new Vector<>();
                JSONObject result;
                sendMessage("success");
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        // 存储匹配成功用户
                        matchingGameUserList.add(gameUserQueue.poll());
                    }
                    result = new JSONObject();
                    for (GameMatchingUser user : matchingGameUserList) {
                        result.put("socketType", CommonsEnum.GAME_MATCHING.getCode());
                        result.put("userId", user.getUserId());
                        result.put("message", matchingGameUserList);
                        GameSocketServer.sendPublicMessage(user.getUserId(), result);
                    }

                    GameEndResultVo gameEndResultVo = gmsHistoryService.matchingGameUser(matchingGameUserList);

                    if (gameEndResultVo == null) {
                        sendMessage("游戏异常");
                    }
                    List<GameEndResultVo> resultList = new ArrayList<>();
                    resultList.add(gameEndResultVo);

                    result = new JSONObject();

                    for (GameMatchingUser user : matchingGameUserList) {
                        result.put("socketType", CommonsEnum.GAME_LOTTERY.getCode());
                        result.put("userId", user.getUserId());
                        result.put("message", gameEndResultVo);
                        GameSocketServer.sendPublicMessage(user.getUserId(), result);
                    }
                }

            } else {
                sendMessage("fail");
            }
        } catch (Exception e) {
            log.error("gameSocket onOpen error:{}", e.getMessage(), e);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("gameSocket 当前在线人数为 {} ", getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        if (!message.contains("ping")) {
            sendMessage("您发送的信息为：" + message);
        } else {
            sendMessage("pong");
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        try {
            this.session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("gameSocket 当前在线人数为 {} ", getOnlineCount());
    }

    public void sendMessage(String message) {
        try {
            if (this.session.isOpen()) {
                this.session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        GameSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        GameSocketServer.onlineCount--;
    }


    public static void sendPublicMessage(Integer userId, JSONObject message) {
        for (GameSocketServer item : webSocketSet) {
            try {
                if (userId.equals(item.userId)) {
                    item.sendMessage(message.toString());
                }
            } catch (Exception e) {
                log.error("gameSocket sendPublicMessage error:{}", e.getMessage(), e);
                continue;
            }
        }
    }

}
