package com.aaa.commondevelop;

/**
 * @author liuzhen.tian
 * @version $ Id: EncodeTest.java, v 0.1 2020/7/17 15:38 liuzhen.tian Exp $
 */
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public class EncodeTest {

    @Autowired
    StringEncryptor stringEncryptor;

    @Test
    public void encryptPwd() {
        //加密密码
        String pwd = stringEncryptor.encrypt("Tlz19970905");
        System.out.println(pwd);
    }
}
