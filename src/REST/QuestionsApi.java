package REST;

import Models.Question;
import REST.Utils.RequestObject;
import REST.Utils.URL;
import REST.Utils.WebResponse;
import REST.Utils.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class QuestionsApi {

    private List<Question> list;
    private ObjectMapper mapper = new ObjectMapper();


    public String getApiQuestions(Long id) {
        RequestObject obj = new RequestObject();
        obj.method = "GET";
        obj.type = "text/html";
        URL url = new URL("/api/quiz/" + id);
        obj.url = url.getUrl();
        WebResponse rest = WebUtils.sendRequest(obj);

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
//        QuestionsApi q = new QuestionsApi();
//        List<Question> l = q.getQuizQuestions(Long.valueOf(1));
//        System.out.println(l.get(0).getRight());
//    }


}
