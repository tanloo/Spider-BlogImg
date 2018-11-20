# Spider-BlogImg
### 利用WebMagic爬取博客全部所需图片，将其保存本地并将图片信息导入数据库
### 爬取的博客为 [好色龍的網路觀察日誌](https://hornydragon.blogspot.com/)之雜七雜八短篇漫畫翻譯

##### *超級喜欢他的漫画翻译，但是墙内不太好访问，正好在学习爬虫，将其~~试炼~~一下，也为了怕有一天突然访问不到了留个念想。*
##### 代码新手，很多地方写得并不是很好，望提出指正~~例如数据库简陋地使用了Mybatis，重复的创建关闭连接自己都觉得蠢~~
---
#### 所使用到的框架：
##### 1. WebMagic v0.7.3
+ [官网](http://webmagic.io/)
+ [github](https://github.com/code4craft/webmagic)
##### 2. Selenium v3.141.59
+ [官网](https://www.seleniumhq.org)
##### 3.PhantomJS v2.1.1
+ [官网](http://phantomjs.org/)

#### 数据库:
+ MySQL v8.0
---
#### How to use:<br>
run HornyDragonBlogProcessor.java

if have not database,you shoud commented out code:`SpiderDownload.save2DB(...)`
