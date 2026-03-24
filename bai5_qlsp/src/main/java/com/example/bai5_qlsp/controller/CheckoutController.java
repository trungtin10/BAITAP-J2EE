package com.example.bai5_qlsp.controller;

import com.example.bai5_qlsp.model.CustomerOrder;
import com.example.bai5_qlsp.service.CartPresentationService;
import com.example.bai5_qlsp.service.CartService;
import com.example.bai5_qlsp.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartPresentationService cartPresentationService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String reviewOrder(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (cartService.isEmpty(session)) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống. Hãy thêm sản phẩm trước khi đặt hàng.");
            return "redirect:/cart";
        }
        CartPresentationService.CartSummary summary = cartPresentationService.summarize(session);
        model.addAttribute("cartLines", summary.lines());
        model.addAttribute("grandTotal", summary.grandTotal());
        return "checkout/review";
    }

    @PostMapping("/confirm")
    public String confirmOrder(HttpSession session, Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        if (cartService.isEmpty(session)) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống, không thể đặt hàng.");
            return "redirect:/cart";
        }
        try {
            String username = authentication != null ? authentication.getName() : null;
            CustomerOrder order = orderService.checkout(session, username);
            return "redirect:/checkout/complete/" + order.getId();
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/complete/{orderId}")
    public String orderComplete(@PathVariable("orderId") Long orderId, Model model,
                                RedirectAttributes redirectAttributes) {
        return orderService.findOrderDetailedById(orderId)
                .map(order -> {
                    model.addAttribute("order", order);
                    return "checkout/complete";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng.");
                    return "redirect:/cart";
                });
    }
}
