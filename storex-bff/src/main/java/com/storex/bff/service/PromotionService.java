package com.storex.bff.service;

import com.storex.bff.model.Banner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * ĐÃ SỬA: WebClient + Mono + timeout 2s + fallback banner mặc định.
 * Không còn RestTemplate (blocking).
 */
@Service
public class PromotionService {

    private static final Logger log = LoggerFactory.getLogger(PromotionService.class);

    private final WebClient promotionWebClient;

    public PromotionService(WebClient promotionWebClient) {
        this.promotionWebClient = promotionWebClient;
    }

    public Mono<Banner> getActiveBanner() {
        return promotionWebClient.get()
                .uri("/api/banners/active")
                .retrieve()
                .bodyToMono(Banner.class)
                .timeout(Duration.ofSeconds(2))
                .onErrorResume(ex -> {
                    log.warn("Promotion service unavailable/timeout: {}. Returning default banner.",
                            ex.toString());
                    return Mono.just(Banner.defaultBanner());
                });
    }
}
