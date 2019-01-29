package test;

import java.util.HashSet;
import java.util.Set;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;


public class SayHelloRequestStreamHandler extends SpeechletRequestStreamHandler {

    private static final Set<String> supportedApplicationIds;

    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/
         * "Edit" the relevant Alexa Skill and put the relevant Application Ids
         * in this Set.
         */
        supportedApplicationIds = new HashSet<String>();

        //supportedApplicationIds.add("[Add your Alexa Skill ID and then uncomment and ]";
        supportedApplicationIds.add("amzn1.ask.skill.99781e9d-fde0-4e2a-8c54-5898ba390530");
        System.out.println("Supported app ids : " + supportedApplicationIds);
    }

    public SayHelloRequestStreamHandler() {
        super(new SayHelloSpeechlet(), supportedApplicationIds);
    }

    public SayHelloRequestStreamHandler(Speechlet speechlet, Set<String> supportedApplicationIds) {
        super(speechlet, supportedApplicationIds);
    }
}