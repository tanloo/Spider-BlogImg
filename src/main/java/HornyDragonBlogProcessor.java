import pojo.Image;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 好色龙博客图片爬虫，仅供学习<p/>
 * 博客地址：https://hornydragon.blogspot.com/<p/>
 * 需科学上网访问<p/>
 * 答疑邮箱：zxc5095@gmail.com
 *
 * @author tanloo
 * @date 2018/11/19
 */
public class HornyDragonBlogProcessor implements PageProcessor {
    private static final String URL_POST = "https://hornydragon\\.blogspot\\.com/\\d{4}/\\d{2}/\\d{1,5}[\\w]*\\.html";
    private static int INDEX_PHOTO = 0;
    /**
     * 正则表达式，以提取字符串中数字
     */
    private static Pattern pattern = Pattern.compile("[^0-9]");
    private List<String> pageURLList = new ArrayList<>();
    private Site site = Site
            .me()
            .setRetryTimes(3)
            .setCycleRetryTimes(3)
            .setSleepTime(1000)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    private void getPageURLList() throws Exception {
        pageURLList.addAll(Selenium.getPageList());
    }

    @Override
    public void process(Page page) {
        //列表页
        if (!page.getUrl().regex(URL_POST).match()) {
            List<String> detailURL = page.getHtml().xpath("//div[@class='blog-posts']").links().regex(URL_POST).all();
            System.out.println("size:" + detailURL.size());
            page.addTargetRequests(detailURL);
        } else {//文章页
            //提取文章编号
            int pageNo = Integer.parseInt(pattern.matcher(page.getHtml().xpath("//div[@class='post hentry']/h3/text()").toString()).replaceAll("").trim());
            //提取文章发布时间
            ZonedDateTime publishedTime = ZonedDateTime.parse(page.getHtml().xpath("//div[@class='post hentry']/meta/@content").toString());
            //提取文章内所有翻译图片链接
            List<String> photoURLs = page.getHtml().xpath("//table[@class='tr-caption-container']/tbody/tr/td/a")
                    .css("img", "src").all();
            photoURLs.addAll(page.getHtml().xpath("//div[@class='separator']/a").css("img", "src").all());
            String title = page.getHtml().xpath("//div[@class='post hentry']/h3/text()").toString();
            Matcher m = pattern.matcher(title);
            String pageURL = page.getUrl().toString();
            String pageYear = pageURL.split("/")[pageURL.split("/").length - 3];
            int pageMonth = Integer.parseInt(pageURL.split("/")[pageURL.split("/").length - 2]);
            try {
                //遍历单页内所有图片
                for (String s : photoURLs) {
                    //438之前的文章图片链接不带协议前缀，检测加上，不然URL类无法解析
                    if (s.startsWith("//")) {
                        s = "https:" + s;
                    }
                    String[] nickNames = s.split("/");
                    //存储到硬盘
                    SpiderDownload.download(s, nickNames[nickNames.length - 1], "F:\\HornyDragonBlogImage\\" + pageYear + "\\" + m.replaceAll("").trim() + "\\");
                    //持久化入数据库
                    SpiderDownload.save2DB(new Image(pageMonth, s, publishedTime, pageNo, Integer.parseInt(pageYear)));
                    System.out.println("第" + (INDEX_PHOTO++) + "张");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //仅加载一次全部列表
        if (pageURLList.isEmpty()) {
            try {
                getPageURLList();
                page.addTargetRequests(pageURLList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] arv) {
        Spider spider = Spider.create(new HornyDragonBlogProcessor())
                .addUrl("https://hornydragon.blogspot.com/search/label/%E9%9B%9C%E4%B8%83%E9%9B%9C%E5%85%AB%E7%9F%AD%E7%AF%87%E6%BC%AB%E7%95%AB%E7%BF%BB%E8%AD%AF?&max-results=30")
                .thread(300);
        //设置爬虫本地SSR代理
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1", 1080)));
        spider.setDownloader(httpClientDownloader);
        spider.run();
    }
}
