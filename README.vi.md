# Pixel Air

Launcher Android thử nghiệm viết bằng Java/native Views, lấy cảm hứng từ giao diện Pixel. Hỗ trợ Android 13 trở lên; phiên bản hiện tại **0.11**.

[English](README.md) · [Lịch sử thay đổi](docs/CHANGELOG.vi.md) · [Chi tiết kiểm tra](VALIDATION.md)

## Tính năng

- Nhiều trang Home, dock chung, thư mục, kéo icon giữa các trang.
- Vuốt lên mở drawer; tìm kiếm, ABC hoặc hai hàng hay dùng + phân nhóm app; sửa nhóm thủ công.
- Icon pack dạng appfilter, thay icon riêng và chỉnh kích thước 36–64 dp.
- Nền tối/kính mờ; mặc định nền tối 80% và xếp app theo loại.
- Một widget: xem trước, chọn trang/vị trí, kéo đổi cỡ và xóa. Lưu widget mới sẽ thay widget hiện tại.
- Hiệu ứng mở khóa: icon được ẩn sau màn khóa rồi bung đồng bộ từ nhỏ ra lớn khi Home bắt đầu vẽ.
- Cài đặt riêng, tự theo ngôn ngữ hệ thống Việt/Anh.

## Tải bản thử

[Tải APK 0.11 trên GitHub Releases](https://github.com/hieupnvmg/pixel-air/releases/tag/v0.11). Đây là bản debug để thử nghiệm.

## Build

Cần Android Studio, SDK Platform **37**, JDK **25**. Gradle Wrapper 9.6.0 và Android Gradle Plugin 9.4.1 đã được cấu hình; Java source tương thích 17.

Mở thư mục trong Android Studio để tạo `local.properties`, hoặc đặt `ANDROID_HOME` tới thư mục SDK. Chạy:

```sh
./gradlew assembleDebug lintDebug
./scripts/test-models.sh
```

APK nằm ở `app/build/outputs/apk/debug/app-debug.apk`. Đây là bản debug để thử; cấu hình SDK của máy và khóa ký không nằm trong repo.

Trên Mac, nếu JDK đi kèm Android Studio là bản 25:

```sh
export JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home'
./gradlew assembleDebug lintDebug
./scripts/test-models.sh
```

Windows có thể build trong Android Studio; script test model cần Git Bash hoặc WSL.

## Cách dùng

Cài APK và đặt Pixel Air làm ứng dụng màn hình chính mặc định. Giữ vùng trống Home để mở cài đặt, widget hoặc chế độ chỉnh bố cục. Giữ widget rồi thả tay để di chuyển/đổi cỡ/xóa. Công tắc hiệu ứng mở khóa nằm trong **Cử chỉ & chuyển động**.

## Trạng thái

Đây là prototype độc lập, không phải Pixel Launcher chính thức. Hiện kiểm tra chủ yếu trên máy ảo Android 17/API 37, chưa được maintainer xác minh trên REDMAGIC thật. Hiệu ứng hệ thống, Recents và cơ chế mở khóa vẫn do ROM quản lý. Chỉ hỗ trợ một widget tại một thời điểm.

Khi báo lỗi, vui lòng ghi model máy, phiên bản Android/ROM, phiên bản launcher và các bước tái hiện. Mã nguồn dùng giấy phép [Apache 2.0](LICENSE); đóng góp và pull request đều được chào đón.
