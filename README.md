# Đề tài: Video Stream
Ứng dụng phát và xem video sử dụng TCP và UDP
### Mô tả chức năng
#### Server
Có sử dụng phần mềm VLC và một số thư viện Java khác.

Server có các chức năng chính đó là:
- Chạy video từ trong máy tính (local)
- Chạy webcam
- Chạy video từ nguồn trên mạng (internet)
- Gửi video dưới dạng các ảnh tới các Client bằng giao thức UDP
- Nhận và gửi tin nhắn bằng giao thức TCP
- Có hỗ trợ multi Client

#### Client
Client có các chức năng chính đó là:
- Nhận các ảnh từ server rồi chạy thành video
- Nhận và gửi tin nhắn
- Chỉnh độ phân giải cho video

### Ý tưởng làm độ phân giải video
Ý tưởng được thực hiện dựa trên:
- Khi chụp ảnh 1 vật ở xa và zoom ảnh lên thì vật đấy sẽ rất mờ, ngược lại nếu chụp gần thì vật đấy sẽ rất nét.
- Lý thuyết của 1080p sang 144p bản chất là giảm số pixel của ảnh 1080p xuống còn 144p => gần giống với thu nhỏ bức ảnh lại bằng cách sửa dimension.
- Sau khi thu nhỏ dimension của ảnh thì sẽ được ảnh mới với dung lượng ít hơn.
#### Luồng chạy
Client: Phía bên Client có chọn các mức độ 1080p -> 144p, Client chọn độ phân giải và gửi lên cho server 1 đoạn text phân biệt 1080p -> 144p.

Server: Server nhận được độ phân giải yêu cầu từ Client rồi thực hiện resize image (nghĩa là thay đổi kích thước của ảnh tương ứng với độ phân giải client gửi tới). Sau khi resize xong thì Server sẽ gửi trả lại các ảnh cho Client, ảnh càng nhỏ thì càng ít dung lượng, ảnh càng to thì tốn nhiều dung lượng gửi đi của Server. 

Client: Ảnh sau khi resize được nhận từ server sẽ được fit vừa với màn hình trình chiếu của Client, từ đó các ảnh nhỏ -> ảnh mờ, ảnh to -> ảnh nét.

=> Từ ý tưởng trên đã phần nào thỏa mãn được cơ chế thay đổi độ phân giải của video
- Độ phân giải thấp thì truyền đi ít dung lượng hơn độ phân giải cao.
- Độ phân giải thấp sẽ có chất lượng hình ảnh kém hơn độ phân giải cao.
