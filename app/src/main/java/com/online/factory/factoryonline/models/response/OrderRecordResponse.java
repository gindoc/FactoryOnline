package com.online.factory.factoryonline.models.response;

import com.online.factory.factoryonline.models.Order;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2017/2/17 11:17
 * 作用:
 */
public class OrderRecordResponse extends Response{
    private List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
