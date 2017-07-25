package com.taotao.sso.test;

import org.springframework.util.DigestUtils;

public class GenerateMD5Password {
    public static void main(String[] args) {
        System.out.println(DigestUtils.md5DigestAsHex("123".getBytes()));
    }
}
