package com.taotao.sso.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;

/**
 * 用户处理Controller
 * @author Administrator
 *
 */
@Controller
public class UserController {
    //注入服务
    @Autowired
    private UserService userService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;
    @Value("${CART_KEY}")
    private String CART_KEY;
    @Value("${ID_LIST}")
    private String ID_LIST;
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public TaotaoResult checkUserData(@PathVariable String param,@PathVariable Integer type) {
        TaotaoResult result = userService.checkData(param, type);
        return result;
    }
    @RequestMapping(value="/user/register",method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult UserRegister(TbUser user) {
        TaotaoResult result = userService.register(user);
        return result;
    }
    @RequestMapping(value="/user/login",method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult userLogin(String username,String password,HttpServletRequest request,
            HttpServletResponse response) {
        TaotaoResult result = userService.login(username, password);
      //将token保存到cookie中
        if(result.getStatus() == 200) {
          //将token保存到cookie中
            CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
        }
        return result;
       /* //将token保存到cookie中
        if(result.getStatus() == 200) {
          //将token保存到cookie中
            CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
            //将Cookie中的购物车列表同步到redis中
            List<TbItem> cartItemList = getCartItemList(request);
            //创建一张列表保存购物车中所有的商品Id
            List<String> itemIdList = new ArrayList<>();
            //遍历商品列表
            for(TbItem tbItem : cartItemList) {
                jedisClient.hset(CART_KEY, tbItem.getId().toString(), JsonUtils.objectToJson(tbItem));
                itemIdList.add(tbItem.getId().toString());
            }
            //将商品Id列表放到redis中
            jedisClient.set(ID_LIST, JsonUtils.objectToJson(itemIdList));
        }*/
        
    }
    @RequestMapping(value="/user/token/{token}",method=RequestMethod.GET,
            //设置相应的数据类型
            produces=MediaType.APPLICATION_JSON_UTF8_VALUE
            )
    @ResponseBody
    public String getUserByToken(@PathVariable String token,String callback) {
        TaotaoResult result = userService.getUserByTokenn(token);
        //判断是否是js跨域请求
        if(StringUtils.isNotBlank(callback)) {//是
            return callback + "(" + JsonUtils.objectToJson(result) + ");";//返回的是一条js语句
        }
        return JsonUtils.objectToJson(result);
    }
  //jsonp的第二种方法，spring4.1以上版本使用
    /*@RequestMapping(value="/user/token/{token}",method=RequestMethod.GET,
            //设置相应的数据类型
            produces=MediaType.APPLICATION_JSON_UTF8_VALUE
            )
    @ResponseBody
    public Object getUserByToken(@PathVariable String token,String callback) {
        TaotaoResult result = userService.getUserByTokenn(token);
        //判断是否是js跨域请求
        if(StringUtils.isNotBlank(callback)) {//是
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
          //设置回调方法
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }*/
    @RequestMapping(value="/user/logout/{token}",method=RequestMethod.GET)
    @ResponseBody
    public TaotaoResult logout(@PathVariable String token) {
        TaotaoResult result = userService.logout(token);
        /*//用户退出登录时将redis中保存的购物车信息保存到数据库中
        if(result.getStatus() == 200) {
            //从redis中取出购物车中的商品Id列表
            String json = jedisClient.get(ID_LIST);
            List<String> idList = JsonUtils.jsonToList(json, String.class);
            //遍历idList从redis中取出购物车商品保存到数据库中
            for(String id : idList) {
                String tbItem = jedisClient.hget(CART_KEY, id);
                TaotaoResult result2 = userService.getUserByTokenn(token);
                TbUser user  = (TbUser) result2.getData();
                //创建TbCart
                TbCart tbCart = new TbCart();
                tbCart.setId(user.getId());
                tbCart.setCart(tbItem);
                
            }
        }*/
        return result;
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
