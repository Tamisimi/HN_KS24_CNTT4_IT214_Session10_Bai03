package com.storex.bff.controller;

import com.storex.bff.model.Banner;
import com.storex.bff.service.PromotionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/home")
public class BannerController {

    private final PromotionService promotionService;

    public BannerController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/banner")
    public Mono<Banner> activeBanner() {
        return promotionService.getActiveBanner();
    }
}
