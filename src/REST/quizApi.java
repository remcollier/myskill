package REST;

import Models.Quiz;
import REST.Utils.RequestObject;
import REST.Utils.WebResponse;
import REST.Utils.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class quizApi {
    private List<Quiz> list;
    private ObjectMapper mapper = new ObjectMapper();
    RequestObject obj = new RequestObject();

    public quizApi() {
        obj.method = "GET";
        obj.type = "text/html";

    }

    public List<Quiz> convertQuizToList(String str) throws IOException {
        List<Quiz> quizList = Arrays.asList(mapper.readValue(str, Quiz[].class));
        return quizList;
    }

    public List<Quiz> getQuizzies() throws IOException {
        URL url = new URL("/api/allquizzes");
        obj.url = url.getUrl();
        WebResponse rest = WebUtils.sendRequest(obj);
        list = convertQuizToList(rest.getContent());
        return list;
    }


    public Quiz getQTD() throws IOException {
        URL url = new URL("/api/quizOfDay");
        obj.url = url.getUrl();
        WebResponse rest = WebUtils.sendRequest(obj);
        Quiz s = mapper.readValue(rest.getContent(), Quiz.class);
        return s;
    }

    public List<Quiz> getEasyQ() throws IOException {
        URL url = new URL("/api/easyQuiz");
        obj.url = url.getUrl();
        WebResponse rest = WebUtils.sendRequest(obj);
        list = convertQuizToList(rest.getContent());
        return list;
    }

    public List<Quiz> getHardQ() throws IOException {
        URL url = new URL("/api/hardQuiz");
        obj.url = url.getUrl();
        WebResponse rest = WebUtils.sendRequest(obj);
        list = convertQuizToList(rest.getContent());
        return list;
    }

    public List<Quiz> getMediumQ() throws IOException {
        URL url = new URL("/api/mediumQuiz");
        obj.url = url.getUrl();
        WebResponse rest = WebUtils.sendRequest(obj);
        list = convertQuizToList(rest.getContent());
        return list;
    }

    public List<Quiz> mostPlayed() throws IOException {
        URL url = new URL("/api/mostPlayed");
        obj.url = url.getUrl();
        WebResponse rest = WebUtils.sendRequest(obj);
        list = convertQuizToList(rest.getContent());
        return list;
    }


    public static void main(String[] args) throws IOException {
        quizApi q = new quizApi();
        List<Quiz> l = q.getEasyQ();
        System.out.println(l.size());
        List<Quiz> g = q.getHardQ();
        List<Quiz> f = q.getMediumQ();
        System.out.println(g.size());
        System.out.println(f.size());


    }
}
