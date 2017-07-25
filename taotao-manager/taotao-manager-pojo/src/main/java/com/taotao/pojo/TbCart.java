package com.taotao.pojo;

public class TbCart {
    private Long id;

    private String cart;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart == null ? null : cart.trim();
    }
}