package org.sw.sample.record.user.service.dto;

/**
 * 승률 조회 응답 DTO
 */
public class WinRateResponse {
    private final int winrate;

    private WinRateResponse(int winrate) {
        this.winrate = winrate;
    }

    public int getWinrate() {
        return winrate;
    }

    public static WinRateResponse of(int winrate) {
        return new WinRateResponse(winrate);
    }
}
