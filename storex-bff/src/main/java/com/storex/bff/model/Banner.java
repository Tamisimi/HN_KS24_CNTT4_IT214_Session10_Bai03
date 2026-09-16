package com.storex.bff.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner {

    private String title;
    private String message;
    private boolean active;

    /** Banner mặc định khi promotion-service timeout / lỗi */
    public static Banner defaultBanner() {
        return Banner.builder()
                .title("Thông báo")
                .message("Khuyến mãi đang được cập nhật")
                .active(false)
                .build();
    }
}
