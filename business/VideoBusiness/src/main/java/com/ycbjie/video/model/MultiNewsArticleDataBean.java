package com.ycbjie.video.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MultiNewsArticleDataBean implements Parcelable {

    public static final Creator<MultiNewsArticleDataBean> CREATOR = new Creator<MultiNewsArticleDataBean>() {
        @Override
        public MultiNewsArticleDataBean createFromParcel(Parcel in) {
            return new MultiNewsArticleDataBean(in);
        }

        @Override
        public MultiNewsArticleDataBean[] newArray(int size) {
            return new MultiNewsArticleDataBean[size];
        }
    };
    /**
     * log_pb : {"impr_id":"20170519112306010003048108480AA6"}
     * read_count : 156694
     * media_name : 人民网
     * ban_comment : 0
     * abstract : 5月14日,国家主席习近平在北京人民大会堂举行宴会,欢迎出席“一带一路”国际合作高峰论坛的外方代表团团长及嘉宾。习近平发表致辞,代表中国政府和人民热烈欢迎各位贵宾的到来。新华社记者 刘卫兵摄孟夏之日,万物并秀。
     * ban_bury : 1
     * has_video : false
     * article_type : 1
     * tag : news_politics
     * forward_info : {"forward_count":8}
     * has_m3u8_video : 0
     * keywords : 自由贸易区,共同体,国际合作高峰论坛,自由化,祝酒辞
     * rid : 20170519112306010003048108480AA6
     * label : 置顶
     * show_portrait_article : false
     * user_verified : 0
     * aggr_type : 1
     * cell_type : 0
     * article_sub_type : 1
     * bury_count : 0
     * title : 习近平“一带一路”高峰论坛系列讲话十大高频词
     * ignore_web_transform : 1
     * source_icon_style : 5
     * tip : 0
     * hot : 0
     * share_url : http://toutiao.com/group/6421272334078247169/?iid=0&app=news_article
     * has_mp4_video : 0
     * source : 人民网
     * comment_count : 2
     * article_url : http://m2.people.cn/r/MV8xXzI5Mjg0OTA4XzQxMTgzN18xNDk1MDg3NTQx
     * filter_words : []
     * share_count : 972
     * stick_label : 置顶
     * publish_time : 1495087541
     * action_list : [{"action":1,"extra":{},"desc":""},{"action":3,"extra":{},"desc":""},{"action":7,"extra":{},"desc":""},{"action":9,"extra":{},"desc":""}]
     * has_image : true
     * cell_layout_style : 1
     * tag_id : 6421272334078247000
     * video_style : 0
     * verified_content :
     * display_url : http://toutiao.com/group/6421272334078247169/
     * is_stick : true
     * large_image_list : []
     * item_id : 6421353649858938000
     * is_subject : false
     * stick_style : 1
     * show_portrait : false
     * repin_count : 2062
     * cell_flag : 11
     * user_info : {"verified_content":"","avatar_url":"http://p3.pstatp.com/thumb/ca400072481685ad43b","user_id":50502346173,"name":"人民网","follower_count":0,"follow":false,"user_auth_info":"","user_verified":false,"description":""}
     * source_open_url : sslocal://profile?uid=50502346173
     * level : 0
     * digg_count : 0
     * behot_time : 1495164186
     * article_alt_url : http://toutiao.com/group/article/6421272334078247169/
     * cursor : 1495164186999
     * url : http://m2.people.cn/r/MV8xXzI5Mjg0OTA4XzQxMTgzN18xNDk1MDg3NTQx
     * preload_web : 0
     * user_repin : 0
     * label_style : 1
     * item_version : 0
     * media_info : {"user_id":50502346173,"verified_content":"","avatar_url":"http://p3.pstatp.com/large/ca400072481685ad43b","media_id":50502346173,"name":"人民网","recommend_type":0,"follow":false,"recommend_reason":"","is_star_user":false,"user_verified":false}
     * group_id : 6421272334078247000
     * middle_image : {"url":"http://p9.pstatp.com/list/300x196/207c000e549a17910c1c.webp","width":400,"url_list":[{"url":"http://p9.pstatp.com/list/300x196/207c000e549a17910c1c.webp"},{"url":"http://pb1.pstatp.com/list/300x196/207c000e549a17910c1c.webp"},{"url":"http://pb3.pstatp.com/list/300x196/207c000e549a17910c1c.webp"}],"uri":"list/207c000e549a17910c1c","height":225}
     * gallary_image_count : 1
     */

    private LogPbBean log_pb;
    private int read_count;
    private String media_name;
    private int ban_comment;
    @SerializedName("abstract")
    private String abstractX;
    private int ban_bury;
    private boolean has_video;
    private int article_type;
    private String tag;
    private ForwardInfoBean forward_info;
    //    private boolean has_m3u8_video;
    private String keywords;
    private String rid;
    private String label;
    private boolean show_portrait_article;
    private int user_verified;
    private int aggr_type;
    private int cell_type;
    private int article_sub_type;
    private int bury_count;
    private String title;
    private int ignore_web_transform;
    private int source_icon_style;
    private int tip;
    private int hot;
    private String share_url;
    private int has_mp4_video;
    private String source;
    private int comment_count;
    private String article_url;
    private int share_count;
    private String stick_label;
    private int publish_time;
    private boolean has_image;
    private int cell_layout_style;
    private long tag_id;
    private int video_style;
    private String verified_content;
    private String display_url;
    //    private boolean is_stick;
    private long item_id;
    private boolean is_subject;
    private int stick_style;
    private boolean show_portrait;
    private int repin_count;
    private int cell_flag;
    private UserInfoBean user_info;
    private String source_open_url;
    private int level;
    private int digg_count;
    private String behot_time;
    private String article_alt_url;
    private long cursor;
    private String url;
    private int preload_web;
    private int user_repin;
    private int label_style;
    private int item_version;
    private MediaInfoBean media_info;
    private long group_id;
    private MiddleImageBean middle_image;
    private int gallary_image_count;
    /**
     * video_id : d4715d18819e4acc8b3012374dd4588f
     * video_detail_info : {"group_flags":32832,"video_type":0,"video_preloading_flag":1,"video_url":[],"direct_play":1,"detail_video_large_image":{"url":"http://p3.pstatp.com/video1609/17600009fb1bb36ce3ee","width":580,"url_list":[{"url":"http://p3.pstatp.com/video1609/17600009fb1bb36ce3ee"},{"url":"http://pb9.pstatp.com/video1609/17600009fb1bb36ce3ee"},{"url":"http://pb1.pstatp.com/video1609/17600009fb1bb36ce3ee"}],"uri":"video1609/17600009fb1bb36ce3ee","height":326},"show_pgc_subscribe":1,"video_third_monitor_url":"","video_id":"d4715d18819e4acc8b3012374dd4588f","video_watching_count":0,"video_watch_count":3502527}
     * image_list : []
     * video_duration : 503
     * group_flags : 32832
     * filter_words : [{"id":"8:0","name":"重复、旧闻","is_selected":false},{"id":"9:1","name":"内容质量差","is_selected":false},{"id":"5:824751570","name":"来源:穹娱剪辑","is_selected":false},{"id":"1:548663395","name":"社会视频","is_selected":false},{"id":"6:15613","name":"银行","is_selected":false}]
     * action_list : [{"action":1,"extra":{},"desc":""},{"action":3,"extra":{},"desc":""},{"action":7,"extra":{},"desc":""},{"action":9,"extra":{},"desc":""}]
     * large_image_list : [{"url":"http://p3.pstatp.com/video1609/17600009fb1bb36ce3ee","width":580,"url_list":[{"url":"http://p3.pstatp.com/video1609/17600009fb1bb36ce3ee"},{"url":"http://pb9.pstatp.com/video1609/17600009fb1bb36ce3ee"},{"url":"http://pb1.pstatp.com/video1609/17600009fb1bb36ce3ee"}],"uri":"video1609/17600009fb1bb36ce3ee","height":326}]
     * like_count : 4787
     */

    private String video_id;
    private VideoDetailInfoBean video_detail_info;
    private int video_duration;
    private int group_flags;
    private int like_count;
    private List<ImageListBean> image_list;
    public MultiNewsArticleDataBean() {
    }
    protected MultiNewsArticleDataBean(Parcel in) {
        read_count = in.readInt();
        media_name = in.readString();
        ban_comment = in.readInt();
        abstractX = in.readString();
        ban_bury = in.readInt();
        has_video = in.readByte() != 0;
        article_type = in.readInt();
        tag = in.readString();
//        has_m3u8_video = in.readByte() != 0;
        keywords = in.readString();
        rid = in.readString();
        label = in.readString();
        show_portrait_article = in.readByte() != 0;
        user_verified = in.readInt();
        aggr_type = in.readInt();
        cell_type = in.readInt();
        article_sub_type = in.readInt();
        bury_count = in.readInt();
        title = in.readString();
        ignore_web_transform = in.readInt();
        source_icon_style = in.readInt();
        tip = in.readInt();
        hot = in.readInt();
        share_url = in.readString();
        has_mp4_video = in.readInt();
        source = in.readString();
        comment_count = in.readInt();
        article_url = in.readString();
        share_count = in.readInt();
        stick_label = in.readString();
        publish_time = in.readInt();
        has_image = in.readByte() != 0;
        cell_layout_style = in.readInt();
        tag_id = in.readLong();
        video_style = in.readInt();
        verified_content = in.readString();
        display_url = in.readString();
        item_id = in.readLong();
        is_subject = in.readByte() != 0;
        stick_style = in.readInt();
        show_portrait = in.readByte() != 0;
        repin_count = in.readInt();
        cell_flag = in.readInt();
        source_open_url = in.readString();
        level = in.readInt();
        digg_count = in.readInt();
        behot_time = in.readString();
        article_alt_url = in.readString();
        cursor = in.readLong();
        url = in.readString();
        preload_web = in.readInt();
        user_repin = in.readInt();
        label_style = in.readInt();
        item_version = in.readInt();
        media_info = in.readParcelable(MediaInfoBean.class.getClassLoader());
        group_id = in.readLong();
        gallary_image_count = in.readInt();
        video_id = in.readString();
        video_detail_info = in.readParcelable(VideoDetailInfoBean.class.getClassLoader());
        video_duration = in.readInt();
        group_flags = in.readInt();
        like_count = in.readInt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MultiNewsArticleDataBean that = (MultiNewsArticleDataBean) o;

        if (item_id != that.item_id)
            return false;
        return title.equals(that.title);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + (int) (item_id ^ (item_id >>> 32));
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(read_count);
        dest.writeString(media_name);
        dest.writeInt(ban_comment);
        dest.writeString(abstractX);
        dest.writeInt(ban_bury);
        dest.writeByte((byte) (has_video ? 1 : 0));
        dest.writeInt(article_type);
        dest.writeString(tag);
//        dest.writeByte((byte) (has_m3u8_video ? 1 : 0));
        dest.writeString(keywords);
        dest.writeString(rid);
        dest.writeString(label);
        dest.writeByte((byte) (show_portrait_article ? 1 : 0));
        dest.writeInt(user_verified);
        dest.writeInt(aggr_type);
        dest.writeInt(cell_type);
        dest.writeInt(article_sub_type);
        dest.writeInt(bury_count);
        dest.writeString(title);
        dest.writeInt(ignore_web_transform);
        dest.writeInt(source_icon_style);
        dest.writeInt(tip);
        dest.writeInt(hot);
        dest.writeString(share_url);
        dest.writeInt(has_mp4_video);
        dest.writeString(source);
        dest.writeInt(comment_count);
        dest.writeString(article_url);
        dest.writeInt(share_count);
        dest.writeString(stick_label);
        dest.writeInt(publish_time);
        dest.writeByte((byte) (has_image ? 1 : 0));
        dest.writeInt(cell_layout_style);
        dest.writeLong(tag_id);
        dest.writeInt(video_style);
        dest.writeString(verified_content);
        dest.writeString(display_url);
        dest.writeLong(item_id);
        dest.writeByte((byte) (is_subject ? 1 : 0));
        dest.writeInt(stick_style);
        dest.writeByte((byte) (show_portrait ? 1 : 0));
        dest.writeInt(repin_count);
        dest.writeInt(cell_flag);
        dest.writeString(source_open_url);
        dest.writeInt(level);
        dest.writeInt(digg_count);
        dest.writeString(behot_time);
        dest.writeString(article_alt_url);
        dest.writeLong(cursor);
        dest.writeString(url);
        dest.writeInt(preload_web);
        dest.writeInt(user_repin);
        dest.writeInt(label_style);
        dest.writeInt(item_version);
        dest.writeParcelable(media_info, flags);
        dest.writeLong(group_id);
        dest.writeInt(gallary_image_count);
        dest.writeString(video_id);
        dest.writeParcelable(video_detail_info, flags);
        dest.writeInt(video_duration);
        dest.writeInt(group_flags);
        dest.writeInt(like_count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public LogPbBean getLog_pb() {
        return log_pb;
    }

    public void setLog_pb(LogPbBean log_pb) {
        this.log_pb = log_pb;
    }

    public int getRead_count() {
        return read_count;
    }

    public void setRead_count(int read_count) {
        this.read_count = read_count;
    }

    public String getMedia_name() {
        return media_name;
    }

    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    public int getBan_comment() {
        return ban_comment;
    }

    public void setBan_comment(int ban_comment) {
        this.ban_comment = ban_comment;
    }

    public String getAbstractX() {
        return abstractX;
    }

    public void setAbstractX(String abstractX) {
        this.abstractX = abstractX;
    }

    public int getBan_bury() {
        return ban_bury;
    }

    public void setBan_bury(int ban_bury) {
        this.ban_bury = ban_bury;
    }

    public boolean isHas_video() {
        return has_video;
    }

    public void setHas_video(boolean has_video) {
        this.has_video = has_video;
    }

    public int getArticle_type() {
        return article_type;
    }

    public void setArticle_type(int article_type) {
        this.article_type = article_type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ForwardInfoBean getForward_info() {
        return forward_info;
    }

    public void setForward_info(ForwardInfoBean forward_info) {
        this.forward_info = forward_info;
    }

//    public boolean getHas_m3u8_video() {
//        return has_m3u8_video;
//    }

//    public void setHas_m3u8_video(boolean has_m3u8_video) {
//        this.has_m3u8_video = has_m3u8_video;
//    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isShow_portrait_article() {
        return show_portrait_article;
    }

    public void setShow_portrait_article(boolean show_portrait_article) {
        this.show_portrait_article = show_portrait_article;
    }

    public int getUser_verified() {
        return user_verified;
    }

    public void setUser_verified(int user_verified) {
        this.user_verified = user_verified;
    }

    public int getAggr_type() {
        return aggr_type;
    }

    public void setAggr_type(int aggr_type) {
        this.aggr_type = aggr_type;
    }

    public int getCell_type() {
        return cell_type;
    }

    public void setCell_type(int cell_type) {
        this.cell_type = cell_type;
    }

    public int getArticle_sub_type() {
        return article_sub_type;
    }

    public void setArticle_sub_type(int article_sub_type) {
        this.article_sub_type = article_sub_type;
    }

    public int getBury_count() {
        return bury_count;
    }

    public void setBury_count(int bury_count) {
        this.bury_count = bury_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIgnore_web_transform() {
        return ignore_web_transform;
    }

    public void setIgnore_web_transform(int ignore_web_transform) {
        this.ignore_web_transform = ignore_web_transform;
    }

    public int getSource_icon_style() {
        return source_icon_style;
    }

    public void setSource_icon_style(int source_icon_style) {
        this.source_icon_style = source_icon_style;
    }

    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public int getHas_mp4_video() {
        return has_mp4_video;
    }

    public void setHas_mp4_video(int has_mp4_video) {
        this.has_mp4_video = has_mp4_video;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getArticle_url() {
        return article_url;
    }

    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public String getStick_label() {
        return stick_label;
    }

    public void setStick_label(String stick_label) {
        this.stick_label = stick_label;
    }

    public int getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(int publish_time) {
        this.publish_time = publish_time;
    }

    public boolean isHas_image() {
        return has_image;
    }

    public void setHas_image(boolean has_image) {
        this.has_image = has_image;
    }

    public int getCell_layout_style() {
        return cell_layout_style;
    }

    public void setCell_layout_style(int cell_layout_style) {
        this.cell_layout_style = cell_layout_style;
    }

    public long getTag_id() {
        return tag_id;
    }

    public void setTag_id(long tag_id) {
        this.tag_id = tag_id;
    }

    public int getVideo_style() {
        return video_style;
    }

    public void setVideo_style(int video_style) {
        this.video_style = video_style;
    }

    public String getVerified_content() {
        return verified_content;
    }

    public void setVerified_content(String verified_content) {
        this.verified_content = verified_content;
    }

    public String getDisplay_url() {
        return display_url;
    }

    public void setDisplay_url(String display_url) {
        this.display_url = display_url;
    }

//    public boolean is_stick() {
//        return is_stick;
//    }

//    public void setIs_stick(boolean is_stick) {
//        this.is_stick = is_stick;
//    }

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public boolean is_subject() {
        return is_subject;
    }

    public void setIs_subject(boolean is_subject) {
        this.is_subject = is_subject;
    }

    public int getStick_style() {
        return stick_style;
    }

    public void setStick_style(int stick_style) {
        this.stick_style = stick_style;
    }

    public boolean isShow_portrait() {
        return show_portrait;
    }

    public void setShow_portrait(boolean show_portrait) {
        this.show_portrait = show_portrait;
    }

    public int getRepin_count() {
        return repin_count;
    }

    public void setRepin_count(int repin_count) {
        this.repin_count = repin_count;
    }

    public int getCell_flag() {
        return cell_flag;
    }

    public void setCell_flag(int cell_flag) {
        this.cell_flag = cell_flag;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public String getSource_open_url() {
        return source_open_url;
    }

    public void setSource_open_url(String source_open_url) {
        this.source_open_url = source_open_url;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDigg_count() {
        return digg_count;
    }

    public void setDigg_count(int digg_count) {
        this.digg_count = digg_count;
    }

    public String getBehot_time() {
        return behot_time;
    }

    public void setBehot_time(String behot_time) {
        this.behot_time = behot_time;
    }

    public String getArticle_alt_url() {
        return article_alt_url;
    }

    public void setArticle_alt_url(String article_alt_url) {
        this.article_alt_url = article_alt_url;
    }

    public long getCursor() {
        return cursor;
    }

    public void setCursor(long cursor) {
        this.cursor = cursor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPreload_web() {
        return preload_web;
    }

    public void setPreload_web(int preload_web) {
        this.preload_web = preload_web;
    }

    public int getUser_repin() {
        return user_repin;
    }

    public void setUser_repin(int user_repin) {
        this.user_repin = user_repin;
    }

    public int getLabel_style() {
        return label_style;
    }

    public void setLabel_style(int label_style) {
        this.label_style = label_style;
    }

    public int getItem_version() {
        return item_version;
    }

    public void setItem_version(int item_version) {
        this.item_version = item_version;
    }

    public MediaInfoBean getMedia_info() {
        return media_info;
    }

    public void setMedia_info(MediaInfoBean media_info) {
        this.media_info = media_info;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public MiddleImageBean getMiddle_image() {
        return middle_image;
    }

    public void setMiddle_image(MiddleImageBean middle_image) {
        this.middle_image = middle_image;
    }

    public int getGallary_image_count() {
        return gallary_image_count;
    }

    public void setGallary_image_count(int gallary_image_count) {
        this.gallary_image_count = gallary_image_count;
    }

    public List<ImageListBean> getImage_list() {
        return image_list;
    }

    public void setImage_list(List<ImageListBean> image_list) {
        this.image_list = image_list;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public VideoDetailInfoBean getVideo_detail_info() {
        return video_detail_info;
    }

    public void setVideo_detail_info(VideoDetailInfoBean video_detail_info) {
        this.video_detail_info = video_detail_info;
    }

    public int getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(int video_duration) {
        this.video_duration = video_duration;
    }

    public int getGroup_flags() {
        return group_flags;
    }

    public void setGroup_flags(int group_flags) {
        this.group_flags = group_flags;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    @Override
    public String toString() {
        return "MultiNewsArticleDataBean{" +
                "log_pb=" + log_pb +
                ", read_count=" + read_count +
                ", media_name='" + media_name + '\'' +
                ", ban_comment=" + ban_comment +
                ", abstractX='" + abstractX + '\'' +
                ", ban_bury=" + ban_bury +
                ", has_video=" + has_video +
                ", article_type=" + article_type +
                ", tag='" + tag + '\'' +
                ", forward_info=" + forward_info +
//                ", has_m3u8_video=" + has_m3u8_video +
                ", keywords='" + keywords + '\'' +
                ", rid='" + rid + '\'' +
                ", label='" + label + '\'' +
                ", show_portrait_article=" + show_portrait_article +
                ", user_verified=" + user_verified +
                ", aggr_type=" + aggr_type +
                ", cell_type=" + cell_type +
                ", article_sub_type=" + article_sub_type +
                ", bury_count=" + bury_count +
                ", title='" + title + '\'' +
                ", ignore_web_transform=" + ignore_web_transform +
                ", source_icon_style=" + source_icon_style +
                ", tip=" + tip +
                ", hot=" + hot +
                ", share_url='" + share_url + '\'' +
                ", has_mp4_video=" + has_mp4_video +
                ", source='" + source + '\'' +
                ", comment_count=" + comment_count +
                ", article_url='" + article_url + '\'' +
                ", share_count=" + share_count +
                ", stick_label='" + stick_label + '\'' +
                ", publish_time=" + publish_time +
                ", has_image=" + has_image +
                ", cell_layout_style=" + cell_layout_style +
                ", tag_id=" + tag_id +
                ", video_style=" + video_style +
                ", verified_content='" + verified_content + '\'' +
                ", display_url='" + display_url + '\'' +
                ", item_id=" + item_id +
                ", is_subject=" + is_subject +
                ", stick_style=" + stick_style +
                ", show_portrait=" + show_portrait +
                ", repin_count=" + repin_count +
                ", cell_flag=" + cell_flag +
                ", user_info=" + user_info +
                ", source_open_url='" + source_open_url + '\'' +
                ", level=" + level +
                ", digg_count=" + digg_count +
                ", behot_time='" + behot_time + '\'' +
                ", article_alt_url='" + article_alt_url + '\'' +
                ", cursor=" + cursor +
                ", url='" + url + '\'' +
                ", preload_web=" + preload_web +
                ", user_repin=" + user_repin +
                ", label_style=" + label_style +
                ", item_version=" + item_version +
                ", media_info=" + media_info +
                ", group_id=" + group_id +
                ", middle_image=" + middle_image +
                ", gallary_image_count=" + gallary_image_count +
                ", video_id='" + video_id + '\'' +
                ", video_detail_info=" + video_detail_info +
                ", video_duration=" + video_duration +
                ", group_flags=" + group_flags +
                ", like_count=" + like_count +
                ", image_list=" + image_list +
                '}';
    }

    public static class LogPbBean {
        /**
         * impr_id : 20170519112306010003048108480AA6
         */

        private String impr_id;

        public String getImpr_id() {
            return impr_id;
        }

        public void setImpr_id(String impr_id) {
            this.impr_id = impr_id;
        }
    }

    public static class ForwardInfoBean {
        /**
         * forward_count : 8
         */

        private int forward_count;

        public int getForward_count() {
            return forward_count;
        }

        public void setForward_count(int forward_count) {
            this.forward_count = forward_count;
        }
    }

    public static class UserInfoBean {
        /**
         * verified_content :
         * avatar_url : http://p3.pstatp.com/thumb/ca400072481685ad43b
         * user_id : 50502346173
         * name : 人民网
         * follower_count : 0
         * follow : false
         * user_auth_info :
         * user_verified : false
         * description :
         */

        private String verified_content;
        private String avatar_url;
        private long user_id;
        private String name;
        private int follower_count;
        private boolean follow;
        private String user_auth_info;
        private boolean user_verified;
        private String description;

        public String getVerified_content() {
            return verified_content;
        }

        public void setVerified_content(String verified_content) {
            this.verified_content = verified_content;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getFollower_count() {
            return follower_count;
        }

        public void setFollower_count(int follower_count) {
            this.follower_count = follower_count;
        }

        public boolean isFollow() {
            return follow;
        }

        public void setFollow(boolean follow) {
            this.follow = follow;
        }

        public String getUser_auth_info() {
            return user_auth_info;
        }

        public void setUser_auth_info(String user_auth_info) {
            this.user_auth_info = user_auth_info;
        }

        public boolean isUser_verified() {
            return user_verified;
        }

        public void setUser_verified(boolean user_verified) {
            this.user_verified = user_verified;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class MediaInfoBean implements Parcelable {
        public static final Creator<MediaInfoBean> CREATOR = new Creator<MediaInfoBean>() {
            @Override
            public MediaInfoBean createFromParcel(Parcel in) {
                return new MediaInfoBean(in);
            }

            @Override
            public MediaInfoBean[] newArray(int size) {
                return new MediaInfoBean[size];
            }
        };
        /**
         * user_id : 50502346173
         * verified_content :
         * avatar_url : http://p3.pstatp.com/large/ca400072481685ad43b
         * media_id : 50502346173
         * name : 人民网
         * recommend_type : 0
         * follow : false
         * recommend_reason :
         * is_star_user : false
         * user_verified : false
         */

        private long user_id;
        private String verified_content;
        private String avatar_url;
        private String media_id;
        private String name;
        private int recommend_type;
        private boolean follow;
        private String recommend_reason;
        private boolean is_star_user;
        private boolean user_verified;

        public MediaInfoBean() {

        }

        protected MediaInfoBean(Parcel in) {
            user_id = in.readLong();
            verified_content = in.readString();
            avatar_url = in.readString();
            media_id = in.readString();
            name = in.readString();
            recommend_type = in.readInt();
            follow = in.readByte() != 0;
            recommend_reason = in.readString();
            is_star_user = in.readByte() != 0;
            user_verified = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(user_id);
            dest.writeString(verified_content);
            dest.writeString(avatar_url);
            dest.writeString(media_id);
            dest.writeString(name);
            dest.writeInt(recommend_type);
            dest.writeByte((byte) (follow ? 1 : 0));
            dest.writeString(recommend_reason);
            dest.writeByte((byte) (is_star_user ? 1 : 0));
            dest.writeByte((byte) (user_verified ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public String getVerified_content() {
            return verified_content;
        }

        public void setVerified_content(String verified_content) {
            this.verified_content = verified_content;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getMedia_id() {
            return media_id;
        }

        public void setMedia_id(String media_id) {
            this.media_id = media_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRecommend_type() {
            return recommend_type;
        }

        public void setRecommend_type(int recommend_type) {
            this.recommend_type = recommend_type;
        }

        public boolean isFollow() {
            return follow;
        }

        public void setFollow(boolean follow) {
            this.follow = follow;
        }

        public String getRecommend_reason() {
            return recommend_reason;
        }

        public void setRecommend_reason(String recommend_reason) {
            this.recommend_reason = recommend_reason;
        }

        public boolean isIs_star_user() {
            return is_star_user;
        }

        public void setIs_star_user(boolean is_star_user) {
            this.is_star_user = is_star_user;
        }

        public boolean isUser_verified() {
            return user_verified;
        }

        public void setUser_verified(boolean user_verified) {
            this.user_verified = user_verified;
        }
    }

    public static class MiddleImageBean {
        /**
         * url : http://p9.pstatp.com/list/300x196/207c000e549a17910c1c.webp
         * width : 400
         * url_list : [{"url":"http://p9.pstatp.com/list/300x196/207c000e549a17910c1c.webp"},{"url":"http://pb1.pstatp.com/list/300x196/207c000e549a17910c1c.webp"},{"url":"http://pb3.pstatp.com/list/300x196/207c000e549a17910c1c.webp"}]
         * uri : list/207c000e549a17910c1c
         * height : 225
         */

        private String url;
        private int width;
        private String uri;
        private int height;
        private List<UrlListBean> url_list;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public List<UrlListBean> getUrl_list() {
            return url_list;
        }

        public void setUrl_list(List<UrlListBean> url_list) {
            this.url_list = url_list;
        }

        public static class UrlListBean {
            /**
             * url : http://p9.pstatp.com/list/300x196/207c000e549a17910c1c.webp
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }

    public static class VideoDetailInfoBean implements Parcelable {
        public static final Creator<VideoDetailInfoBean> CREATOR = new Creator<VideoDetailInfoBean>() {
            @Override
            public VideoDetailInfoBean createFromParcel(Parcel in) {
                return new VideoDetailInfoBean(in);
            }

            @Override
            public VideoDetailInfoBean[] newArray(int size) {
                return new VideoDetailInfoBean[size];
            }
        };
        /**
         * group_flags : 32832
         * video_type : 0
         * video_preloading_flag : 1
         * video_url : []
         * direct_play : 1
         * detail_video_large_image : {"url":"http://p3.pstatp.com/video1609/17600009fb1bb36ce3ee","width":580,"url_list":[{"url":"http://p3.pstatp.com/video1609/17600009fb1bb36ce3ee"},{"url":"http://pb9.pstatp.com/video1609/17600009fb1bb36ce3ee"},{"url":"http://pb1.pstatp.com/video1609/17600009fb1bb36ce3ee"}],"uri":"video1609/17600009fb1bb36ce3ee","height":326}
         * show_pgc_subscribe : 1
         * video_third_monitor_url :
         * video_id : d4715d18819e4acc8b3012374dd4588f
         * video_watching_count : 0
         * video_watch_count : 3502527
         */

        private int group_flags;
        private int video_type;
        private int video_preloading_flag;
        private int direct_play;
        private DetailVideoLargeImageBean detail_video_large_image;
        private int show_pgc_subscribe;
        private String video_third_monitor_url;
        private String video_id;
        private int video_watching_count;
        private int video_watch_count;
        private List<?> video_url;

        public VideoDetailInfoBean(Parcel in) {
            group_flags = in.readInt();
            video_type = in.readInt();
            video_preloading_flag = in.readInt();
            direct_play = in.readInt();
            detail_video_large_image = in.readParcelable(DetailVideoLargeImageBean.class.getClassLoader());
            show_pgc_subscribe = in.readInt();
            video_third_monitor_url = in.readString();
            video_id = in.readString();
            video_watching_count = in.readInt();
            video_watch_count = in.readInt();
        }

        public VideoDetailInfoBean() {
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(group_flags);
            dest.writeInt(video_type);
            dest.writeInt(video_preloading_flag);
            dest.writeInt(direct_play);
            dest.writeParcelable(detail_video_large_image, flags);
            dest.writeInt(show_pgc_subscribe);
            dest.writeString(video_third_monitor_url);
            dest.writeString(video_id);
            dest.writeInt(video_watching_count);
            dest.writeInt(video_watch_count);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public int getGroup_flags() {
            return group_flags;
        }

        public void setGroup_flags(int group_flags) {
            this.group_flags = group_flags;
        }

        public int getVideo_type() {
            return video_type;
        }

        public void setVideo_type(int video_type) {
            this.video_type = video_type;
        }

        public int getVideo_preloading_flag() {
            return video_preloading_flag;
        }

        public void setVideo_preloading_flag(int video_preloading_flag) {
            this.video_preloading_flag = video_preloading_flag;
        }

        public int getDirect_play() {
            return direct_play;
        }

        public void setDirect_play(int direct_play) {
            this.direct_play = direct_play;
        }

        public DetailVideoLargeImageBean getDetail_video_large_image() {
            return detail_video_large_image;
        }

        public void setDetail_video_large_image(DetailVideoLargeImageBean detail_video_large_image) {
            this.detail_video_large_image = detail_video_large_image;
        }

        public int getShow_pgc_subscribe() {
            return show_pgc_subscribe;
        }

        public void setShow_pgc_subscribe(int show_pgc_subscribe) {
            this.show_pgc_subscribe = show_pgc_subscribe;
        }

        public String getVideo_third_monitor_url() {
            return video_third_monitor_url;
        }

        public void setVideo_third_monitor_url(String video_third_monitor_url) {
            this.video_third_monitor_url = video_third_monitor_url;
        }

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }

        public int getVideo_watching_count() {
            return video_watching_count;
        }

        public void setVideo_watching_count(int video_watching_count) {
            this.video_watching_count = video_watching_count;
        }

        public int getVideo_watch_count() {
            return video_watch_count;
        }

        public void setVideo_watch_count(int video_watch_count) {
            this.video_watch_count = video_watch_count;
        }

        public List<?> getVideo_url() {
            return video_url;
        }

        public void setVideo_url(List<?> video_url) {
            this.video_url = video_url;
        }

        public static class DetailVideoLargeImageBean implements Parcelable {
            public static final Creator<DetailVideoLargeImageBean> CREATOR = new Creator<DetailVideoLargeImageBean>() {
                @Override
                public DetailVideoLargeImageBean createFromParcel(Parcel in) {
                    return new DetailVideoLargeImageBean(in);
                }

                @Override
                public DetailVideoLargeImageBean[] newArray(int size) {
                    return new DetailVideoLargeImageBean[size];
                }
            };
            /**
             * url : http://p3.pstatp.com/video1609/17600009fb1bb36ce3ee
             * width : 580
             * url_list : [{"url":"http://p3.pstatp.com/video1609/17600009fb1bb36ce3ee"},{"url":"http://pb9.pstatp.com/video1609/17600009fb1bb36ce3ee"},{"url":"http://pb1.pstatp.com/video1609/17600009fb1bb36ce3ee"}]
             * uri : video1609/17600009fb1bb36ce3ee
             * height : 326
             */

            private String url;
            private int width;
            private String uri;
            private int height;
            private List<MiddleImageBean> url_list;

            public DetailVideoLargeImageBean(Parcel in) {
                url = in.readString();
                width = in.readInt();
                uri = in.readString();
                height = in.readInt();
            }

            public DetailVideoLargeImageBean() {
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(url);
                dest.writeInt(width);
                dest.writeString(uri);
                dest.writeInt(height);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public List<MiddleImageBean> getUrl_list() {
                return url_list;
            }

            public void setUrl_list(List<MiddleImageBean> url_list) {
                this.url_list = url_list;
            }
        }
    }

    public static class LargeImageListBean {
        /**
         * url : http://p3.pstatp.com/video1609/17600009fb1bb36ce3ee
         * width : 580
         * url_list : [{"url":"http://p3.pstatp.com/video1609/17600009fb1bb36ce3ee"},{"url":"http://pb9.pstatp.com/video1609/17600009fb1bb36ce3ee"},{"url":"http://pb1.pstatp.com/video1609/17600009fb1bb36ce3ee"}]
         * uri : video1609/17600009fb1bb36ce3ee
         * height : 326
         */

        @SerializedName("url")
        private String urlX;
        private int width;
        private String uri;
        private int height;
        private List<MiddleImageBean> url_list;

        public String getUrlX() {
            return urlX;
        }

        public void setUrlX(String urlX) {
            this.urlX = urlX;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public List<MiddleImageBean> getUrl_list() {
            return url_list;
        }

        public void setUrl_list(List<MiddleImageBean> url_list) {
            this.url_list = url_list;
        }
    }

    public static class ImageListBean {
        /**
         * url : http://p1.pstatp.com/list/300x196/213b0003f2d1a191e4ff.webp
         * width : 496
         * url_list : [{"url":"http://p1.pstatp.com/list/300x196/213b0003f2d1a191e4ff.webp"},{"url":"http://pb3.pstatp.com/list/300x196/213b0003f2d1a191e4ff.webp"},{"url":"http://pb9.pstatp.com/list/300x196/213b0003f2d1a191e4ff.webp"}]
         * uri : list/213b0003f2d1a191e4ff
         * height : 279
         */

        @SerializedName("url")
        private String url;
        private int width;
        private String uri;
        private int height;
        private List<LargeImageListBean> url_list;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public List<LargeImageListBean> getUrl_list() {
            return url_list;
        }

        public void setUrl_list(List<LargeImageListBean> url_list) {
            this.url_list = url_list;
        }
    }
}