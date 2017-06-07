package at.sw2017.pocketdiary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Statistic;
import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBStatistic;

import static junit.framework.Assert.assertEquals;

/**
 * Created by marku on 29.05.2017.
 */

public class CreateStatisticUnitTest {

    DBHandler dbh;
    DBStatistic dbs;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dbs = new DBStatistic(context);
        TestHelper.initCategories(context);
    }

    @After
    public void tearDown() {
        dbh.close();
        dbs.close();
    }

    @Test
    public void testInsert(){
        String title = "Statistic";
        String term = "Search Term";
        int category_id = 1;
        Date date = new Date(2017,05,30);

        Statistic statistic = new Statistic();
        statistic.setTitle(title);
        statistic.setDateFrom(date);
        statistic.setDateUntil(date);
        statistic.setCategoryId(category_id);
        statistic.setSubCategoryId(category_id);
        statistic.setSearchTerm(term);

        dbs.insert(statistic);
        List<Statistic> all_stats = dbs.getData();
        Statistic db_stat = all_stats.get(0);

        String stat_title = db_stat.getTitle();
        Date stat_from = db_stat.getDateFrom();
        Date stat_until = db_stat.getDateUntil();
        int stat_main_cat = db_stat.getCategoryId();
        int stat_sub_cat = db_stat.getSubCategoryId();
        String stat_search_term = db_stat.getSearchTerm();

        assertEquals(title, stat_title);
        assertEquals(category_id, stat_main_cat);
        assertEquals(category_id, stat_sub_cat);
        assertEquals(term, stat_search_term);
    }

    @Test
    public void testUpdate(){
        testInsert();
        String title = "Statistic Update";
        String term = "Search Term Update";
        int category_id = 1;
        Date date = new Date(2017,04,30);

        Statistic statistic = new Statistic();
        statistic.setId(1);
        statistic.setTitle(title);
        statistic.setDateFrom(date);
        statistic.setDateUntil(date);
        statistic.setCategoryId(category_id);
        statistic.setSubCategoryId(category_id);
        statistic.setSearchTerm(term);

        dbs.update(statistic);
        List<Statistic> all_stats = dbs.getData();
        Statistic db_stat = all_stats.get(0);

        String stat_title = db_stat.getTitle();
        Date stat_from = db_stat.getDateFrom();
        Date stat_until = db_stat.getDateUntil();
        int stat_main_cat = db_stat.getCategoryId();
        int stat_sub_cat = db_stat.getSubCategoryId();
        String stat_search_term = db_stat.getSearchTerm();

        assertEquals(title, stat_title);
        assertEquals(category_id, stat_main_cat);
        assertEquals(category_id, stat_sub_cat);
        assertEquals(term, stat_search_term);
    }

    @Test
    public void getStatisticWithId(){
        String title = "Statistic";
        String term = "Search Term";
        int category_id = 1;
        Date date = new Date(2017,05,30);

        Statistic statistic = new Statistic();
        statistic.setTitle(title);
        statistic.setDateFrom(date);
        statistic.setDateUntil(date);
        statistic.setCategoryId(category_id);
        statistic.setSubCategoryId(category_id);
        statistic.setSearchTerm(term);

        dbs.insert(statistic);
        Statistic db_stat = dbs.getStatistic(1);

        String stat_title = db_stat.getTitle();
        Date stat_from = db_stat.getDateFrom();
        Date stat_until = db_stat.getDateUntil();
        int stat_main_cat = db_stat.getCategoryId();
        int stat_sub_cat = db_stat.getSubCategoryId();
        String stat_search_term = db_stat.getSearchTerm();

        assertEquals(title, stat_title);
        assertEquals(category_id, stat_main_cat);
        assertEquals(category_id, stat_sub_cat);
        assertEquals(term, stat_search_term);
    }
}
