package REST;

import Models.Question;
import REST.Utils.RequestObject;
import REST.Utils.WebResponse;
import REST.Utils.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class questionsApi {

    private List<Question> list;
    private ObjectMapper mapper = new ObjectMapper();


    public String getApiQuestions(Long id) {
        RequestObject obj = new RequestObject();
        obj.method = "GET";
        obj.type = "text/html";

        obj.url = "http://alexa-cs.ucd.ie:8080/api/quiz/" + id;
//        obj.url = "http://localhost:80/api/quiz/" + id;
        WebResponse rest = WebUtils.sendRequest(obj);

//        WebUtils.sendRequest()
        return rest.getContent();
    }

    public List<Question> convertQuestionsToList(String str) throws IOException {
        List<Question> question = Arrays.asList(mapper.readValue(str, Question[].class));
        return question;
    }


    public List<Question> getQuizQuestions(Long id) throws IOException {
        list = convertQuestionsToList(getApiQuestions(id));
        return list;
    }


//    public static void main(String[] args) throws IOException {
//        questionsApi q = new questionsApi();
//        List<Question> l = q.getQuizQuestions(Long.valueOf(1));
//        System.out.println(l.get(0).getRight());
//    }


}
