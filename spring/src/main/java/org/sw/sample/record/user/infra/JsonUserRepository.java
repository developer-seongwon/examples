package org.sw.sample.record.user.infra;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.sw.sample.record.user.domain.entity.UserEntity;
import org.sw.sample.record.user.domain.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * JSON 파일 기반 사용자 리포지토리 구현체
 */
@Repository
public class JsonUserRepository implements UserRepository {
    private static final String DATA_FILE = "./spring/data/records.json";
    private final List<UserEntity> users;

    public JsonUserRepository() throws IOException {
                     this.users = new ObjectMapper().readValue(new File(DATA_FILE), new TypeReference<>() {
        });
    }

    @Override
    public List<UserEntity> findAll() {
        return this.users;
    }
}
