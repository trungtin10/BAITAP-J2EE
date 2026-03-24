# Cấu hình Google OAuth - Sửa lỗi redirect_uri_mismatch

## Redirect URI cần thêm (copy chính xác)

```
http://localhost:8081/login/oauth2/code/google
```

## Các bước thực hiện

### 1. Mở đúng OAuth Client
- Link: https://console.cloud.google.com/apis/credentials?project=rosy-antler-490503-s9
- Đăng nhập Google
- Trong **OAuth 2.0 Client IDs**, tìm client có **Client ID**: `842236116115-e90743p95mldbot7gfg39v5rv40j2m1m.apps.googleusercontent.com`
- **Bấm vào tên client** (không phải Client ID) để mở trang chỉnh sửa

### 2. Thêm Redirect URI
- Cuộn xuống mục **Authorized redirect URIs**
- Bấm **+ ADD URI**
- **Copy-dán** (không gõ tay) URI sau vào ô:
  ```
  http://localhost:8081/login/oauth2/code/google
  ```
- Kiểm tra: không có khoảng trắng, không có dấu / ở cuối

### 3. Lưu
- Bấm **SAVE** ở cuối trang
- Đợi 2-3 phút để Google cập nhật

### 4. Kiểm tra
- Đảm bảo **Authorized JavaScript origins** có: `http://localhost:8081` (không có path)
- Đảm bảo **Authorized redirect URIs** có: `http://localhost:8081/login/oauth2/code/google`

## Lưu ý
- **Authorized JavaScript origins** ≠ **Authorized redirect URIs** (2 mục khác nhau)
- Redirect URI phải có đầy đủ path `/login/oauth2/code/google`
- Chỉ dùng `http` cho localhost (không dùng `https`)
