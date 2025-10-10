package org.sw.sample.record.user.service;

import org.springframework.stereotype.Service;
import org.sw.sample.node.UserNode;
import org.sw.sample.record.user.domain.UserRepository;
import org.sw.sample.record.user.domain.entity.UserEntity;
import org.sw.sample.record.user.service.dto.AllUserRequest;
import org.sw.sample.record.user.service.dto.AllUserResponse;

import java.util.Comparator;

/**
 * 사용자 응용 서비스
 */
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public AllUserResponse getUsers(AllUserRequest request) {
        return AllUserResponse.of(this.repository.findAll().stream()
                .sorted(Comparator.comparing(UserEntity::getUsername).thenComparing(UserEntity::getTag))
                .map(this::convertTo)
                .toList());
    }

    private UserNode convertTo(UserEntity entity) {
        // TODO record
        UserNode node = new UserNode();
        node.setTag(entity.getTag());
        node.setName(entity.getUsername());
        return node;
    }
}
