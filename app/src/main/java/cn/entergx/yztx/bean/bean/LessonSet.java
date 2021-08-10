package cn.entergx.yztx.bean.bean;
import java.io.Serializable;
import java.util.ArrayList;

public class LessonSet implements Serializable {
    public long id;
    public int lessonCount;
    public long upId;
    public long upTime;
    public int lessonType;
    public String title;
    public String description;
    public String cover_url;
    public long view_count;
    public long comment_count;
    public long collection_count;
    public long like_count;
    public Float score;
    public ArrayList<Lesson> lessons;
}
