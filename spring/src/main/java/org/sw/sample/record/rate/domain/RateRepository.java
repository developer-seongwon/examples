package org.sw.sample.record.rate.domain;

import org.sw.sample.record.rate.domain.entity.UserEntity;

import java.util.Optional;

public interface RateRepository {

    String DATA_FILE = "../../../data/records.json";

    Optional<UserEntity> findByUserNameAndTag(String userName, String tag);
}
