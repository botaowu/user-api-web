package im.tao.util;

import java.util.Arrays;

public class Sms4 {

    private static final int ENCRYPT = 1;
    private static final int DECRYPT = 0;

    private static final int ROUND = 32;
    private static final int BLOCK = 16;

    private static short[] sBox = {
            0xd6, 0x90, 0xe9, 0xfe, 0xcc, 0xe1, 0x3d, 0xb7, 0x16, 0xb6, 0x14, 0xc2, 0x28, 0xfb, 0x2c, 0x05,
            0x2b, 0x67, 0x9a, 0x76, 0x2a, 0xbe, 0x04, 0xc3, 0xaa, 0x44, 0x13, 0x26, 0x49, 0x86, 0x06, 0x99,
            0x9c, 0x42, 0x50, 0xf4, 0x91, 0xef, 0x98, 0x7a, 0x33, 0x54, 0x0b, 0x43, 0xed, 0xcf, 0xac, 0x62,
            0xe4, 0xb3, 0x1c, 0xa9, 0xc9, 0x08, 0xe8, 0x95, 0x80, 0xdf, 0x94, 0xfa, 0x75, 0x8f, 0x3f, 0xa6,
            0x47, 0x07, 0xa7, 0xfc, 0xf3, 0x73, 0x17, 0xba, 0x83, 0x59, 0x3c, 0x19, 0xe6, 0x85, 0x4f, 0xa8,
            0x68, 0x6b, 0x81, 0xb2, 0x71, 0x64, 0xda, 0x8b, 0xf8, 0xeb, 0x0f, 0x4b, 0x70, 0x56, 0x9d, 0x35,
            0x1e, 0x24, 0x0e, 0x5e, 0x63, 0x58, 0xd1, 0xa2, 0x25, 0x22, 0x7c, 0x3b, 0x01, 0x21, 0x78, 0x87,
            0xd4, 0x00, 0x46, 0x57, 0x9f, 0xd3, 0x27, 0x52, 0x4c, 0x36, 0x02, 0xe7, 0xa0, 0xc4, 0xc8, 0x9e,
            0xea, 0xbf, 0x8a, 0xd2, 0x40, 0xc7, 0x38, 0xb5, 0xa3, 0xf7, 0xf2, 0xce, 0xf9, 0x61, 0x15, 0xa1,
            0xe0, 0xae, 0x5d, 0xa4, 0x9b, 0x34, 0x1a, 0x55, 0xad, 0x93, 0x32, 0x30, 0xf5, 0x8c, 0xb1, 0xe3,
            0x1d, 0xf6, 0xe2, 0x2e, 0x82, 0x66, 0xca, 0x60, 0xc0, 0x29, 0x23, 0xab, 0x0d, 0x53, 0x4e, 0x6f,
            0xd5, 0xdb, 0x37, 0x45, 0xde, 0xfd, 0x8e, 0x2f, 0x03, 0xff, 0x6a, 0x72, 0x6d, 0x6c, 0x5b, 0x51,
            0x8d, 0x1b, 0xaf, 0x92, 0xbb, 0xdd, 0xbc, 0x7f, 0x11, 0xd9, 0x5c, 0x41, 0x1f, 0x10, 0x5a, 0xd8,
            0x0a, 0xc1, 0x31, 0x88, 0xa5, 0xcd, 0x7b, 0xbd, 0x2d, 0x74, 0xd0, 0x12, 0xb8, 0xe5, 0xb4, 0xb0,
            0x89, 0x69, 0x97, 0x4a, 0x0c, 0x96, 0x77, 0x7e, 0x65, 0xb9, 0xf1, 0x09, 0xc5, 0x6e, 0xc6, 0x84,
            0x18, 0xf0, 0x7d, 0xec, 0x3a, 0xdc, 0x4d, 0x20, 0x79, 0xee, 0x5f, 0x3e, 0xd7, 0xcb, 0x39, 0x48
    };

    private static int[] fk = {0xa3b1bac6, 0x56aa3350, 0x677d9197, 0xb27022dc};

    private static int[] ck = {
            0x00070e15, 0x1c232a31, 0x383f464d, 0x545b6269,
            0x70777e85, 0x8c939aa1, 0xa8afb6bd, 0xc4cbd2d9,
            0xe0e7eef5, 0xfc030a11, 0x181f262d, 0x343b4249,
            0x50575e65, 0x6c737a81, 0x888f969d, 0xa4abb2b9,
            0xc0c7ced5, 0xdce3eaf1, 0xf8ff060d, 0x141b2229,
            0x30373e45, 0x4c535a61, 0x686f767d, 0x848b9299,
            0xa0a7aeb5, 0xbcc3cad1, 0xd8dfe6ed, 0xf4fb0209,
            0x10171e25, 0x2c333a41, 0x484f565d, 0x646b7279
    };

    private int rotl(int x, int y) {
        return x << y | x >>> (32 - y);
    }

    private int byteSub(int a) {
        return (sBox[a >>> 24 & 0xFF] & 0xFF) << 24
                ^ (sBox[a >>> 16 & 0xFF] & 0xFF) << 16
                ^ (sBox[a >>> 8 & 0xFF] & 0xFF) << 8
                ^ (sBox[a & 0xFF] & 0xFF);
    }

    private int l1(int b) {
        return b ^ rotl(b, 2) ^ rotl(b, 10) ^ rotl(b, 18) ^ rotl(b, 24);
    }

    private int l2(int b) {
        return b ^ rotl(b, 13) ^ rotl(b, 23);
    }

    private void sms4Crypt(byte[] input, byte[] output, int[] rk) {
        int mid;
        int[] x = new int[4];
        int[] tmp = new int[4];
        for (int i = 0; i < 4; i++) {
            tmp[0] = input[4 * i] & 0xff;
            tmp[1] = input[1 + 4 * i] & 0xff;
            tmp[2] = input[2 + 4 * i] & 0xff;
            tmp[3] = input[3 + 4 * i] & 0xff;
            x[i] = tmp[0] << 24 | tmp[1] << 16 | tmp[2] << 8 | tmp[3];
        }
        // 进行32轮的加密变换操作
        for (int r = 0; r < 32; r += 4) {
            mid = x[1] ^ x[2] ^ x[3] ^ rk[r];
            mid = byteSub(mid);
            // x4
            x[0] = x[0] ^ l1(mid);

            mid = x[2] ^ x[3] ^ x[0] ^ rk[r + 1];
            mid = byteSub(mid);
            // x5
            x[1] = x[1] ^ l1(mid);

            mid = x[3] ^ x[0] ^ x[1] ^ rk[r + 2];
            mid = byteSub(mid);
            // x6
            x[2] = x[2] ^ l1(mid);

            mid = x[0] ^ x[1] ^ x[2] ^ rk[r + 3];
            mid = byteSub(mid);
            // x7
            x[3] = x[3] ^ l1(mid);
        }

        // 反序变换
        for (int j = 0; j < 16; j += 4) {
            output[j] = (byte) (x[3 - j / 4] >>> 24 & 0xFF);
            output[j + 1] = (byte) (x[3 - j / 4] >>> 16 & 0xFF);
            output[j + 2] = (byte) (x[3 - j / 4] >>> 8 & 0xFF);
            output[j + 3] = (byte) (x[3 - j / 4] & 0xFF);
        }
    }

    private void sms4KeyExt(byte[] key, int[] rk, int cryptFlag) {
        int r, mid;
        int[] x = new int[4];
        int[] tmp = new int[4];
        for (int i = 0; i < 4; i++) {
            // 实现对初始密钥的分组（分为4组）
            tmp[0] = key[4 * i] & 0xFF;
            tmp[1] = key[1 + 4 * i] & 0xff;
            tmp[2] = key[2 + 4 * i] & 0xff;
            tmp[3] = key[3 + 4 * i] & 0xff;

            x[i] = tmp[0] << 24 | tmp[1] << 16 | tmp[2] << 8 | tmp[3];
            x[i] = key[4 * i] << 24 | key[1 + 4 * i] << 16 | key[2 + 4 * i] << 8 | key[3 + 4 * i];
        }
        // 异或运算之后的结果
        x[0] ^= fk[0];
        x[1] ^= fk[1];
        x[2] ^= fk[2];
        x[3] ^= fk[3];
        for (r = 0; r < 32; r += 4) {
            //
            mid = x[1] ^ x[2] ^ x[3] ^ ck[r];
            mid = byteSub(mid);
            // rk0=K4
            rk[r] = x[0] ^= l2(mid);

            mid = x[2] ^ x[3] ^ x[0] ^ ck[r + 1];
            mid = byteSub(mid);
            // rk1=K5
            rk[r + 1] = x[1] ^= l2(mid);

            mid = x[3] ^ x[0] ^ x[1] ^ ck[r + 2];
            mid = byteSub(mid);
            // rk2=K6
            rk[r + 2] = x[2] ^= l2(mid);

            mid = x[0] ^ x[1] ^ x[2] ^ ck[r + 3];
            mid = byteSub(mid);
            // rk3=K7
            rk[r + 3] = x[3] ^= l2(mid);
        }

        // cryptFla==0 为解密，解密时轮密钥使用顺序：rk31,rk30,...,rk0（逆序）
        if (cryptFlag == DECRYPT) {
            for (r = 0; r < 16; r++) {
                mid = rk[r];
                rk[r] = rk[31 - r];
                rk[31 - r] = mid;
            }
        }
    }

    private void sms4(byte[] in, int inLen, byte[] key, byte[] out, int cryptFlag) {
        int point = 0;
        int[] roundKey = new int[ROUND];
        sms4KeyExt(key, roundKey, cryptFlag);
        byte[] input;
        byte[] output = new byte[16];
        while (inLen >= BLOCK) {
            input = Arrays.copyOfRange(in, point, point + 16);
            sms4Crypt(input, output, roundKey);
            System.arraycopy(output, 0, out, point, BLOCK);
            inLen -= BLOCK;
            point += BLOCK;
        }
    }

    private static byte[] encodeSMS4(String plaintext, byte[] key) {
        if (plaintext == null || "".equals(plaintext)) {
            return null;
        }
        for (int i = plaintext.getBytes().length % 16; i < 16; i++) {
            plaintext += '\0';
        }
        return Sms4.encodeSMS4(plaintext.getBytes(), key);
    }

    private static byte[] encodeSMS4(byte[] plainText, byte[] key) {
        byte[] ciphertext = new byte[plainText.length];
        int k = 0;
        int plainLen = plainText.length;
        while (k + 16 <= plainLen) {
            byte[] cellPlain = new byte[16];
            for (int i = 0; i < 16; i++) {
                cellPlain[i] = plainText[k + i];
            }
            byte[] cellCipher = encode16(cellPlain, key);
            for (int i = 0; i < cellCipher.length; i++) {
                ciphertext[k + i] = cellCipher[i];
            }
            k += 16;
        }
        return ciphertext;
    }

    private static byte[] decodeSMS4(byte[] cipherText, byte[] key) {
        byte[] plaintext = new byte[cipherText.length];
        int k = 0;
        int cipherLen = cipherText.length;
        while (k + 16 <= cipherLen) {
            byte[] cellCipher = new byte[16];
            for (int i = 0; i < 16; i++) {
                cellCipher[i] = cipherText[k + i];
            }
            byte[] cellPlain = decode16(cellCipher, key);
            for (int i = 0; i < cellPlain.length; i++) {
                plaintext[k + i] = cellPlain[i];
            }
            k += 16;
        }
        return plaintext;
    }

    private static String decodeSMS4toString(byte[] cipherText, byte[] key) {
        byte[] plaintext = new byte[cipherText.length];
        plaintext = decodeSMS4(cipherText, key);
        return new String(plaintext);
    }

    private static byte[] encode16(byte[] plainText, byte[] key) {
        byte[] cipher = new byte[16];
        Sms4 sm4 = new Sms4();
        sm4.sms4(plainText, 16, key, cipher, ENCRYPT);
        return cipher;
    }

    private static byte[] decode16(byte[] ciphertext, byte[] key) {
        byte[] plain = new byte[16];
        Sms4 sm4 = new Sms4();
        sm4.sms4(ciphertext, 16, key, plain, DECRYPT);
        return plain;
    }

    private static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1) {
            throw new IllegalArgumentException("this byteArray must not be null or empty");
        }

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10) {
                hexString.append("0");
            }
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString().toLowerCase();
    }

    public static String encrypt(String text, String key) {
        byte[] keys = key.getBytes();
        byte[] out = encodeSMS4(text, keys);
        if (out == null) {
            return null;
        }

        return toHexString(out);
    }

}