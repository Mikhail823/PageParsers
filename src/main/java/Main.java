import model.SitePage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sitemap.SiteMapAction;
import utils.HibernateUtil;

import java.util.concurrent.ForkJoinPool;

public class Main {
    public  static SiteMapAction siteMapAction = new SiteMapAction();
    public static void main(String[] args) {
        String url = "https://www.playback.ru";

        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        forkJoinPool.invoke(new SiteMapAction(url));
        saveInDB();
    }
    public static void saveInDB(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        for (SitePage page : siteMapAction.getPageList()){
            session.save(page);
        }
        transaction.commit();
        session.close();
    }
}
