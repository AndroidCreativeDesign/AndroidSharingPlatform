package cn.daixiaodong.myapp.model;

/**
 * Created by daixiaodong on 15/7/29.
 */
public class PhotoModel {
    private String url;
    private String intro;

    public PhotoModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
