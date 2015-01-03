package recaptcha

import net.tanesha.recaptcha.ReCaptchaResponse;

public class RecaptchaNewResponse extends ReCaptchaResponse {

    public Recaptcha2Response(boolean valid, String errorMessage) {
        super(valid, errorMessage);
    }
}
