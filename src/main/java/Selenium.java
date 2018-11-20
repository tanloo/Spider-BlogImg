import org.openqa.selenium.*;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Selenium加载类
 * @author tanloo
 * @date 2018/11/19
 */
class Selenium {
    /**
     * 正则表达式，以提取字符串中数字
     */
    private static Pattern pattern = Pattern.compile("[^0-9]");

    /**
     * 获取博客所有列表URL
     * @return List<String>
     * @throws Exception
     */
    static List<String> getPageList() throws Exception {
        //加载phantomjs，路径自定义
        System.setProperty("phantomjs.binary.path", "D:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        DesiredCapabilities caps = new DesiredCapabilities();
        //设置访问代理
        String proxyIpAndPort = "127.0.0.1:1080";
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSocksProxy(proxyIpAndPort);
        caps.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
        caps.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
        caps.setCapability(CapabilityType.PROXY, proxy);

        WebDriver driver = new PhantomJSDriver(caps);
        driver.get("https://hornydragon.blogspot.com/search/label/%E9%9B%9C%E4%B8%83%E9%9B%9C%E5%85%AB%E7%9F%AD%E7%AF%87%E6%BC%AB%E7%95%AB%E7%BF%BB%E8%AD%AF?&max-results=30");
        System.out.println(driver.getTitle());
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Thread.sleep(2000);
        //获取最后一页页码
        WebElement element = driver.findElement(By.cssSelector(".displaypageNum.lastpage a"));
        String onclickSrt = element.getAttribute("onclick");
        int pageNum = Integer.parseInt(pattern.matcher(onclickSrt).replaceAll("").trim());
        System.out.println("Total Page num: " + pageNum);

        List<String> pageList = new ArrayList<>();
        for (int i = 2; i < pageNum + 1; i++) {
            //在网页中执行JS代码，此代码为跳转页面
            js.executeScript("redirectlabel(" + i + ");");
            //停5秒，以等待页面加载
            Thread.sleep(5000);
            pageList.add(driver.getCurrentUrl());
        }
        driver.close();
        driver.quit();
        return pageList;
    }

}
