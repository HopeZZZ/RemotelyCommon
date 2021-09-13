package com.zhongke.common.utils;

import android.text.TextUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * author : wpt
 * date   : 2021/7/2716:40
 * desc   :
 */
public class ZKEncryptUtils {

    /**
     * 根据所有参数生成sign
     * 签名流程（移动端）
     * 1.添加随机字符串，参数:nonceStr 长度32位，请求方自行生成，包含字符0-9a-zA-Z
     * 2.添加当前时间戳，参数：timestamp，10位的秒级时间戳，请求方自行生成，如 1629770135 （表示2021-08-24 09:55:35）
     * 3.所有参数按照ASCII码值进行升序排序（字典序）
     * 4.拼接为url query的形式，例如有参数a，值为100，参数b值为vb，参数x，值为vx，那么拼接后的字符串为,
     * 此处假设nonceStr=3jstmvuaj1tq2fsbjlsn5s9ewyt8r7va
     * timestamp=1629770135
     * 则拼接的字符串为
     * a=100&b=vb&nonceStr=3jstmvuaj1tq2fsbjlsn5s9ewyt8r7va×tamp=1629770135&x=vx
     * 5.在以上字符串的基础上，拼接参数token，长度为32位，如果token值没有，就token就=’’ （空字符串）
     * 假设token=sf5bcke9n2lkxd3otfgqro4byyovpxnr
     * 则拼接的字符串为
     * a=100&b=vb&nonceStr=3jstmvuaj1tq2fsbjlsn5s9ewyt8r7va×tamp=1629770135&x=vx&token=sf5bcke9n2lkxd3otfgqro4byyovpxnr
     * 假设token为空，或接口不需要token值，则token为空字符串，拼接的字符串为
     * a=100&b=vb&nonceStr=3jstmvuaj1tq2fsbjlsn5s9ewyt8r7va×tamp=1629770135&x=vx&token=
     * 6.对拼接完成的字符串，进行SHA1 摘要，得出sign
     * 7.使用公钥对sign进行rsa加密，对结果进行base64编码，得到sign
     * @param rootMap :请求里的所有参数，也包含了1、2中的nonceStr和timestamp
     */
    public static String createSign(SortedMap<Object, Object> rootMap,String token) {
        if (rootMap == null){
            rootMap = new TreeMap<>();
        }
        StringBuilder sb = new StringBuilder();
        Set es = rootMap.entrySet();//3.所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k) && !"file".equals(k)) {
                sb.append(k).append("=").append(v).append("&"); //4.拼接为url query的形式
            }
        }
        sb.append("token=").append(token);//5.最后加上token
        String sign = getSha1(sb.toString());//6.进行SHA1加密
        try {
            sign = ZKRSAUtils.encryptByPublicKey(sign);//7.公钥加密
        } catch (Exception e) {
            e.getMessage();
        }
        return sign;
    }

    /**
     * SHA1实现
     * @param content 加密前内容
     * @return 返回SHA1 加密后的内容
     */
    public static String getSha1(String content){
        if (TextUtils.isEmpty(content)){
            return null;
        }
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray;
        byteArray = content.getBytes(StandardCharsets.UTF_8);
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

}
