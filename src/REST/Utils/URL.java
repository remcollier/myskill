package REST.Utils;

public class URL {

    //       private String url="http://localhost:80";
    private String url="http://alexa-cs.ucd.ie:80";

    public URL(String url) {
        this.url += url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url += url;
    }
}
