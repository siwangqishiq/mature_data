package xyz.panyi;

import com.alibaba.fastjson.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import xyz.panyi.model.Mature;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CatchData {
    public static final String host="http://23hkk.com";
    public static final String UA = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31";

    public static List<Mature> matureList = new ArrayList<Mature>();

    public static void main(String[] args) throws IOException {
        matureList.clear();
        //parseContent("https://23hkk.com/?page=2");
        parse();
        //findVideoUrl("https://23hkk.com/?id=24");
        saveList();
    }

    public static void saveList(){
        String jsonString = JSON.toJSONString(matureList);
        System.out.println(jsonString);

        System.out.println("matureList size = " + matureList.size());

        System.out.println("save matureList start...");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(new File("list.json")));
            out.write(jsonString);
            out.close();
            System.out.println("save end!!!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parse() throws IOException {


        Document doc = Jsoup.connect(host).userAgent(UA).get();

        List<String> searchUrls = new ArrayList<String>();
        Set<String> urlSets = new HashSet<String>();
        searchUrls.add(host);
        urlSets.add(host);

        Elements pageList = doc.getElementsByClass("pagebar");
        System.out.println("pagebar info: ");
        Element contentElem = pageList.get(0);

        Elements list = contentElem.getElementsByTag("a");
        for(Element  pageElement :  list){
            System.out.println(pageElement.attr("href"));
            String url = pageElement.attr("href");
            if(!urlSets.contains(url)){
                urlSets.add(url);
                searchUrls.add(url);
            }
        }

        for(String url : searchUrls){
            parseContent(url);
        }
    }

    public static void parseContent(String url){
        System.out.println("抓取网页 : " + url);

        try {
            Document doc = Jsoup.connect(url).userAgent(UA).get();
            Elements elems = doc.getElementsByClass("main container");

            Element contentElem = elems.get(0);

            Elements liElems = contentElem.getElementsByTag("li");
            for(Element tagLi : liElems){
                //System.out.println(tagLi.html());
                Element aTag = tagLi.getElementsByTag("a").get(0);

                String href = aTag.attr("href");

                Element imgTag = aTag.getElementsByTag("img").get(0);
                String imgUrl = imgTag.attr("src");
                String title = imgTag.attr("alt");

                Mature mature = new Mature();
                matureList.add(mature);

                System.out.println(title);
                System.out.println(href);
                System.out.println(imgUrl);

                mature.setTitle(title);
                mature.setHref(href);
                mature.setImage(imgUrl);

                String videoSrc = findVideoUrl(href , mature);
                System.out.println(videoSrc);

                System.out.println("========================================================");
            }//end for each
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String findVideoUrl(String pageUrl , Mature mature){
        String videoUrl = "";
        try {
            Document doc = Jsoup.connect(pageUrl).userAgent(UA).get();

            Elements elements = doc.getElementsByTag("video");
            if(!elements.isEmpty()){
                Element videoTag = elements.first();
                String poster = videoTag.attr("poster");
                System.out.println("poster = " + poster);
                mature.setPoster(poster);

                Elements sourceElements = videoTag.getElementsByTag("source");
                if(sourceElements.isEmpty()){
                    System.out.println("pageUrl = " + pageUrl+"source");
                }else{
                    Element sourceTag = sourceElements.first();
                    videoUrl = sourceTag.attr("src");

                    System.out.println("videoUrl = " + videoUrl);
                    mature.setVideoUrl(videoUrl);
                }
            }else{
                System.out.println("pageUrl = " + pageUrl+"未发现video");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoUrl;
    }
}
