package org.sw.sample.record.rate.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sw.sample.record.rate.api.record.WinRateResponseRecord;
import org.sw.sample.record.rate.service.RateService;
import org.sw.sample.record.rate.service.dto.WinRateRequest;
import org.sw.sample.record.rate.service.dto.WinRateResponse;

/**
 * 승률 API 컨트롤러
 */
@RestController
@RequestMapping("/api/gamerecord")
public class RateController {

    private final RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    /**
     * 특정 사용자의 승률 조회
     * GET /api/gamerecord/winrate?username={username}&tag={tag}
     */
    @GetMapping("/winrate")
    public ResponseEntity<WinRateResponseRecord> getWinRate(
            @RequestParam String username,
            @RequestParam String tag) {
        
        WinRateRequest request = new WinRateRequest(username, tag);
        WinRateResponse response = rateService.getWinRate(request);
        
        return ResponseEntity.ok(new WinRateResponseRecord(response.getWinrate()));
    }
}
