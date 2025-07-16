//package com.fruits.wechatWebsocket.wechatUtils;
//
//import org.apache.commons.codec.binary.Base64;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Arrays;
//
//@Component
//public class wechatCallbackUtils {
//
//    private static String token;
//
//    @Value("${wx.token}")
//    public void setToken(String Token) {
//        token = Token;
//    }
//
//    /**
//     * 验证URL
//     * @param msgSignature 签名串，对应URL参数的msg_signature
//     * @param timeStamp 时间戳，对应URL参数的timestamp
//     * @param nonce 随机串，对应URL参数的nonce
//     * @param echoStr 随机串，对应URL参数的echostr
//     *
//     * @return 解密之后的echostr
//     * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
//     */
//    public String VerifyURL(String msgSignature, String timeStamp, String nonce, String echoStr)
//            throws AesException {
//        String signature = SHA1.getSHA1(token, timeStamp, nonce, echoStr);
//
//        if (!signature.equals(msgSignature)) {
//            throw new AesException(AesException.ValidateSignatureError);
//        }
//
//        String result = decrypt(echoStr);
//        return result;
//    }
//
//
//
//    /**
//     * 对密文进行解密.
//     *
//     * @param text 需要解密的密文
//     * @return 解密得到的明文
//     * @throws AesException aes解密失败
//     */
//    String decrypt(String text) throws AesException {
//        byte[] original;
//        try {
//            // 设置解密模式为AES的CBC模式
//            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
//            SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
//            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
//            cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);
//
//            // 使用BASE64对密文进行解码
//            byte[] encrypted = Base64.decodeBase64(text);
//
//            // 解密
//            original = cipher.doFinal(encrypted);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new AesException(AesException.DecryptAESError);
//        }
//
//        String jsonContent, from_receiveid;
//        try {
//            // 去除补位字符
//            byte[] bytes = PKCS7Encoder.decode(original);
//
//            // 分离16位随机字符串,网络字节序和receiveid
//            byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);
//
//            int jsonLength = recoverNetworkBytesOrder(networkOrder);
//
//            jsonContent = new String(Arrays.copyOfRange(bytes, 20, 20 + jsonLength), CHARSET);
//            from_receiveid = new String(Arrays.copyOfRange(bytes, 20 + jsonLength, bytes.length),
//                    CHARSET);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new AesException(AesException.IllegalBuffer);
//        }
//
//        // receiveid不相同的情况
//        if (!from_receiveid.equals(receiveid)) {
//            throw new AesException(AesException.ValidateCorpidError);
//        }
//        return jsonContent;
//
//    }
//
//
//}
