# EcoTS Project

![EcoTS Logo](link/to/logo.png)

EcoTS là một dự án nhằm mục đích phân loại rác và khuyến khích các hoạt động thiện nguyện và quyên góp trong cộng đồng. Dự án này sử dụng công nghệ để tạo ra một hệ thống phân loại rác thông minh và kết hợp với các chức năng xã hội để thúc đẩy nhận thức về bảo vệ môi trường.

## Tính Năng

- **Phân Loại Rác**: Hệ thống sử dụng công nghệ TensorFlow (YOLOv8) để phân loại rác thành các loại chính: nhựa, giấy, thủy tinh, kim loại.
- **Hoạt Động Thiện Nguyện**: Cung cấp thông tin về các hoạt động thiện nguyện và sự kiện quyên góp trong cộng đồng.
- **Quyên Góp**: Tích hợp chức năng quyên góp để hỗ trợ các dự án bảo vệ môi trường.

## Công Nghệ Sử Dụng

- Spring Boot: Framework Java cho phát triển ứng dụng web.
- PostgreSQL: Hệ quản trị cơ sở dữ liệu quan hệ để lưu trữ dữ liệu chính của hệ thống.
- MongoDB: Cơ sở dữ liệu không quan hệ để lưu trữ dữ liệu linh hoạt, như thông tin về hoạt động thiện nguyện và quyên góp.
- TensorFlow: Thư viện machine learning để phát triển mô hình phân loại rác (YOLOv8).
- Flutter: Framework phát triển ứng dụng di động đa nền tảng để xây dựng giao diện người dùng cho ứng dụng di động.

## Hướng Dẫn Cài Đặt và Sử Dụng

```md
# Clone Repository:

git clone https://github.com/yourusername/EcoTS.git

# Cài Đặt Backend:

- Mở terminal/cmd và di chuyển vào thư mục `backend`:

cd EcoTS/backend

- Cài đặt các dependencies và khởi động Spring Boot:

./gradlew bootRun

# Cài Đặt Flutter App:

- Mở terminal/cmd và di chuyển vào thư mục `flutter_app`:

cd EcoTS/flutter_app

- Cài đặt các dependencies và khởi chạy ứng dụng Flutter:

flutter pub get
flutter run

# Cấu Hình Cơ Sở Dữ Liệu:

- Tạo cơ sở dữ liệu PostgreSQL và MongoDB với các thông tin cấu hình tương ứng.
- Cập nhật các thông tin cấu hình trong file `application.properties` của Spring Boot.

# Chạy Ứng Dụng:

- Khởi động lại backend sau khi cấu hình xong.
- Khởi động ứng dụng di động Flutter để trải nghiệm.
