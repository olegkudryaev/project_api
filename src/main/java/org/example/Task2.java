package org.example;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Task2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setAppletEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.waitForBackgroundJavaScript(100000);
        webClient.getOptions().setUseInsecureSSL(true);
        HtmlPage myPage = webClient.getPage("https://onlinesim.ru/price-list");

        Document doc = Jsoup.parse(myPage.asXml());

        Elements newsHeadlines = doc.select(".col-md-12.country-services.no-padding div h2 span");

        Map<String, Map<String, String>> countryMap = new HashMap<>();
        for (Element headline : newsHeadlines) {
            Map <String, String> serviceMap = new HashMap<>();
            Elements services = doc.select(".service-block");
            for (Element service : services) {
                Element price = service.select(".price-text").first();
                Element name = service.select(".price-name").first();

                serviceMap.put(name.text(), price.text());
            }
            countryMap.put(headline.text(), serviceMap);
        }
        JSONObject obj = new JSONObject(countryMap);
        System.out.println(obj);
    }
}
