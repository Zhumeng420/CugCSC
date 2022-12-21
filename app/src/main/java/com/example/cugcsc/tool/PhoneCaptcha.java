package com.example.cugcsc.tool;

public class PhoneCaptcha {
    public static boolean SendPhoneCaptcha(String phoneNumber,String code)  {
        String text = code;
        System.out.println("手机验证码内容" + text);
        String base = "https://cspe.api.storeapi.net/pyi/86/204?format=json&appid=18774&template_id=10200&sign=c9b25cc3b1d3d8e0a4ab9d6d706f6247";
        String pathUrl = base + "&mobile=" + phoneNumber + "&template_param=%7B%22code%22%3A" + '"' + text + '"' + "%7D";
        String back=HttpUtils.getJSON(pathUrl);
        System.out.println(back);
        return true;
    }
}
