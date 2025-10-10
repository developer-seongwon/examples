package org.sw.sample.record.user.domain;

import org.sw.sample.record.user.domain.entity.UserEntity;

import java.util.List;

/**
 * 사용자 리포지토리 인터페이스
 */
public interface UserRepository {
    /**
     * 모든 사용자 조회
     */
    List<UserEntity> findAll();
}
