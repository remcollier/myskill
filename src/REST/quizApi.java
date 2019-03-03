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
        URL url= new URL("/api/allquizzes");
        obj.url =url.getUrl();
    }

    public String getApiQuizzes() {
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
