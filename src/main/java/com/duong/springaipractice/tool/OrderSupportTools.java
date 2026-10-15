package com.duong.springaipractice.tool;

import com.duong.springaipractice.dto.response.OrderResponse;
import com.duong.springaipractice.dto.response.UserResponse;
import com.duong.springaipractice.entity.Order;
import com.duong.springaipractice.entity.User;
import com.duong.springaipractice.repository.OrderRepository;
import com.duong.springaipractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderSupportTools {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Tool(description = "Tra cứu thông tin cá nhân của khách hàng dựa trên địa chỉ email")
    public UserResponse getCustomerProfile(
            @ToolParam(description = "Email của khách hàng cần tra cứu") String email
    ){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    @Tool(description = "Lấy danh sách tất cả đơn hàng mà khách hàng đã đặt dựa trên email của họ")
    public List<OrderResponse> getCustomerOrders(
            @ToolParam(description = "Email của khách hàng sở hữu các đơn hàng") String email
    ){
        List<Order> ordersByEmail = orderRepository.findByUserEmail(email);
        return ordersByEmail.stream()
                .map(order -> OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .productName(order.getProductName())
                .amount(order.getAmount())
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .createdAt(order.getCreatedAt())
                .build()).toList();
    }

    @Tool(description = "Thực hiện hủy một đơn hàng cụ thể theo yêu cầu của khách hàng dựa trên mã đơn hàng (orderCode) và lý do hủy")
    public String cancelOrder(
            @ToolParam(description = "Mã đơn hàng cần hủy, ví dụ: DH-100 (đây là thuộc tính orderCode)") String orderCode,
            @ToolParam(description = "Lý do hủy đơn hàng do khách hàng cung cấp") String reason
    ){
        // 1. Tìm đơn hàng
        Optional<Order> orderOptional = orderRepository.findByOrderCode(orderCode);
        if(orderOptional.isEmpty()){
            return "Lỗi: Không tìm thấy đơn hàng nào có mã " + orderCode;
        }
        Order order = orderOptional.get();

        // 2. Kiểm tra nghiệp vụ: Chỉ hủy đơn Pending
        if(!"PENDING".equalsIgnoreCase(order.getStatus())){
            return "Từ chối hủy: Đơn hàng " + orderCode + " đang ở trạng thái '" + order.getStatus() + "' nên không thể hủy.";
        }

        // 3. Cập nhật database
        order.setStatus("CANCELLED");
        orderRepository.save(order);

        return "Thành công: Đơn hàng " + orderCode + " đã được hủy thành công trên hệ thống. Lý do: " + reason;
    }
}
