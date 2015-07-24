package cn.daixiaodong.myapp.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "push_message")
public class PushMessage {


    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "message")
    private String message;


    public PushMessage() {
    }


    public PushMessage(String message) {

        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
