package org.sw.sample.record.rate.domain.entity;

/**
 * 사용자 도메인 모델
 */
public class UserEntity {
    private Long id;
    private String username;
    private String tag;
    private Integer win;
    private Integer lose;

    public UserEntity() {
    }

    public UserEntity(Long id, String username, String tag, Integer win, Integer lose) {
        this.id = id;
        this.username = username;
        this.tag = tag;
        this.win = win;
        this.lose = lose;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTag() {
        return tag;
    }

    public Integer getWin() {
        return win;
    }

    public Integer getLose() {
        return lose;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setWin(Integer win) {
        this.win = win;
    }

    public void setLose(Integer lose) {
        this.lose = lose;
    }

    /**
     * 승률 계산
     */
    public double getWinRate() {
        int total = win + lose;
        if (total == 0) return 0.0;
        return (double) win / total * 100;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", tag='" + tag + '\'' +
                ", win=" + win +
                ", lose=" + lose +
                '}';
    }
}
