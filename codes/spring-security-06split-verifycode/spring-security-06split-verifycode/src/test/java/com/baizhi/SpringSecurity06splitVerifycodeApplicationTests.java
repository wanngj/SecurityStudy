package com.baizhi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SpringSecurity06splitVerifycodeApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123");
        boolean matches = passwordEncoder.matches("123", "$2a$10$h9m4ftzr9LDhtoOuhFYM1uNTHUI1O4qgLYR5v/oiuoNvq.Eu1NAuC");// true

        System.out.println(encode);
        System.out.printf("matches = " + matches);
    }

}
