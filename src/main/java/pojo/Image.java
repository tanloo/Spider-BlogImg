package pojo;

import java.time.ZonedDateTime;


/**
 * @author tanloo
 * @date 2018/11/19
 */
public class Image {
    private int month;
    private String imgURL;
    private ZonedDateTime publishedTime;
    private int titleNum;
    private int year;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Image() {
    }

    public Image(int month, String imgURL, ZonedDateTime publishedTime, int titleNum, int year) {
        this.month = month;
        this.imgURL = imgURL;
        this.publishedTime = publishedTime;
        this.titleNum = titleNum;
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public ZonedDateTime getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(ZonedDateTime publishedTime) {
        this.publishedTime = publishedTime;
    }

    public int getTitleNum() {
        return titleNum;
    }

    public void setTitleNum(int titleNum) {
        this.titleNum = titleNum;
    }
}
