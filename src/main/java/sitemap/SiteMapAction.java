package sitemap;

import model.SitePage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.HibernateUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveAction;

import static utils.HibernateUtil.getSessionFactory;

public class SiteMapAction extends RecursiveAction {
    private String url;
    private Connection.Response response = null;
    private List<SitePage> pageList = Collections.synchronizedList(new ArrayList<>());
    private Set<String> urlsSet = Collections.synchronizedSet(new HashSet<>());
    private HibernateUtil hibernateUtil;

    public SiteMapAction(){}

    public SiteMapAction(String url){
        this.url = url;
    }

    @Override
    protected void compute(){
        try {
            List<String> list = new ArrayList<>(getParserSite(url));
            List<SiteMapAction> tasks = new ArrayList<>();

            for (String child : list){
                SiteMapAction siteMapAction = new SiteMapAction(child);
                tasks.add(siteMapAction);
                siteMapAction.fork();
            }

            for (SiteMapAction task : tasks){
                task.join();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private Set<String> getParserSite(String url) throws IOException, InterruptedException {
        Document doc = getConnection(url).parse();
        Elements links = doc.select("a");

        for (Element link : links){
            String absUrl = link.attr("abs:href");
            if (absUrl.contains(url)){
                continue;
            }
            if (absUrl.startsWith(url) && absUrl.startsWith(".html")){
                String childUrl = absUrl.replaceAll(url, "")
                        .replaceAll("#", "")
                        .trim();
                pageList.add(new SitePage(childUrl, getConnection(absUrl).statusCode(), link.html()));
                urlsSet.add(childUrl);
            }
            urlsSet.remove(url);
        }

       return urlsSet;
    }

    private Connection.Response getConnection(String url) throws InterruptedException, IOException {
        Thread.sleep(500);
        return response = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                .referrer("www.google.com").execute();
    }

    public List<SitePage> getPageList() {
        return pageList;
    }

}
