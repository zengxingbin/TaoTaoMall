package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;

/**
 * 购物车处理Controller
 * 
 * @author Administrator
 *
 */
@Controller
public class CartController {
    @Value("${CART_KEY}")
    private String CART_KEY;
    @Value("${EXPIRE_TIME}")
    private int EXPIRE_TIME;
    @Autowired
    private ItemService itemService;

    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
            HttpServletRequest request,HttpServletResponse response) {
        //从cookie取出购物车列表
        List<TbItem> cartItemList = getCartItemList(request);
        //根据商品id从购物车中查询商品是否存在
        boolean flag = false;//购物车中存在此商品
        for(TbItem tbItem : cartItemList) {
            if(tbItem.getId() == itemId.longValue()) {
                flag = true;
                //列表中已经存在该商品，将商品数量相加
                tbItem.setNum(tbItem.getNum() + num);
                break;
            }
            
        }
        if(flag)
            return "cartSuccess";
        //若购物车不存在该商品，则需要从数据库中查出此商品，然后放到购物车中
        TbItem tbItem = itemService.getItemById(itemId);
        //设置购物车商品数量
        tbItem.setNum(num);
        //设置显示一张图片
        String image = tbItem.getImage();
        if(StringUtils.isNotBlank(image)) {
            tbItem.setImage(image.split(",")[0]);
        }
        //添加到购物车
        cartItemList.add(tbItem);
        //将购物车列表放进Cookie中
        CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList),EXPIRE_TIME, true);
        return "cartSuccess";
    }
    @RequestMapping("/cart/cart")
    public String  toCart(HttpServletRequest request) {
        //从cookie中取出购物车商品列表
        List<TbItem> cartItemList = getCartItemList(request);
        //放入request作用域中
        request.setAttribute("cartList",cartItemList);
        return "cart";
    }
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateCart(@PathVariable Long itemId,@PathVariable Integer num,
            HttpServletRequest request,HttpServletResponse response) {
        //从cookie中取出购物车中的商品列表
        List<TbItem> cartItemList = getCartItemList(request);
        //遍历购物车列表根据商品Id，修改商品数量
        for(TbItem tbItem : cartItemList) {
            if(tbItem.getId() == itemId.longValue()) {
                tbItem.setNum(num); 
                break;
            }
        }
        //将购物车列表重新写进cookie
        CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), EXPIRE_TIME,true);
        return TaotaoResult.ok();
    }
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteItem(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response) {
        //从cookie中获取商品列表
        List<TbItem> cartItemList = getCartItemList(request);
        //遍历商品列表，根据Id删除对应的商品
        for(TbItem tbItem : cartItemList) {
            if(tbItem.getId() == itemId.longValue()) {
                cartItemList.remove(tbItem);
                break;
            }
        }
        //将购物车商品列表放回cookie
        CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), EXPIRE_TIME, true);
        //重定向到购物车页面
        return "redirect:/cart/cart.html";
    }
    private List<TbItem> getCartItemList(HttpServletRequest request) {
        String json = CookieUtils.getCookieValue(request, CART_KEY, true);
        // 判断json是否为空
        if (StringUtils.isBlank(json)) {
            // 如果为空，放回一个空的列表
            return new ArrayList<>();
        }
        // 否则
        // 将json转换成list
        List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
        return list;
    }
}
