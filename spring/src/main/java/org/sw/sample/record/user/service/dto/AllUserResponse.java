package org.sw.sample.record.user.service.dto;

import org.sw.sample.node.UserNode;

import java.util.List;

/**
 * 모든 사용자 조회 응답 DTO
 */
public class AllUserResponse {
    private final List<UserNode> users;

    private AllUserResponse(List<UserNode> users) {
        this.users = users;
    }

    public List<UserNode> getUsers() {
        return this.users;
    }

    public int getSize() {
        return this.users.size();
    }

    public static AllUserResponse of(List<UserNode> users) {
        return new AllUserResponse(users);
    }
}
