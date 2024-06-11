package shihu.exception;

/**
 * 业务异常
 * @Author vocmi
 * @Email 2686782542@qq.com
 * @Date 2024-06-08
 */
public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}
