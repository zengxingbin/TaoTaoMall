package com.taotao.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;

/**
 * 订单处理Controller
 * @author Administrator
 *
 */
@Controller
public class OrderCartController {
    @Value("${CART_KEY}")
    private String CART_KEY;
    //注入sevice
    @Autowired
    private OrderService orderService;
    @RequestMapping("/order/order-cart")
    public String showOrderPage(HttpServletRequest request) {
        //用户必须是登录状态
        //取用户id
        TbUser user = (TbUser)request.getAttribute("user");
        System.out.println(user);
        //根据用户信息查询地址列表
        //将用户地址列表显示到订单页面
        //从cookie中取出购物车商品列表，将购物车商品列表显示到订单页面
        List<TbItem> cartItemList = getCartItemList(request);
       // System.out.println(cartItemList);
        //将购物车商品列表放到request作用域中
        request.setAttribute("cartList",cartItemList);
        return "order-cart";
    }
    @RequestMapping("/order/create")
    public String createOrder(OrderInfo orderInfo,Model model) {
        TaotaoResult result = orderService.createOrder(orderInfo);
        //回显订单号
        model.addAttribute("orderId", result.getData().toString());
        //回显价格
        model.addAttribute("payment", orderInfo.getPayment());
        //回显货物到达时间
        DateTime dateTime = new DateTime();//当前时间
        dateTime.plus(3);//3天后到达
        model.addAttribute("date", dateTime.toString("yyyy-MM-dd"));
        return "success";
    }
    private List<TbItem> getCartItemList(HttpServletRequest request) {
        String json = CookieUtils.getCookieValue(request, CART_KEY, true);
        // 判断json是否为空
        if (StringUtils.isBlank(json)) {
            // 如果为空，放回一个空的列表i
            return new ArrayList<>();
        }
        // 否则
        // 将json转换成list
        List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
        return list;
    }
}
