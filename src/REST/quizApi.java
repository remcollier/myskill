package REST;

import Models.Quiz;
import REST.WebREST.RequestObject;
import REST.WebREST.WebResponse;
import REST.WebREST.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class quizApi {
    private List<Quiz> list;
    private ObjectMapper mapper = new ObjectMapper();

    public String getApiQuizzes() {
        RequestObject obj = new RequestObject();
        obj.method = "GET";
        obj.url = "http://alexa-cs.ucd.ie:8080/api/allquizzes";
//        obj.url = "http://localhost:80/api/allquizzes";
        WebResponse rest = WebUtils.sendRequest(obj);
        return rest.getContent();
    }

    public List<Quiz> convertQuizToList(String str) throws IOException {
        List<Quiz> quizList = Arrays.asList(mapper.readValue(str, Quiz[].class));
        return quizList;
    }

    public List<Quiz> getQuizzies() throws IOException {
        list = convertQuizToList(getApiQuizzes());
        return list;
    }

//    public static void main(String[] args) throws IOException {
//        quizApi q = new quizApi();
//        List<Quiz> l = q.getQuizzies();
//        System.out.println(l.get(0).getId());
//    }
}
