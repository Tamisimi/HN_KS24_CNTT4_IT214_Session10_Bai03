# Bài 3 Session 10 — Chuyển RestTemplate sang WebClient (Spring WebFlux)

## 1. Phân tích lỗi (code gốc)

```java
private final RestTemplate restTemplate = new RestTemplate();
public Banner getActiveBanner() {
    Banner banner = restTemplate.getForObject(url, Banner.class); // BLOCKING
    ...
}
```

### Vì sao nghiêm trọng trong WebFlux?

1. **RestTemplate là blocking I/O**  
   Mỗi lời gọi giữ **1 thread** cho đến khi có response (hoặc timeout mặc định rất dài). Thread không làm việc khác trong lúc chờ mạng.

2. **WebFlux dùng event-loop (ít thread)**  
   WebFlux thiết kế non-blocking: một số ít thread (event loop) phục vụ hàng nghìn request. Khi gọi RestTemplate bên trong, thread event-loop bị **block** → không xử lý request khác → pool cạn → **HTTP 500 / treo cứng** dưới tải cao (Flash Sale 10.000 user).

3. **Phá vỡ mô hình reactive**  
   Toàn bộ pipeline WebFlux chỉ non-blocking khi mọi bước I/O đều reactive (`Mono`/`Flux` + WebClient). Một chỗ RestTemplate là đủ để “phá” kiến trúc.

4. **Không timeout / không fallback**  
   Service chết → exception → crash thay vì trả banner mặc định.

## 2. Cách sửa

- Dùng **WebClient** (non-blocking)
- Trả về **`Mono<Banner>`**
- **Timeout 2 giây** + **fallback** banner mặc định

## 3. File chính

- `PromotionService.java` — đã sửa
- `WebClientConfig.java` — cấu hình WebClient + timeout
- `Banner.java` — model
- `BAO_CAO_PHAN_TICH.md` — phân tích chi tiết
