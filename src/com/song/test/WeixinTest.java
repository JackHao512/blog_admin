package com.song.test;

import com.song.access.AccessToken;
import com.song.util.WeixinUtil;

public class WeixinTest {
	public static void main(String[] args) {
		AccessToken token = WeixinUtil.getAccessToken();
		System.out.println("票据："+token.getToken());
		System.err.println("失效时间："+token.getExpiresIn());
		
		
		
	}
}
