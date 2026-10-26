# Pixel Air 0.13

## Mới trong 0.13

- Chạm tiêu đề nhóm để thu gọn/mở rộng, mặc định vẫn mở hết. Giữ tên, biểu tượng và số app khi thu gọn.
- Ghi nhớ lựa chọn từng nhóm trên thiết bị, kể cả sau khi khởi động lại.
- Chip tự mở nhóm đang thu gọn trước khi cuộn đến. Tìm kiếm vẫn lấy toàn bộ app, không thay đổi trạng thái đã lưu.
- Mũi tên xoay, app xuất hiện nhẹ khi mở nhóm; bỏ hiệu ứng khi tắt animation hệ thống. Header có trạng thái mở/thu cho trình đọc màn hình.
- Cài PixelAir-0.13.apk đè bản cũ để giữ dữ liệu.

## Mới trong 0.12

- Các nhóm app mở sẵn, hiện đầy đủ nhiều hàng trong cùng một danh sách cuộn.
- Thanh tìm kiếm và chip nhóm cố định; bấm chip cuộn đến nhóm, không lọc hoặc ẩn app khác.
- Header có biểu tượng, màu nhấn, số app và họa tiết theo chủ đề; nền nhóm nhẹ, không dùng thẻ lớn.
- Giữ tối đa hai hàng Hay dùng, loại trùng khỏi nhóm phía dưới; giữ các chế độ ABC, tìm kiếm và nhấn giữ kéo app ra Home.
- Tái sử dụng hàng danh sách và vẽ họa tiết tĩnh để tránh thêm animation khi cuộn. Tôn trọng tùy chọn tắt chuyển động của hệ thống.
- Cài PixelAir-0.12.apk đè bản cũ để giữ dữ liệu.

## Mới trong 0.11

- Cài **PixelAir-0.11.apk** đè bản cũ, giữ bố cục và các tùy chọn đã lưu.
- Sửa lỗi icon hiện sẵn rồi mới chạy hiệu ứng: icon được ẩn khi Home còn sau màn khóa, giữ trạng thái đó qua resume và thay đổi bố cục, bắt đầu animation trong `OnPreDraw` khi keyguard đã đóng và Home có focus. Không còn khoảng chờ 110 ms sau khi Home đã xuất hiện.
- Tham khảo [video người dùng gửi](https://www.youtube.com/shorts/M-Wiqst-MSw): cả lưới icon và dock bắt đầu nhỏ, gom về gần tâm rồi bung đồng bộ tới vị trí/kích thước bình thường trong 500 ms. Không chạy rải từng icon từ ngoài vào như bản cũ.
- Chỉ hoàn tất chu kỳ mở khóa khi khung hình thực sự bắt đầu vẽ, tránh mất hiệu ứng nếu workspace được làm mới trước lần vẽ đó. Chạm hoặc rời Home hủy animation và phục hồi icon; tắt animation giữ Home hiển thị bình thường.
- Giữ nguyên công tắc **Cử chỉ & chuyển động → Icon bay vào khi mở khóa**. Chuyển cảnh màn khóa của REDMAGIC OS vẫn do hệ thống quản lý; bản này xử lý phần Home.

Các mục dưới đây là lịch sử tính năng.

## Mới trong 0.10

- Cài **PixelAir-0.10.apk** đè lên bản trước để giữ bố cục và tùy chọn.
- **Xóa widget ngay trên Home**: giữ widget rồi thả tay để mở khung di chuyển/đổi cỡ, bấm **Xóa widget** ở hàng trên của thanh công cụ. Trong chế độ chỉnh Home → Widget cũng có mục Xóa widget. Đường **Cài đặt → Xóa widget** dùng chung logic dọn dữ liệu.
- Xóa cả ID widget, vị trí/kích thước, vùng dành riêng và trạng thái đang đặt; hủy các tham chiếu tới khung cũ. Callback bố cục cũ không được ghi lại vùng dành riêng sau khi xóa. Ứng dụng cung cấp widget được giữ nguyên.
- **Icon bay vào khi mở khóa**: icon trên trang hiện tại và dock đi từ phía ngoài vào, thu về kích thước bình thường trong khoảng 0,5 giây. Đồng hồ, widget và hình nền đứng yên.
- Mặc định bật. Có thể tắt ở **Cài đặt → Cử chỉ & chuyển động → Icon bay vào khi mở khóa**. Tôn trọng tùy chọn tắt animation của Android; chạm vào Home hoặc rời ứng dụng sẽ kết thúc chuyển động, trả icon về vị trí chuẩn.
- Hiệu ứng chạy một lần khi mở khóa vào Home, không chạy khi thoát app về Home, không đợi tới lần về Home sau nếu đã mở khóa vào app khác. Theo dõi sự kiện màn hình và trạng thái keyguard/focus, không thay màn khóa hoặc cơ chế xác thực của máy.

Tài liệu đối chiếu: [Intent.USER_PRESENT](https://developer.android.com/reference/android/content/Intent#ACTION_USER_PRESENT), [KeyguardManager](https://developer.android.com/reference/android/app/KeyguardManager), [ViewPropertyAnimator](https://developer.android.com/reference/android/view/ViewPropertyAnimator).

Các hướng dẫn 0.9 trở xuống ghi lại lịch sử tính năng; dùng thao tác mới ở trên cho 0.10.

## Mới trong 0.9

- Cài **PixelAir-0.9.apk** đè bản cũ. Lần đầu mở bản này, mặc định chuyển sang **Nền tối, độ đậm 80%, hai hàng hay dùng + Theo loại** theo yêu cầu. Các lần mở sau vẫn giữ tùy chọn bạn đổi; bố cục và widget cũ được giữ.
- **Thêm widget**: chọn thẻ xem trước, hoàn tất cấp quyền/cấu hình, rồi chọn chỗ đặt trên Home. Kéo widget để di chuyển, kéo nút góc phải dưới để đổi cỡ, dùng **‹ / ›** để chọn trang hoặc **+ Trang** để tạo trang. Chạm vùng trống để đưa widget tới đó; **Lưu** để xác nhận, **Hủy/Back** để giữ bố cục cũ.
- **Giữ widget rồi thả tay** để mở chế độ di chuyển. Cũng có thể vào **Cài đặt → Di chuyển / đổi cỡ widget** hoặc chế độ chỉnh Home → Widget → Di chuyển / đổi cỡ.
- Khung đỏ báo đè lên icon/đồng hồ, không cho lưu. Widget được giới hạn trong workspace và kích thước theo thông tin nhà cung cấp. Chỗ widget chiếm được dành riêng khi thêm/kéo app; trang chứa widget không bị xóa nhầm như trang trống.
- Vị trí, kích thước và trang của widget được lưu theo tỷ lệ workspace. Hủy widget mới sẽ hủy ID mới, giữ widget cũ. Hiện vẫn hỗ trợ **một widget**: bấm Lưu widget mới sẽ thay widget hiện tại; chưa có nhiều widget đồng thời.
- **Cài đặt → Màn hình chính → Kích thước icon**: 36–64 dp, xem trước ngay trong cài đặt; áp dụng cho Home, dock và danh sách app khi quay lại.
- Giao diện tự theo ngôn ngữ hệ thống: **tiếng Việt và tiếng Anh**. Ngôn ngữ chưa có bản dịch dùng tiếng Anh; không ép ngôn ngữ riêng. Ngày và tên ứng dụng theo locale Android.

Đối chiếu API: [Android localization](https://developer.android.com/guide/topics/resources/localization), [Widget host](https://developer.android.com/develop/ui/views/appwidgets/host).

Các ghi chú 0.8 trở xuống là lịch sử tính năng; dùng hướng dẫn 0.9 ở trên.

## Mới trong 0.8

- Cài **PixelAir-0.8.apk** đè lên bản cũ để giữ bố cục và tùy chọn.
- Drawer → Sắp xếp ứng dụng → **2 hàng hay dùng + Theo loại**. Tối đa 8 app hay dùng ở đầu; phần còn lại chia nhóm Trò chơi, Công cụ, Công việc, Media, Liên lạc, Bản đồ, Tin tức, Mua sắm và Khác. Mỗi nhóm xếp ABC, nhóm trống được ẩn, không lặp app hay dùng. Tìm kiếm vẫn tìm toàn bộ app.
- **Cài đặt → Sửa nhóm ứng dụng**: chọn app qua lưới icon rồi sửa nhóm hoặc chọn Tự động. Nhận diện dùng metadata Android trước và tên/gói làm dự phòng, có thể cần sửa thủ công.
- **Giữ vùng trống → Chỉnh màn hình chính**: chạm trực tiếp các ô được đánh dấu để thay app, đổi icon hoặc xóa. Ô trống và trình chọn icon dùng lưới hình có tìm kiếm. Giữ icon để kéo; Xong hoặc Back để kết thúc.
- **Đổi biểu tượng** chọn hình từ các app đã cài, áp dụng bộ icon hiện tại. Chỉ thay hình, vẫn mở app ban đầu. **Icon mặc định** xóa lựa chọn riêng. Chưa duyệt toàn bộ icon thay thế/ẩn của icon pack.
- **Giữ vùng trống → Widget**: thẻ có ảnh xem trước của nhà cung cấp hoặc icon với ghi chú khi thiếu ảnh; có tìm kiếm. Chọn thẻ rồi hoàn tất cấp quyền/cấu hình Android.
- Khi chỉnh Home, chạm **Chỉnh widget** để xem trực tiếp thay đổi chiều cao 60–160 dp. Hiện vẫn chỉ có một vùng widget ở trang đầu; chưa hỗ trợ nhiều widget tự do trên mọi trang. Widget lớn có thể không vừa vùng này.
- Quản lý trang và đặt launcher mặc định nằm trong **Cài đặt Pixel Air**.

Đối chiếu API: [ApplicationInfo.category](https://developer.android.com/reference/android/content/pm/ApplicationInfo#category), [AppWidgetProviderInfo](https://developer.android.com/reference/android/appwidget/AppWidgetProviderInfo).

Các mục dưới đây ghi lại tính năng của các bản trước; dùng đường dẫn thao tác mới ở trên cho bản 0.8.

Launcher độc lập cho REDMAGIC 11 Air quốc tế chạy Android 16. Giao diện theo ảnh người dùng cung cấp, tham khảo Pixel Launcher và video Pixel Launcher Extended. Đây không phải APK Pixel Launcher chính thức hoặc bản port đầy đủ của Google.

## Cài đặt

Cài PixelAir-0.8.apk đè lên bản trước để giữ icon, thư mục và số trang để giữ bố cục. Mở Pixel Air, giữ vùng trống → Đặt làm màn hình chính mặc định. Có thể đổi lại launcher REDMAGIC trong Cài đặt hệ thống → Ứng dụng mặc định → Ứng dụng màn hình chính.

## Thao tác

- Vuốt lên để mở danh sách ứng dụng. Ngăn ứng dụng đi theo ngón tay; thả tay để mở hẳn hoặc trở về theo khoảng kéo và tốc độ.
- Vuốt xuống khi danh sách đang ở đầu để đóng; hoặc dùng nút Back. Khi danh sách đã cuộn xuống, vuốt sẽ cuộn về đầu trước.
- Vuốt trái/phải để đổi trang màn hình chính. Dock và thanh tìm kiếm cố định giữa các trang. Chạm chấm trang để chuyển nhanh.
- Giữ vùng trống → Thêm trang màn hình chính. Trang đầu có đồng hồ, ngày, vùng widget và 8 ô ứng dụng; các trang sau có 20 ô. Dock có 4 ô dùng chung.
- Nhấn giữ icon để mở menu **Xóa khỏi màn hình chính** hoặc **Thông tin ứng dụng**. Xóa icon không gỡ ứng dụng. Giữ tiếp rồi kéo vẫn di chuyển icon sang ô khác. Thả lên icon có sẵn để chọn đổi vị trí hoặc gộp thư mục.
- Muốn chuyển icon sang trang khác: kéo sát mép trái/phải, giữ khoảng 0,6 giây rồi kéo vào ô muốn đặt. Giữ ở mép phải trang cuối có nội dung sẽ tạo thêm trang.
- Khi thêm ứng dụng vào màn hình chính mà trang hiện tại đã đầy, launcher tìm trang còn trống hoặc tự tạo trang.
- Giữ app trong danh sách để đưa ra Home, rồi kéo/thả vào ô trống. Nếu thả ở vùng trống khác, launcher chọn ô còn trống. Dùng Back để hủy kéo.
- Chạm ô trống để chọn ứng dụng. Hoặc giữ vùng trống → Chỉnh các ô ứng dụng. Xóa shortcut không gỡ ứng dụng.
- Giữ vùng trống → Xóa trang trống hiện tại. Trang đầu không bị xóa.
- Giữ vùng trống → Thêm widget. Hiện hỗ trợ một vùng widget ở trang đầu, chưa kéo giãn tự do. Xóa bằng mục Xóa widget.
- Khi từ app quay về Home, launcher giữ trang đã mở app và giao diện có sẵn. Nhấn Home thêm lần nữa khi đang ở màn hình chính để về trang đầu.

## Bản 0.7: nền drawer, kéo app và menu Home

- Giữ vùng trống hoặc ô trống trên Home mở menu **Đổi hình nền / Cài đặt Pixel Air**.
- Giữ icon trong danh sách bắt đầu kéo và hiện Home. Thả vào ô trống để đặt; thả ở vùng trống khác để chọn ô trống tự động. Không ghi đè icon có sẵn. Hủy kéo không tạo shortcut.
- Cài đặt → Danh sách ứng dụng có **Nền tối / Kính mờ**, chỉnh **Độ đậm nền** và **Độ làm mờ nền**.
- Kính mờ dùng lớp màu trong suốt, gradient và viền sáng; blur thật dùng API WindowManager khi hệ thống hỗ trợ và đang bật.
- Khi hệ thống không cho blur (ví dụ giới hạn ROM/chế độ tiết kiệm), nền kính trong suốt vẫn hoạt động. Không phải hiệu ứng khúc xạ Liquid Glass riêng của iOS.
- Blur được tắt khi đóng drawer hoặc rời launcher. Các ô danh sách vẫn tái sử dụng như trước.

## Bản 0.6: hai hàng hay dùng và icon pack

- Chế độ Hay dùng: tối đa 8 app có lượt mở được đặt ở 2 hàng đầu; các app khác xếp ABC. Không lặp cùng một entry giữa hai nhóm. Khi chưa có lượt mở, hiển thị toàn bộ theo ABC.
- Có tiêu đề Hay dùng / Ứng dụng khác; cả danh sách cuộn chung. Tìm kiếm quét toàn bộ app và hiển thị kết quả ABC.
- Có thể chọn toàn bộ ABC như trước.
- Hỗ trợ bộ icon đã cài theo chuẩn appfilter (res/xml, res/raw hoặc assets/appfilter.xml), nhận dạng qua intent của ADW/Nova/GO/Lawnchair/Apex và một số launcher phổ biến.
- Áp dụng cho Home, dock, danh sách ứng dụng và các icon nhỏ trong thư mục. App thiếu mapping/resource hợp lệ giữ icon gốc. Gỡ bộ icon cũng trở về icon gốc.

### Cách dùng bộ icon

1. Cài một icon pack tương thích từ Play Store.
2. Giữ vùng trống ở Home hoặc chạm bánh răng trong drawer để mở Cài đặt Pixel Air.
3. Vào **Bộ icon → Chọn bộ icon**, chọn tên gói đã cài, rồi quay về Home.
4. Muốn trở lại mặc định, chọn **Icon gốc**.

Có nút tìm icon pack trên Play Store ngay trong cài đặt. Launcher không tự tải, mua hay chạy mã của bộ icon; chỉ đọc tài nguyên của gói đã cài. Bản này tập trung vào mapping appfilter PNG/vector; không bảo đảm mọi bộ icon trên Play Store, mask/iconback hay đồng hồ động riêng của từng launcher đều tương thích.

## Bản 0.5: vị trí tìm kiếm, cảm giác vuốt và cài đặt riêng

- Thanh Google mặc định được nâng thêm khoảng 16 dp so với 0.4 (khoảng trống dưới thanh từ 8 lên 24 dp).
- Có màn hình **Cài đặt Pixel Air** riêng. Mở bằng giữ vùng trống ở Home hoặc chạm bánh răng ở đầu danh sách app.
- Cài đặt gồm: tên icon, chấm trang, độ nâng thanh Google, cảm giác chuyển trang Êm/Cân bằng/Nhanh, độ nhạy nhận vuốt, ABC/Hay dùng, quản lý trang, widget và launcher mặc định.
- Các thay đổi được lưu và áp dụng khi quay về Home; mở cài đặt rồi quay lại mà không thay đổi không dựng lại workspace.
- Chuyển trang dùng lò xo giảm chấn tới hạn theo vị trí/vận tốc lúc thả. Tính theo thời gian thực, không giả định màn hình 60 Hz. Mức Cân bằng là mặc định.
- Mép đầu/cuối có đàn hồi nhẹ. Khi chạm bắt lại chuyển động, launcher dùng đúng vị trí khung hình đang hiển thị.
- Tôn trọng tùy chọn tắt animation của Android khi hoàn tất chuyển trang.

## Bản 0.4: danh sách app và về Home

- Bỏ mũi tên dưới thanh tìm kiếm ở màn hình chính; đầu danh sách dùng một vạch nhỏ.
- Giữ sẵn drawer và các ô đang hiển thị; danh sách dùng GridView với tái sử dụng ô để cuộn, tìm kiếm và mở lại không dựng toàn bộ app.
- Chạm **ABC** hoặc **Hay dùng** ở đầu danh sách để chọn sắp xếp. Lựa chọn được lưu.
- **Hay dùng** dựa trên số lần mở icon/app từ Pixel Air, lưu cục bộ; app bằng lượt mở được xếp ABC. Không đọc lịch sử dùng app toàn máy và không yêu cầu quyền Usage Access. Ban đầu chưa có lượt mở thì thứ tự giống ABC.
- Nhấn giữ icon màn hình chính/dock để xóa shortcut; nhấn giữ rồi kéo tiếp vẫn có thể di chuyển icon.
- Khi quay về Home, không truy vấn lại toàn bộ ứng dụng và không dựng lại cây giao diện. Chỉ cập nhật danh sách khi app được cài/gỡ/thay đổi. Drawer được đóng trước khi mở app và launcher gửi tọa độ icon nguồn cho Android.
- Giữ đúng trang đã mở app khi từ app về Home. Chỉ nhấn Home lúc đã ở màn hình chính mới chuyển về trang đầu.

Hiệu ứng chuyển cửa sổ app về Home do System UI/REDMAGIC OS quản lý. Bản này xử lý các nguồn khựng và thay đổi trang bên trong launcher; không có quyền hệ thống Quickstep để điều khiển toàn bộ animation như launcher được cài sẵn.

## Bản 0.3: cuộn trang

- Vùng trang rộng toàn bộ màn hình; lề icon nằm bên trong từng trang nên không tạo khe ở mép.
- Dùng một vùng cuộn với OverScroller thay cho hai animation trên hai view rời.
- Chuẩn bị sẵn các trang gần trang hiện tại; không dựng cây giao diện ở lần MOVE bắt đầu vuốt.
- Khoảng kéo bám ngón tay sau ngưỡng nhận cử chỉ của Android.
- Chọn trang và thời gian hoàn tất theo vị trí/vận tốc thả; nội suy chuyển tiếp theo vận tốc.
- Có thể chạm bắt lại trang đang chuyển và kéo ngược, thay vì chặn thao tác đến hết animation.
- Giữ nguyên dữ liệu, dock, thư mục và khả năng kéo icon qua mép trang.

## Bản 0.2 thay đổi

- Ngăn ứng dụng chuyển động theo ngón tay, hiệu ứng thu nhẹ màn hình chính, tự hoàn tất theo vận tốc/khoảng kéo.
- Có thể kéo ngăn ứng dụng xuống để đóng, hoặc kéo ít rồi thả để hủy.
- Nhiều trang riêng, chỉ báo trang, dock chung, chuyển trang có animation.
- Kéo icon qua mép trang; tự thêm trang khi cần.
- Chuyển bố cục 0.1 sang dữ liệu nhiều trang, giữ thư mục và dock.
