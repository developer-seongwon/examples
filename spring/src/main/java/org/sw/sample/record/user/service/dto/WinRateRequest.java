package org.sw.sample.record.user.service.dto;

/**
 * 승률 조회 요청 DTO
 */
public class WinRateRequest {
    private String username;
    private String tag;

    public WinRateRequest() {
    }

    public WinRateRequest(String username, String tag) {
        this.username = username;
        this.tag = tag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
