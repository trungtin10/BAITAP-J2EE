package com.example.bai5_qlsp.service;

import com.example.bai5_qlsp.model.CustomerOrder;
import com.example.bai5_qlsp.model.OrderDetail;
import com.example.bai5_qlsp.model.Product;
import com.example.bai5_qlsp.repository.CustomerOrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Transactional
    public CustomerOrder checkout(HttpSession session, String username) {
        Map<Long, Integer> cart = cartService.getCart(session);
        if (cart.isEmpty()) {
            throw new IllegalStateException("Giỏ hàng trống");
        }

        CustomerOrder order = new CustomerOrder();
        order.setCreatedAt(LocalDateTime.now());
        order.setUsername(username != null ? username : "guest");

        long total = 0L;
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Long productId = entry.getKey();
            int qty = entry.getValue();
            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại: " + productId));
            long unit = product.getPrice() != null ? product.getPrice() : 0L;
            long line = unit * qty;
            total += line;

            OrderDetail detail = new OrderDetail();
            detail.setCustomerOrder(order);
            detail.setProduct(product);
            detail.setQuantity(qty);
            detail.setUnitPrice(unit);
            detail.setLineTotal(line);
            order.getDetails().add(detail);
        }

        order.setTotalAmount(total);
        cartService.clearCart(session);
        return customerOrderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerOrder> findOrderDetailedById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return customerOrderRepository.findDetailedById(id);
    }
}
