# Báo cáo phân tích — RestTemplate trong WebFlux

## Cơ chế RestTemplate

RestTemplate dùng blocking HTTP client (HttpURLConnection / Apache HttpClient).  
Luồng gọi:

```
Thread nhận request
  → gọi restTemplate.getForObject(...)
  → thread BỊ BLOCK chờ network
  → có response mới tiếp tục
```

Dưới tải cao: số thread bị block ≈ số request đang chờ promotion-service → Tomcat/reactor thread pool cạn.

## Vì sao WebFlux không chịu được RestTemplate

WebFlux dựa trên **Project Reactor + Netty event-loop**:
- Số thread event-loop cố định, nhỏ (thường = số CPU core).
- Mỗi thread phải luôn sẵn sàng xử lý nhiều connection.
- Gọi API blocking trên event-loop = **starvation**: không còn thread để accept/xử lý request mới → 500 / treo.

## Kết luận

Trong ứng dụng WebFlux, mọi lời gọi HTTP ra ngoài phải dùng **WebClient** (hoặc client reactive khác) và trả về `Mono`/`Flux`, kèm timeout + fallback để không crash khi service phụ thuộc chậm/chết.
