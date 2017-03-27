package com.zhangry.demo.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by zhangry on 2017/3/17.
 */
public class EncodeUtil {
    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    private EncodeUtil() {
    }

    public static String encodeHex(byte[] input) {
        return Hex.encodeHexString(input);
    }

    public static byte[] decodeHex(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException var2) {
            throw ExceptionUtil.unchecked(var2);
        }
    }

    public static String encodeBase64(byte[] input) {
        return Base64.encodeBase64String(input);
    }

    public static String encodeUrlSafeBase64(byte[] input) {
        return Base64.encodeBase64URLSafeString(input);
    }

    public static byte[] decodeBase64(String input) {
        return Base64.decodeBase64(input);
    }

    public static String encodeBase62(byte[] input) {
        char[] chars = new char[input.length];

        for(int i = 0; i < input.length; ++i) {
            chars[i] = BASE62[(input[i] & 255) % BASE62.length];
        }

        return new String(chars);
    }

    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }

    public static String unescapeHtml(String htmlEscaped) {
        return StringEscapeUtils.unescapeHtml4(htmlEscaped);
    }

    public static String escapeXml(String xml) {
        return StringEscapeUtils.escapeXml(xml);
    }

    public static String unescapeXml(String xmlEscaped) {
        return StringEscapeUtils.unescapeXml(xmlEscaped);
    }

    public static String urlEncode(String part) {
        try {
            return URLEncoder.encode(part, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            throw ExceptionUtil.unchecked(var2);
        }
    }

    public static String urlDecode(String part) {
        try {
            return URLDecoder.decode(part, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            throw ExceptionUtil.unchecked(var2);
        }
    }

    public static String md5(String text) {
        return md5(text, 32);
    }

    public static String md5(String text, int encodeType) {
        if(StringUtils.isBlank(text)) {
            return null;
        } else {
            String result = null;

            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(text.getBytes());
                byte[] b = md.digest();
                StringBuffer buf = new StringBuffer("");

                for(int offset = 0; offset < b.length; ++offset) {
                    int i = b[offset];
                    if(i < 0) {
                        i += 256;
                    }

                    if(i < 16) {
                        buf.append("0");
                    }

                    buf.append(Integer.toHexString(i));
                }

                switch(encodeType) {
                    case 16:
                        result = buf.toString().substring(10, 26);
                        break;
                    case 32:
                        result = buf.toString();
                        break;
                    default:
                        result = buf.toString().substring(10, 26);
                }

                return result;
            } catch (NoSuchAlgorithmException var8) {
                throw ExceptionUtil.unchecked(var8);
            }
        }
    }
}

