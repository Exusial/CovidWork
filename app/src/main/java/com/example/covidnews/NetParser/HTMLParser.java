package com.example.covidnews.NetParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class HTMLParser {
    public static ArrayList<String> parseHTML(String url){
        ArrayList<String> urls = new ArrayList<>();
        if(!url.endsWith(".htm"))
            return urls;
        try {
            Document doc = Jsoup.connect(url).get();
            String str = doc.toString();
            Elements content = doc.getElementsByTag("img");
            for(Element element : content){
                String text = element.attr("src");
                String[] s = text.split(":");
                if(s.length == 1 && !text.equals("") && !text.startsWith("../"))
                    urls.add(text);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return urls;
    }
}