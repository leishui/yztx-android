package cn.entergx.yztx.bean.bean;

import java.io.Serializable;

import cn.entergx.yztx.bean.page.SpringPage;

public class Comment implements Serializable
{
    public long id;
    public int type;
    public long commentId;
    public String comment_content;
    public long comment_time;
    public long like_count;
    public long reply_count;
    public long commentator_id;
    public String commentator_name;
    public String commentator_url;
    public SpringPage<Reply> replies;
}
