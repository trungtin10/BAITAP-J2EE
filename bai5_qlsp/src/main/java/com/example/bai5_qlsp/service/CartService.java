package com.example.bai5_qlsp.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CartService {

    public static final String SESSION_CART = "SHOPPING_CART";

    @SuppressWarnings("unchecked")
    public Map<Long, Integer> getCart(HttpSession session) {
        Object attr = session.getAttribute(SESSION_CART);
        if (attr == null) {
            Map<Long, Integer> cart = new LinkedHashMap<>();
            session.setAttribute(SESSION_CART, cart);
            return cart;
        }
        return (Map<Long, Integer>) attr;
    }

    public void addToCart(HttpSession session, Long productId, int quantity) {
        if (productId == null || quantity <= 0) {
            return;
        }
        Map<Long, Integer> cart = getCart(session);
        cart.merge(productId, quantity, Integer::sum);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(SESSION_CART);
    }

    public boolean isEmpty(HttpSession session) {
        return getCart(session).isEmpty();
    }

    public void setLineQuantity(HttpSession session, Long productId, int quantity) {
        if (productId == null) {
            return;
        }
        Map<Long, Integer> cart = getCart(session);
        if (quantity <= 0) {
            cart.remove(productId);
        } else {
            cart.put(productId, quantity);
        }
    }

    public void removeLine(HttpSession session, Long productId) {
        if (productId == null) {
            return;
        }
        getCart(session).remove(productId);
    }
}
