package REST;

import Models.Question;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class quizApi {

    private List<Question> list;
    private ObjectMapper mapper = new ObjectMapper();


    public String getApiQuestions(Long id) {
        RequestObject obj = new RequestObject();
        obj.method = "GET";
        obj.url = "http://localhost:80/api/quiz/" + id;
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


    public static void main(String[] args) throws IOException {
        quizApi q = new quizApi();
        List<Question> l = q.getQuizQuestions(Long.valueOf(6));
//        System.out.println(l.get(0).getRight());
    }


}
