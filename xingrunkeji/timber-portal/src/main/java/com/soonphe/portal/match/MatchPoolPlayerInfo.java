package com.soonphe.portal.match;


public class MatchPoolPlayerInfo {
    private int playerId;//玩家ID
    private long startMatchTime;//开始匹配时间


    private MatchPoolPlayerInfo(int playerId) {
        super();
        this.playerId = playerId;
        this.startMatchTime = System.currentTimeMillis();
    }

    public int getPlayerId() {
        return playerId;
    }

    public long getStartMatchTime() {
        return startMatchTime;
    }
}