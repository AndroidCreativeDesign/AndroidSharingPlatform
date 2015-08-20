package life.z_turn.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *  推送消息Model
 *  因为要存储在SQLite，所有用OrmLite框架注解了
 */
@DatabaseTable(tableName = "push_message")
public class PushMessageModel {


    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "message")
    private String message;


    public PushMessageModel() {
    }


    public PushMessageModel(String message) {

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
