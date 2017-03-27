package com.zhangry.demo.common.util;

import com.google.common.base.Strings;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by zhangry on 2017/3/17.
 */
public class StringUtil {
    public static final String EMPTY = "";
    public static final String LF = "\n";
    public static final String CR = "\r";

    private StringUtil() {
    }

    public static boolean isNullOrEmpty(String s) {
        boolean result = Strings.isNullOrEmpty(s);
        return result?result:s.equals("null") || s.equals("undefined");
    }

    public static String commonPrefix(CharSequence a, CharSequence b) {
        return Strings.commonPrefix(a, b);
    }

    public static String commonSuffix(CharSequence a, CharSequence b) {
        return Strings.commonSuffix(a, b);
    }

    public static String emptyToNull(String s) {
        return Strings.emptyToNull(s);
    }

    public static String nullToEmpty(String s) {
        return Strings.nullToEmpty(s);
    }

    public static String leftPad(String s, int minLength, char padChar) {
        return Strings.padStart(s, minLength, padChar);
    }

    public static String rightPad(String s, int minLength, char padChar) {
        return Strings.padEnd(s, minLength, padChar);
    }

    public static String join(Object[] array, char separator) {
        return StringUtils.join(array, separator);
    }

    public static String capitalize(String name) {
        return StringUtils.capitalize(name);
    }

    public static String getMethodName(String name) {
        return "get" + StringUtils.capitalize(name);
    }

    public static String setMethodName(String name) {
        return "set" + StringUtils.capitalize(name);
    }

    public static String toSqlIn(String s, String split) {
        if(!isNullOrEmpty(s) && !isNullOrEmpty(split)) {
            String[] splits = s.split(split);
            String result = "";
            String[] var4 = splits;
            int var5 = splits.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String item = var4[var6];
                result = result + "'" + item.trim() + "',";
            }

            if(!isNullOrEmpty(result)) {
                result = result.substring(0, result.length() - 1);
            }

            return result;
        } else {
            return s;
        }
    }

    public static String toSqlIn(Object[] objArray) {
        AssertUtil.notEmpty(objArray);
        String s = join(objArray, ',');
        return toSqlIn(s, ",");
    }

    public static String toSqlIn(List<Object> objectList) {
        AssertUtil.notEmpty(objectList);
        return toSqlIn(objectList.toArray());
    }

    private static String slice(String str, int start) {
        if(isNullOrEmpty(str)) {
            return "";
        } else {
            int length;
            for(length = str.length(); start < 0; start += length) {
                ;
            }

            return start >= length?"":str.substring(start);
        }
    }

    private static String slice(String str, int start, int end) {
        if(isNullOrEmpty(str)) {
            return "";
        } else {
            int length;
            for(length = str.length(); start < 0; start += length) {
                ;
            }

            if(start >= length) {
                return "";
            } else {
                while(end < 0) {
                    end += length;
                }

                if(end > length) {
                    end = length - 1;
                }

                return end < start?"":str.substring(start, end);
            }
        }
    }

    public static String format(String format, String... args) {
        if(!isNullOrEmpty(format) && args != null && args.length != 0) {
            StringBuilder builder = new StringBuilder();
            int i = 0;

            while(true) {
                while(true) {
                    int open = format.indexOf(123, i);
                    int close = format.indexOf(125, i);
                    if(open < 0 && close < 0) {
                        builder.append(slice(format, i));
                        return builder.toString();
                    }

                    if(close > 0 && (close < open || open < 0)) {
                        if(format.charAt(close + 1) != 125) {
                            throw new RuntimeException("如果想加入{}字符需成双出现，如{{ 或者 }}!");
                        }

                        builder.append(slice(format, i, close + 1));
                        i = close + 2;
                    } else {
                        builder.append(slice(format, i, open));
                        i = open + 1;
                        if(format.charAt(i) == 123) {
                            builder.append('{');
                            ++i;
                        } else {
                            if(close < 0) {
                                throw new RuntimeException("格式化字符串缺少闭合标记，不符合规则!");
                            }

                            String brace = format.substring(i, close);
                            boolean var7 = false;

                            int index;
                            try {
                                index = Integer.parseInt(brace, 10);
                            } catch (NumberFormatException var9) {
                                throw new RuntimeException("占位符必须为数字!");
                            }

                            String arg = args[index];
                            if(arg == null) {
                                arg = "";
                            }

                            builder.append(arg);
                            i = close + 1;
                        }
                    }
                }
            }
        } else {
            return format;
        }
    }

    public static int getStringByteLength(String s, String encoding) {
        if(isNullOrEmpty(s)) {
            return 0;
        } else {
            int len = 0;
            boolean isEncodingExists = !isNullOrEmpty(encoding);

            for(int i = 0; i < s.length(); ++i) {
                try {
                    byte[] b = null;
                    byte[] b;
                    if(isEncodingExists) {
                        b = s.substring(i, i + 1).getBytes(encoding);
                    } else {
                        b = s.substring(i, i + 1).getBytes();
                    }

                    len += b.length;
                } catch (UnsupportedEncodingException var6) {
                    ;
                }
            }

            return len;
        }
    }

    public static String unicode2Chinese(String value) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

        char ch;
        for(Matcher matcher = pattern.matcher(value); matcher.find(); value = value.replace(matcher.group(1), ch + "")) {
            ch = (char)Integer.parseInt(matcher.group(2), 16);
        }

        return value;
    }

    public static String chinese2Unicode(String value) {
        String strRet = "";

        for(int i = 0; i < value.length(); ++i) {
            strRet = strRet + "\\u" + Integer.toHexString(value.charAt(i));
        }

        return strRet;
    }

    public static String FullToHalf(String s) {
        if(s == null) {
            return s;
        } else if(s.length() == 0) {
            return s;
        } else {
            int iLen = s.length();
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < iLen; ++i) {
                char c = s.charAt(i);
                if(c >= '！' && '～' > c) {
                    c -= 'ﻠ';
                } else if(c == '￥') {
                    c -= 'ﾉ';
                } else if(c == 8217) {
                    c = (char)(c - 8178);
                } else if(c == 8221) {
                    c = (char)(c - 8187);
                } else if(c == 12316) {
                    c = (char)(c - 12190);
                } else if(c == 8722) {
                    c = (char)(c - 8677);
                } else if(c == 8216) {
                    c = (char)(c - 8120);
                } else if(c == 12290) {
                    c = (char)(c - 12244);
                } else if(c == 12289) {
                    c = (char)(c - 12245);
                } else if(c == 12288) {
                    c = (char)(c - 12256);
                }

                sb.append(c);
            }

            return new String(sb);
        }
    }

    public static String HalfToFull(String s) {
        if(s == null) {
            return s;
        } else if(s.length() == 0) {
            return s;
        } else {
            int iLen = s.length();
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < iLen; ++i) {
                char c = s.charAt(i);
                if(c >= 33 && 126 >= c) {
                    c += 'ﻠ';
                } else if(c == 92) {
                    c += 'ﾉ';
                } else if(c == 39) {
                    c = (char)(c + 8178);
                } else if(c == 34) {
                    c = (char)(c + 8187);
                } else if(c == 126) {
                    c = (char)(c + 12190);
                } else if(c == 45) {
                    c = (char)(c + 8677);
                } else if(c == 96) {
                    c = (char)(c + 8120);
                } else if(c == 46) {
                    c = (char)(c + 12244);
                } else if(c == 44) {
                    c = (char)(c + 12245);
                } else if(c == 32) {
                    c = (char)(c + 12256);
                }

                sb.append(c);
            }

            return new String(sb);
        }
    }

    public static String encodeSqlLike(String strIn, String strEscape) {
        strIn = replace(strIn, strEscape, strEscape + strEscape);
        strIn = replace(strIn, "\\", "\\\\");
        strIn = replace(strIn, "%", strEscape + "%");
        strIn = replace(strIn, "_", strEscape + "_");
        strIn = replace(strIn, "％", strEscape + "％");
        strIn = replace(strIn, "＿", strEscape + "＿");
        strIn = encodeSingleQuote(strIn);
        return strIn;
    }

    public static String replace(String strIn, String strRe, String strBy) {
        String strTemp = "";

        for(int iPos = strIn.indexOf(strRe); iPos != -1; iPos = strIn.indexOf(strRe)) {
            strTemp = strTemp + strIn.substring(0, iPos) + strBy;
            strIn = strIn.substring(iPos + strRe.length());
        }

        strIn = strTemp + strIn;
        return strIn;
    }

    public static String encodeSingleQuote(String strIn) {
        if(strIn == null) {
            return strIn;
        } else {
            strIn = replace(strIn, "'", "''");
            return strIn;
        }
    }

    public static String removeSelect(String sql) {
        int beginPos = sql.toLowerCase().indexOf("from");
        return sql.substring(beginPos);
    }

    public static String removeOrders(String sql) {
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", 2);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();

        while(m.find()) {
            m.appendReplacement(sb, "");
        }

        m.appendTail(sb);
        return sb.toString();
    }
}

