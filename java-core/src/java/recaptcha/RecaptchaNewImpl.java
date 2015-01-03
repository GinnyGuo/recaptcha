package recaptcha

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Verify new Google recpatcha
 */
public class RecaptchaNewImpl {
    public  Recaptcha2Impl(){
    }

    public Recaptcha2Response checkAnswer(String privateKey, String response, Logger log) {
        String postParameters = null;

        try {
            postParameters = "secret=" + URLEncoder.encode(privateKey,"UTF-8") + "&response=" + URLEncoder.encode(response,"UTF-8");
        }catch(UnsupportedEncodingException e) {
            log.error (e.toString());
        }

        String url = "https://www.google.com/recaptcha/api/siteverify?";
        String fullUrl = url + postParameters;
        String jsonResponse = Utilities.getJSON(fullUrl);
        boolean valid = false;
        String errorMessage = null;
        JsonObject dataResponse = null;
        JsonParser jsonParser = new JsonParser();

        try {
            dataResponse = jsonParser.parse(jsonResponse).getAsJsonObject();
        } catch (Exception e) {
            log.error(e.toString());
        }

        if(dataResponse == null) {
            errorMessage = "Null read from server.";
        } else {
            String errorCodes = null;
            valid = dataResponse.get("success").getAsBoolean();

            if (!valid) {
                valid = false;

                try {
                    errorCodes = dataResponse.get("error-codes").getAsString();
                } catch (NullPointerException e) {
                    log.error(e.toString());
                }
                errorMessage = errorCodes;
            }
        }
        errorMessage = errorMessage + "\nresponsee from Google verify server---"+ dataResponse  + "\nTextarea submitted to Google Verify server: " + response;
        return new Recaptcha2Response(valid, errorMessage);
    }
}
