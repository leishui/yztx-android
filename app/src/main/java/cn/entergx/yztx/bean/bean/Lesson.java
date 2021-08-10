package cn.entergx.yztx.bean.bean;

import java.io.Serializable;

public class Lesson implements Serializable
{
    public long lessonId;
    public String name;
    public String description;
    public long uploader_id;
    public String cover_url;
    public boolean source_type;
    public String resource_url;
    public long view_count;
    public long comment_count;
    public long collection_count;
    public long like_count;
    public float score;
    public long upload_time;
    public long lesson_type;
    public long lessonSetId;
    public String time_long;
    public User user;
}
