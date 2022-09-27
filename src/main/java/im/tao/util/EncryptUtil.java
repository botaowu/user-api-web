package im.tao.util;

import java.util.Base64;

public class EncryptUtil {
    private static final byte[] enkeystore = {
            0x03, 0x07, 0x0f, 0x00,
            0x0a, 0x01, 0x0b, 0x02,
            0x0e, 0x0c, 0x05, 0x08,
            0x0d, 0x06, 0x09, 0x04
    };
    private static final byte[] dekeystore = {
            0x03, 0x05, 0x07, 0x00,
            0x0f, 0x0a, 0x0d, 0x01,
            0x0b, 0x0e, 0x04, 0x06,
            0x09, 0x0c, 0x08, 0x02
    };

    public static byte[] encode(byte[] data) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] += (enkeystore[(data[i] >>> 4) & 0x0F] << 4);
            result[i] += (enkeystore[data[i] & 0x0F]);
        }
        return result;
    }

    public static byte[] decode(byte[] data) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] += (dekeystore[(data[i] >>> 4) & 0x0F] << 4);
            result[i] += (dekeystore[data[i] & 0x0F]);
        }
        return result;
    }

    public static String encode(String str){
        byte[] bytes = str.getBytes();
        byte[] encode = encode(bytes);
        return Base64.getEncoder().encodeToString(encode);
    }

    public static String decode(String str){
        byte[] decode = Base64.getDecoder().decode(str);
        byte[] decodeArray = decode(decode);
        return new String(decodeArray);
    }
}

