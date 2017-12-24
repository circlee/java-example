package com.example;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class MyPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    @Override
    public void process(Page page) {
        System.out.println(page.getJson());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider spider = Spider.create(new MyPageProcessor());
        String str = String.format("https://wenku.baidu.com/content/fbbee96d26d3240c844769eae009581b6bd9bd85?m=9cd2b43f4c385f69fd4033937fe807e5&type=json&cn=952&_=1&t=1505712300&callback=wenku952");
        spider.addUrl(str);
        spider.thread(1).run();
    }
}
