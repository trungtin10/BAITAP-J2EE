# Course Registration - Hệ thống đăng ký học phần

## Cài đặt

### 1. Database

**Mặc định dùng H2** (chạy ngay, không cần cài MySQL):
```bash
mvnw spring-boot:run
```

**Dùng MySQL**: Sửa `application.properties` hoặc chạy với profile:
```bash
mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```
- Tạo database `course_registration` trong MySQL

### 2. Cấu hình application.properties

Sửa `src/main/resources/application.properties`:
- `spring.datasource.username` và `spring.datasource.password` theo MySQL của bạn

### 3. Google OAuth2 (Câu 9)

1. Vào https://console.cloud.google.com/
2. Tạo project → APIs & Services → Credentials → Create OAuth 2.0 Client ID
3. Chọn Web application, thêm Authorized redirect URI: `http://localhost:8080/login/oauth2/code/google`
4. Copy Client ID và Client Secret vào application.properties

### 4. Chạy ứng dụng

```bash
mvn spring-boot:run
```

Truy cập: http://localhost:8080

## Tài khoản mẫu

Tài khoản được tạo tự động khi chạy ứng dụng lần đầu (DataLoader, mật khẩu mã hóa BCrypt).

| Username | Password   | Role   |
|----------|------------|--------|
| admin    | admin123   | ADMIN  |
| student  | student123 | STUDENT|

## Các chức năng đã triển khai

- **Câu 1**: Trang Home hiển thị danh sách học phần (tên, tín chỉ, giảng viên, hình ảnh) + phân trang 5/trang
- **Câu 2**: CRUD học phần cho ADMIN (Create, Update, Delete)
- **Câu 3**: Đăng ký tài khoản sinh viên (username, password, email) - mặc định role STUDENT
- **Câu 4**: Spring Security - /admin/** (ADMIN), /courses (authenticated), /enroll/** (STUDENT)
- **Câu 5**: Đăng nhập username/password → redirect /home
- **Câu 6**: Nút Đăng ký học phần (chỉ STUDENT)
- **Câu 7**: Trang "Học phần của tôi"
- **Câu 8**: Tìm kiếm theo tên học phần trong Header
- **Câu 9**: Đăng nhập Google OAuth2
- **Câu 10**: Giao diện responsive (Bootstrap 5)
