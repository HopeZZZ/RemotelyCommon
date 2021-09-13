package com.zhongke.common.utils;



import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class ZKRSAUtils {

    /**
     * 公钥加密
     * @param src 原内容
     * @return 返回加密后的内容
     */
    public static String encryptByPublicKey(String src) {
        try {
            String publicKey = ZKSpUtil.getInstance().getStringValue("publicKey");//公钥加密
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(ZKBase64Util.decode(publicKey));
            KeyFactory keyFactoryJ = KeyFactory.getInstance("RSA");
            PublicKey publicK = keyFactoryJ.generatePublic(x509EncodedKeySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
            byte[] result = cipher.doFinal(src.getBytes());
            return ZKBase64Util.encode(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
