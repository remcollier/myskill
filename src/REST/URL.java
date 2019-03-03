package REST;

public class URL {

    //   private String url="http://localhost:80";
    private String url="http://alexa-cs.ucd.ie:8080";

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
