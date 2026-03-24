package com.example.bai5_qlsp.controller;

import com.example.bai5_qlsp.model.Product;
import com.example.bai5_qlsp.service.CartPresentationService;
import com.example.bai5_qlsp.service.CartService;
import com.example.bai5_qlsp.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartPresentationService cartPresentationService;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        CartPresentationService.CartSummary summary = cartPresentationService.summarize(session);
        model.addAttribute("cartLines", summary.lines());
        model.addAttribute("grandTotal", summary.grandTotal());
        model.addAttribute("emptyCart", summary.empty());
        return "cart/view";
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestParam("productId") Long productId,
            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
            HttpSession session,
            @RequestParam(value = "redirect", required = false) String redirect,
            RedirectAttributes redirectAttributes) {

        Product product = productService.getProductById(productId).orElse(null);
        if (product == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm.");
            return "redirect:/products";
        }
        cartService.addToCart(session, productId, quantity);
        redirectAttributes.addFlashAttribute("message", "Đã thêm vào giỏ hàng.");

        if (redirect != null && !redirect.isBlank()) {
            return "redirect:" + redirect;
        }
        return "redirect:/products";
    }

    @PostMapping("/update")
    public String updateLine(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") int quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        cartService.setLineQuantity(session, productId, quantity);
        redirectAttributes.addFlashAttribute("message", "Đã cập nhật giỏ hàng.");
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeLine(
            @RequestParam("productId") Long productId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        cartService.removeLine(session, productId);
        redirectAttributes.addFlashAttribute("message", "Đã xóa sản phẩm khỏi giỏ.");
        return "redirect:/cart";
    }
}
