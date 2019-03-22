package com.dubhad.qaproject.util;

import com.dubhad.qaproject.resource.Configuration;
import lombok.extern.log4j.Log4j2;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Class, that provides utility methods for security tasks
 */
@Log4j2
public class SecurityUtil {
    private static final String MD5_ENCRYPT_TYPE = "MD5";
    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String ZERO_STRING = "0";
    private static final char A_CHAR = 'a';
    private static final int ALPHABET_SiZE = 26;

    /**
     * Get random salt, that consists only from lowercase English letters
     * @return random string with length, specified in configuration file
     */
    public static String getSalt(){
        return getRandomString(Configuration.SALT_LENGTH);
    }

    /**
     * Get random code, that consists only from lowercase English letters, for registration confirmation
     * @return random string with length, specified in configuration file
     */
    public static String getConfirmationCode(){
        return getRandomString(Configuration.CONFIRMATION_CODE_LENGTH);
    }

    /**
     * random string with given length, that contains only lowercase English letters
     * @param length: length of resulting string
     * @return random string with given length
     */
    public static String getRandomString(int length){
        final Random r = new Random();
        byte[] salt = new byte[length];
        for(int i = 0; i < length; ++i){
            salt[i] = (byte) (A_CHAR + r.nextInt(ALPHABET_SiZE));
        }
        return new String(salt);
    }

    /**
     * Hexadecimal md5 hash of source string
     * @param source: string to be hashed
     * @return Hexadecimal md5 hash of source
     */
    public static String md5(String source){
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance(MD5_ENCRYPT_TYPE);
            byte[] bytes = md.digest(source.getBytes(UTF_8_ENCODING));
            for (byte b : bytes) {
                String hex = Integer.toHexString(0x00FF & b);
                if (hex.length() == 1) {
                    sb.append(ZERO_STRING);
                }
                sb.append(hex);
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            log.error(e);
            sb = new StringBuffer();
        }
        return sb.toString();
    }

    /**
     * Appends salt to source string and gets MD5 from it
     * @param source: string to be hashed
     * @param salt: salt for hashing
     * @return Hexadecimal md5 hash of source string
     */
    public static String md5(String source, String salt){
        return md5(source + salt);
    }
}
