package com.example.bai5_qlsp.service;

import com.example.bai5_qlsp.dto.CartLineDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class CartPresentationService {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    public CartSummary summarize(HttpSession session) {
        Map<Long, Integer> cart = cartService.getCart(session);
        List<CartLineDto> lines = new ArrayList<>();
        long grandTotal = 0L;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Long productId = entry.getKey();
            int qty = entry.getValue();
            productService.getProductById(productId).ifPresent(product -> {
                long price = product.getPrice() != null ? product.getPrice() : 0L;
                long sub = price * qty;
                lines.add(new CartLineDto(product.getId(), product.getName(), price, qty, sub));
            });
        }

        lines.sort(Comparator.comparing(CartLineDto::getProductId));
        for (CartLineDto line : lines) {
            grandTotal += line.getSubtotal() != null ? line.getSubtotal() : 0L;
        }

        return new CartSummary(lines, grandTotal, lines.isEmpty());
    }

    public record CartSummary(List<CartLineDto> lines, long grandTotal, boolean empty) {
    }
}
