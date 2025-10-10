package org.sw.sample.record.rate.infra;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.sw.sample.record.rate.domain.RateRepository;
import org.sw.sample.record.rate.domain.entity.UserEntity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * JSON 파일 기반 사용자 리포지토리 구현체
 */
@Repository
public class JsonRateRepository implements RateRepository {

    private static final String DATA_FILE = "./data/records.json";
    private final List<UserEntity> users;

    public JsonRateRepository() throws IOException {
        this.users = new ObjectMapper().readValue(new File(DATA_FILE), new TypeReference<>() {
        });
    }

    @Override
    public Optional<UserEntity> findByUserNameAndTag(String userName, String tag) {
        return this.users.stream()
                .filter(user -> user.getUsername().equals(userName))
                .filter(user -> user.getTag().equals(tag))
                .findFirst();
    }
}
