package org.sw.sample.record.rate.service;

import org.springframework.stereotype.Service;
import org.sw.sample.exception.NotFoundException;
import org.sw.sample.record.rate.domain.RateRepository;
import org.sw.sample.record.rate.domain.entity.UserEntity;
import org.sw.sample.record.rate.service.dto.WinRateRequest;
import org.sw.sample.record.rate.service.dto.WinRateResponse;

/**
 * 승률 응용 서비스
 */
@Service
public class RateService {

    private final RateRepository repository;

    public RateService(RateRepository repository) {
        this.repository = repository;
    }

    /**
     * 특정 사용자의 승률 조회
     */
    public WinRateResponse getWinRate(WinRateRequest request) {
        UserEntity user = repository.findByUserNameAndTag(request.getUsername(), request.getTag())
                .orElseThrow(() -> NotFoundException.ofUser(
                        String.format("User not found - username: %s, tag: %s",
                                request.getUsername(), request.getTag())
                ));

        // 승률 계산 (소수 부분 버림)
        int winrate = (int) user.getWinRate();

        return WinRateResponse.of(winrate);
    }
}
