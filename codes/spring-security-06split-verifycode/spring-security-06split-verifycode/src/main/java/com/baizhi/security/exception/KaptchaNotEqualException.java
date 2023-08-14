package com.baizhi.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author wang
 * @version V1.0
 * @company Broada.com
 * @date 2023/7/30 9:09
 */

public class KaptchaNotEqualException extends AuthenticationException {
    public KaptchaNotEqualException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public KaptchaNotEqualException(String msg) {
        super(msg);
    }
}
