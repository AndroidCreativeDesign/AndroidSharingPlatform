package life.z_turn.app.model;

/**
 * Created by daixiaodong on 15/8/9.
 */
public class InfoBean {
    private String property;
    private String value;


    public InfoBean(String property) {
        this.property = property;
    }

    public InfoBean(String property, String value) {
        this.property = property;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
