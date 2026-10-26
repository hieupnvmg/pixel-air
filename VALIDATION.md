# Kiểm tra 0.14

- assembleDebug, lintDebug và 10 lớp kiểm tra model qua. DrawerPagesTest bao phủ phân trang 37 game, biên nhóm, trang cuối, toàn bộ ID xuất hiện một lần, đổi dung lượng trang và chế độ ABC.
- Máy ảo API 37: Settings lưu hướng Ngang; cài đè giữ mặc định Dọc. Dừng/khởi động lại vẫn giữ hướng đã chọn.
- Trang đầu hiện đủ 8 app hay dùng; vuốt sang Công cụ rồi vuốt ngược trả về đúng trang; chip nhảy sang Media. Mở Clock từ trang ngang và trở lại drawer vẫn hiện đủ icon.
- Tìm kiếm lấy app ở trang khác; mở lại drawer bắt đầu từ trang đầu. Vuốt xuống đóng drawer ngang.
- Thu nhỏ vùng hiển thị thành 1080×1200: ABC có 20 app trên 5 trang; duyệt bằng nút Next kiểm tra 20 tên icon khác nhau, không thiếu hoặc trùng. Đã khôi phục kích thước máy ảo.
- Đã sửa lỗi dựng trang trong layout callback bằng cách xếp việc tính lại trang sang lượt xử lý tiếp theo; kiểm tra trang đầu sau khởi động lạnh không trống.
- Chuyển Dọc/Ngang giữ lựa chọn Media đang thu; ngang vẫn hiện Photos, quay lại dọc vẫn thu. Nhấn giữ app và Back không làm thay đổi bố cục Home.
- Tiếng Việt trên drawer ngang và điều hướng khi tắt animation hệ thống qua; đã khôi phục locale, animation và chế độ Dọc trên máy ảo.
- Chữ ký APK hợp lệ; không có AndroidRuntime exception trong các luồng thử.
- Chưa đo FPS/độ trễ; chưa thử trên REDMAGIC Android 16 thật.

## Kiểm tra 0.13

- assembleDebug và lintDebug qua trên JDK đi kèm Android Studio.
- Máy ảo API 37: chạm Media thu/mở đúng các app, lưu lựa chọn và giữ sau khi dừng/khởi động lại tiến trình.
- Chip Media tự mở nhóm đã thu rồi cuộn đến; tìm Photos trong nhóm đã thu vẫn có kết quả. Đóng tìm kiếm giữ trạng thái thu gọn.
- Thu riêng nhóm Hay dùng và Công cụ không ảnh hưởng Media; chip tự mở Công cụ khi animation hệ thống tắt. Đã khôi phục cài đặt animation và mở lại các nhóm trên máy ảo.
- Chữ ký APK hợp lệ. Không có AndroidRuntime exception trong các luồng thử. Chưa thử trên REDMAGIC thật.

## Kiểm tra 0.12

Kiểm tra bổ sung 0.12, máy ảo Android 17 / API 37:

- assembleDebug và lintDebug thành công; bộ kiểm tra model gồm 9 lớp qua.
- DrawerSectionsTest: 8 app hay dùng, không trùng ID, nhóm 9 game/6 media hiện đủ nhiều hàng, nhóm rỗng bị bỏ, fallback loại lạ, vị trí header và chế độ ABC.
- Kiểm tra UI thực tế: 8 app Hay dùng, chip Media cuộn đến app, All trở về đầu mà giữ nguyên các nhóm; tìm YouTube xuyên nhóm; đóng/mở khôi phục danh sách.
- Chuyển ABC → Hay dùng + ABC → Hay dùng + loại thành công. Chạm Clock trên hàng tái sử dụng mở ứng dụng thật. Không có AndroidRuntime exception trong các luồng thử.
- Đổi locale máy sang vi-VN: ô tìm kiếm, chip Tất cả và header Công cụ chuyển sang tiếng Việt; đã khôi phục locale gốc.
- Chữ ký APK v2 hợp lệ.
- Ảnh drawer trong README lấy từ APK 0.12 trên máy ảo, không phải ảnh concept.
- Chưa đo FPS/độ trễ và chưa thử trên REDMAGIC Android 16 thật.

## Kiểm tra 0.11

Kiểm tra bổ sung 0.11, máy ảo API 37:

- `assembleDebug` và `lintDebug` thành công; không có lỗi lint chặn build.
- Unit UnlockGateTest kiểm tra peek pending không tiêu thụ chu kỳ; chỉ take khi ready, một lần mỗi chu kỳ, expiry và rời Home.
- Chu kỳ khóa/mở khóa thực tế: log một lần bắt đầu animation từ OnPreDraw; trước đó icon được giữ ẩn. Không phát khi trở về từ Clock, không phát trễ sau khi mở khóa vào Clock, tắt animation Android không phát hiệu ứng. Không có AndroidRuntime exception.
- Quay máy ảo ở 540×1200, xem các khung hình trước khi Home xuất hiện, khi icon đang ẩn và sau khi icon bung vào vị trí. Không thấy trạng thái đầy đủ xuất hiện trước rồi bị thu lại để chạy lần hai trong lần quay thử.
- So sánh đoạn mẫu YouTube ở các mốc 71%, 75%, 82%: lưới bắt đầu nhỏ/gom tâm rồi mở rộng. Đổi quỹ đạo và scale cho cùng hướng chuyển động, không bảo đảm trùng khít chuyển cảnh của launcher hệ thống Samsung.
- Vẫn cần thử trên REDMAGIC Android 16 với mở khóa vân tay/PIN/khuôn mặt và chuyển cảnh ROM thực tế.

Các mục dưới đây ghi lại kiểm tra bản trước.

Kiểm tra bổ sung 0.10, máy ảo Android 17 / API 37:

- Build APK và lint thành công, không có lỗi chặn build; còn cảnh báo.
- Xóa widget Analog đang đặt từ thanh công cụ của khung widget: không còn AppWidgetHostView, xóa widget/geometry/reserved/pendingPlacement; icon, dock và số trang giữ nguyên. Khởi động lại tiến trình không xuất hiện widget cũ.
- Thêm lại widget thật qua Create và Scallop, Save, sau đó xóa qua Cài đặt. Không còn widget/reserved/native view; ứng dụng Google Clock vẫn được cài. AppWidget service không còn widget của Pixel Air sau khi xóa.
- Unit UnlockGateTest: một lần mỗi chu kỳ, chờ focus, không lặp, hết hạn, không phát sau khi rời Home và chu kỳ tắt màn hình mới.
- Kiểm tra bằng sự kiện tắt/bật màn hình thật và keyguard không có PIN của máy ảo; dùng `wm dismiss-keyguard` để kết thúc keyguard. Hiệu ứng bắt đầu một lần khi Home có focus và keyguard đã đóng, kể cả khi không nhận USER_PRESENT.
- Mở Clock rồi về Home bình thường: không phát hiệu ứng. Khóa/mở khóa vào Clock rồi mới về Home: không phát hiệu ứng. Đặt animator_duration_scale=0: không phát hiệu ứng.
- Công tắc trong cài đặt launcher tắt được hiệu ứng khi animation Android vẫn bật; bật lại lưu đúng. Có khoảng chờ 110 ms sau khi keyguard đóng và Home có focus để phần bay vào ít bị chuyển cảnh hệ thống che; chạm/rời Home hủy cả tác vụ đang chờ.
- Không có AndroidRuntime exception trong các luồng kiểm tra. Giá trị khóa màn hình và tùy chọn animation của máy ảo được khôi phục sau kiểm tra.
- Cần thử trên REDMAGIC Android 16: mở khóa bằng vân tay/khuôn mặt/PIN, thời điểm chuyển cảnh của ROM và độ mượt ở tần số quét thực tế.

Các mục dưới đây ghi lại kiểm tra bản trước.

Kiểm tra bổ sung 0.9 (26/10/2026), máy ảo API 37, chưa thử REDMAGIC thật:

- Build `assembleDebug` và `lintDebug` thành công, không có lỗi lint; vẫn còn cảnh báo.
- Cài đè giữ workspace và widget ID 3 từ 0.8; migration đặt drawerStyle 0, drawerOpacity 80, groupByCategory true.
- Chỉnh icon tới 64 dp, xem trước giá trị, xác nhận ImageView trên Home và drawer rộng 168 px ở density 420 dpi.
- Widget thật: mở placement, thêm trang, kéo đổi tọa độ, kéo góc đổi kích thước, Save lưu widgetPage/X/Y/Width/HeightFraction và footprint dành riêng.
- Khởi động lại tiến trình giữ đúng ID, trang, tọa độ và kích thước widget.
- Giữ widget thật mở movement; Cancel giữ lại geometry đã lưu.
- Thêm Analog từ picker → cấp quyền Create → cấu hình Scallop → placement. Widget cũ vẫn giữ trong lúc xem trước widget mới.
- Cố đặt lên đồng hồ trang đầu: hiển thị Overlaps content và không cho Save. Cancel xóa pendingPlacement, giữ widget cũ.
- Thêm candidate mới, chọn + Page, Save đặt widget mới đúng trang được chọn.
- Đổi **ngôn ngữ thiết bị thực tế bằng cmd locale set-device-locale**, không dùng app locale override: en-US → vi-VN chuyển drawer, nhóm và Settings sang Việt; đổi lại en-US chuyển về Anh. Đã khôi phục locale ban đầu; app locale list rỗng.
- Không có AndroidRuntime exception trong các luồng kiểm tra.
- Unit WidgetGeometryTest: giới hạn normalized coordinates, cạnh chạm không overlap, copy độc lập; AppCategoriesTest kiểm tra metadata/fallback.
- Đã xem ảnh drawer tiếng Anh, trang đặt widget và các bản xem trước.

Giới hạn hiện tại: một widget, widget mới thay widget hiện tại sau Save; chưa có nhiều widget đồng thời. Native widget/provider khác và cử chỉ trên REDMAGIC Android 16 cần thử máy thật. Tiếng Việt/Anh đã dịch, locale khác fallback Anh.

Các phần dưới đây ghi lại kiểm tra các bản cũ.

Kiểm tra bổ sung 0.8 (26/10/2026):

- `assembleDebug` và `lintDebug` thành công; không có lỗi lint, còn cảnh báo.
- Unit test AppCategories: ưu tiên category do Android cung cấp, dự phòng tên/gói, nhóm công cụ/media/liên lạc/công việc và app không nhận diện.
- Cài đè 0.7 giữ workspace, lượt mở và cấu hình kính mờ.
- Chọn chế độ hai hàng hay dùng + Theo loại, hiển thị tiêu đề nhóm và tìm Photos từ toàn bộ danh sách.
- Lưới chọn icon bằng hình, thay icon Camera bằng hình Clock, lưu lựa chọn riêng, rồi khôi phục icon mặc định; ứng dụng đích vẫn là Camera.
- Chọn lại nhóm Camera thành Công cụ và lưu đúng.
- Giữ vùng trống mở chỉnh Home; chạm ô Camera mở các thao tác chỉnh trực tiếp; kết thúc chỉnh bằng Xong.
- Trình chọn widget có thẻ ảnh xem trước và tìm kiếm. Chọn Analog, hoàn tất hộp thoại Create widget và màn hình Select a Clock Face của Google Clock; AppWidget ID được lưu và widget thật hiển thị trên Home.
- Thanh chỉnh chiều cao cập nhật vùng widget trực tiếp và lưu 132 dp trong lần thử đầu.
- Kiểm tra lại APK cuối: widget còn sau cài đè, thanh chỉnh kích thước cập nhật và lưu; đóng trình xem trước/widget editor đúng; Cài đặt → Sửa nhóm mở lưới hình, Camera giữ nhóm Công cụ và đặt lại Tự động xóa override.
- Khởi động lại tiến trình giữ chế độ Theo loại, AppWidget ID và kích thước. Không có AndroidRuntime exception trong các luồng thử.
- Chữ ký APK được apksigner xác minh; metadata versionCode 8, versionName 0.8, minSdk 33, targetSdk 36.
- Đã xem ảnh máy ảo của drawer phân nhóm, Home có ô chỉnh, thẻ xem trước widget.

Giới hạn: chưa thử REDMAGIC thật; chỉ một vùng widget ở trang đầu, chiều cao 60–160 dp; chưa có nhiều widget đặt tự do. Chọn icon riêng hiện lấy các icon app đã cài theo bộ icon đang chọn, chưa duyệt toàn bộ icon thay thế của icon pack. Nhóm tự động cần người dùng sửa khi metadata/tên không đủ rõ.

Môi trường: Mac Apple Silicon; máy ảo Android 17 / API 37. Đích sử dụng là REDMAGIC 11 Air Android 16, chưa thử trên máy thật hoặc máy ảo Android 16.

Đã kiểm tra:

- Build APK và lint thành công, không có lỗi chặn build; còn cảnh báo về API, khả năng truy cập và bố cục.
- Model: sức chứa mỗi trang, tự thêm trang, dock dùng chung, đổi vị trí qua trang, gộp thư mục và xóa trang trống.
- Cài đè từ 0.1; các icon, thư mục và dock được giữ đúng sau chuyển dữ liệu.
- Vuốt lên mở ngăn ứng dụng; tìm kiếm Chrome.
- Vuốt xuống đóng ngăn ứng dụng; kéo lên ít và chậm sẽ hủy mở.
- Thêm trang; vuốt trái/phải; tọa độ dock không đổi khi chuyển trang.
- Mỗi trang có nội dung riêng; số trang, trang hiện tại và icon vẫn còn sau khi khởi động lại.
- Giữ icon ở mép để chuyển sang trang khác, đặt vào ô mới và lưu dữ liệu.
- Không có lỗi AndroidRuntime trong các luồng kiểm tra trên.

Cần thử trên điện thoại: cảm giác cử chỉ với REDMAGIC OS, hiệu năng ở tần số quét thực tế, DPI/kích thước chữ, ứng dụng Google và từng loại widget. Video chạy thử là ghi màn hình máy ảo, không phải video từ REDMAGIC.

Kiểm tra bổ sung cho 0.3:

- Vùng workspace có bounds x=0..1080 trên màn hình máy ảo rộng 1080 px.
- Khi giữ ngón tay ở giữa thao tác, khoảng dịch của icon khớp khoảng kéo trừ touch slop; dock không đổi vị trí.
- Kéo ngắn/chậm trả về trang cũ; kéo đủ trang chuyển sang trang kế tiếp.
- Bắt lại animation đang chạy và kéo ngược hoạt động; không phải đợi animation kết thúc.
- Vuốt nhanh trái/phải liên tiếp rồi trở về trang đầu giữ trạng thái nhất quán.
- Kiểm tra hồi quy mở/đóng danh sách app và kéo icon qua mép trang.
- Kiểm tra độc lập SwipePhysics: chọn trang, fling, đổi hướng, giới hạn trang, thời gian phụ thuộc vận tốc.

Đây là kiểm tra hành vi và hình học trên máy ảo, không phải phép đo FPS/độ trễ trên REDMAGIC.

Kiểm tra bổ sung 0.4:

- Bỏ mũi tên dưới cùng; đầu drawer dùng vạch kéo nhỏ.
- Drawer dùng GridView tái sử dụng view và được giữ sẵn khi ẩn; lọc từ khóa vẫn hoạt động.
- ABC hiển thị đúng tên và thứ tự; Hay dùng đưa Clock lên đầu sau các lượt mở thực tế.
- Lựa chọn sort và lượt mở vẫn còn sau khi dừng/khởi động lại tiến trình.
- Mở app ở trang 2 rồi dùng nút Home hệ thống trở về đúng trang 2, drawer đã đóng.
- WorkspacePager và GridView giữ nguyên identity trước và sau mở app/quay lại Home, xác nhận không dựng lại các view này khi resume.
- Nhấn giữ icon hiện menu xóa; xóa Calendar khỏi Home làm trống ô nhưng Calendar vẫn cài và vẫn nằm trong drawer.
- Giữ rồi kéo vẫn di chuyển icon từ ô này sang ô khác.
- AppRankingTest qua: ABC, số lượt mở, tie-break và bộ đếm lớn.
- Build/lint và kiểm tra chữ ký APK qua; không có lỗi AndroidRuntime trong các luồng thử.

Chế độ Hay dùng chỉ đếm lượt mở qua icon/app trong Pixel Air, không phải thống kê toàn máy. Bản này giảm nguồn khựng bên trong launcher; chưa thể xác nhận animation System UI trên REDMAGIC thật.

Kiểm tra bổ sung 0.5:

- Thanh Google cao hơn mặc định, kiểm tra bằng bounds trên máy ảo.
- Giữ vùng trống mở SettingsActivity riêng; nút bánh răng trong drawer mở cùng màn hình.
- Lưu lựa chọn độ nâng thanh Google, tên icon, chấm trang, cảm giác chuyển trang và độ nhạy.
- Quay về Home áp dụng đúng các lựa chọn; mở lại Settings hiển thị lựa chọn đã lưu.
- Kéo ở mép trang có độ đàn hồi giới hạn, thả tay trở về đúng vị trí.
- Vuốt qua lại hoàn tất chuyển trang theo cơ chế spring, không có lỗi AndroidRuntime.
- SpringMotionTest qua: liên tục vị trí/vận tốc lúc thả, hội tụ, độc lập nhịp lấy mẫu, đàn hồi mép và đổi hướng.
- Build và lint thành công. Kiểm tra trên máy ảo Android 17; chưa đo độ trễ/FPS hay xác nhận cảm giác trên REDMAGIC thật.

Kiểm tra bổ sung 0.6:

- Seed lượt mở của 10 app trên máy ảo: đúng 8 app đầu ở hai hàng Hay dùng; phần còn lại ABC và không trùng entry.
- Tìm kiếm vẫn tìm app ngoài nhóm Hay dùng; chế độ ABC toàn bộ vẫn hoạt động.
- Danh sách dùng hàng tái sử dụng: chạm app để mở và nhấn giữ để hiện menu đều qua kiểm tra.
- Gói tài nguyên mẫu khai báo intent ADW và res/xml/appfilter.xml được phát hiện trong bộ chọn.
- Icon mapped thay đổi thật trên drawer, Home, dock và ảnh nhỏ trong thư mục (đối chiếu ảnh chụp).
- App không có mapping và mapping trỏ resource thiếu giữ icon gốc.
- Lựa chọn bộ icon được giữ sau restart.
- Gỡ gói mẫu: launcher dùng lại icon gốc, bộ chọn báo gói không còn cài; nút dùng icon gốc lưu lại lựa chọn mặc định.
- IconPackMappingTest qua: canonical component, class tương đối, chuỗi thiếu/không hợp lệ.
- Build/lint thành công; không có lỗi AndroidRuntime trong các luồng thử.

Kiểm tra icon pack dùng fixture tự tạo chỉ có tài nguyên. Chưa kiểm tra mọi gói icon trên Play Store hoặc thiết bị REDMAGIC thật.

Kiểm tra bổ sung 0.7:

- Giữ Home hiện đủ hai lựa chọn, mục đổi hình nền mở trình chọn hệ thống mà không tự thay nền.
- Giữ app trong drawer rồi thả tạo đúng một shortcut trên Home.
- Giữ và kéo từ drawer vào ô trống đặt được app; Back hủy kéo không tạo shortcut.
- Lưu kiểu kính, độ đậm và radius blur từ SettingsActivity.
- Trên máy ảo có hỗ trợ, WindowManager nhận FLAG_BLUR_BEHIND; ảnh chụp cho thấy nền kính trên wallpaper và icon/nội dung không bị blur.
- Tắt blur hệ thống tạm bằng settings CLI: drawer vẫn sử dụng được, flag blur được bỏ. Khôi phục giá trị gốc: blur hoạt động lại.
- Đóng drawer trở về Home bỏ flag blur. Không có lỗi AndroidRuntime của ứng dụng.
- Build/lint và kiểm tra chữ ký APK qua. Chưa kiểm tra trực tiếp trên REDMAGIC.

Ghi chú môi trường: lệnh `wm disable-blur true` của image API 37 báo lỗi attribution của shell; dùng Settings CLI chuẩn để kiểm tra cùng trạng thái rồi khôi phục đúng giá trị ban đầu. Đây không phải lỗi runtime của launcher.
