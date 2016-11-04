/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mockcampaignrunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class MockCampaignRunner {

    private static String select_sql;
    private static String update_sql;
    private static String insert_sql;
    private static DataSource dataSource;
    private static final String driverClassName = "oracle.jdbc.driver.OracleDriver";
    private static final String url = "jdbc:oracle:thin:@//localhost:1521/orcl";
    private static final String dbUsername = "novadb_user";
    private static final String dbPassword = "oracle";

    private static void initState(String file) {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            select_sql = br.readLine();
            insert_sql = br.readLine();
            update_sql = br.readLine();
        } catch (IOException | NumberFormatException e) {
            System.err.println(e.getMessage());
        }
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        ds.setUsername(dbUsername);
        ds.setPassword(dbPassword);
        dataSource = ds;
    }

    public static long setTest() {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update("DELETE FROM DG_RE_INTERACTION");
        template.update("UPDATE DG_RE_TARGETLIST SET current_state = 0");
        long now = System.nanoTime();

        List<Map<String, Object>> data = template.queryForList(select_sql);
        System.out.println("Retrieved " + data.size() + " contacts");
        template.update(insert_sql);
        template.update(update_sql);

        long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - now);
        System.out.println(duration + " milliseconds");
        return duration;
    }

    private final static String UPDATE_STMT = "Update dg_re_targetlist set "
            + "current_component_name = ?,current_state = ?,"
            + "last_action_date = ? "
            + "where campaign_id = ? and contact_id = ? and current_state = 0 "
            + "and to_char(current_timestamp at time zone 'America/Los_Angeles', 'HH24:MI') > substr('11:00:00.000', 1, 5) "
            + "and to_char(current_date, 'MM/DD/YYYY') = to_char( to_timestamp('11/02/2016' || '11:00:00.000', 'MM/DD/YYYY HH24:MI:SS.FF') at time zone 'America/Los_Angeles', 'MM/DD/YYYY')";
    private final static int[] UPD_TYPES = new int[]{Types.VARCHAR,
        Types.NUMERIC, Types.TIMESTAMP, Types.NUMERIC, Types.NUMERIC,};
    private final static String CURRENT_COMPONENT_NAME = "Email Activity";
    private final static Long CURRENT_STATE = 1l;
    private final static String TIME_ZONE = "America/Los_Angeles";
    private final static String TIME = "11:00:00.000";
    private final static String Date = "11/02/2016";

    private static void updateContact(JdbcTemplate template, Long campaignId, Long contactId) {
        Object[] dataToInsert = {CURRENT_COMPONENT_NAME, CURRENT_STATE,
            DateTime.now(DateTimeZone.UTC).toDate(), campaignId, contactId
        };
        template.update(UPDATE_STMT, dataToInsert, UPD_TYPES);
    }

    private final static String INSERT_STMT = "INSERT INTO DG_RE_INTERACTION"
            + " (PROCESS_NAME, PROCESS_ID, CONTACT_ID, AGENT_NAME, COMPONENT_NAME, ATOMIC_ACTIVITY_NAME,"
            + "  ACTION_NAME, OBJECT_NAME, ATTRIBUTE_NAME, ATTRIBUTE_VALUE, COMPONENT_STATE,"
            + "  ENDSTATE, ACTION_DATE, VERSION, ID)"
            + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, DG_RE_INTERACTIONID_SEQ.nextVal )";
    private final static int[] SQL_TYPES = new int[]{Types.VARCHAR,
        Types.NUMERIC, Types.NUMERIC, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.NUMERIC,
        Types.NUMERIC, Types.TIMESTAMP, Types.NUMERIC
    };
    private final static String PROCESS_NAME = "Campaign";
    private final static String AGENT_NAME = "Apple";
    private final static String COMPONENT_NAME = "Email Activity";
    private final static String ATOMIC_ACTIVITY_NAME = "A1";
    private final static String ACTION_NAME = "Notify";
    private final static String OBJECT_NAME = "Email";
    private final static String ATTRIBUTE_NAME = "Content";
    private final static String ATTRIBUTE_VALUE = "5322";
    private final static Long COMPONENT_STATE = 1l;
    private final static Long END_STATE = 1l;
    private final static Long HIBERNATE_VERSION = 1l;

    private static void insertInteraction(JdbcTemplate template, Long campaignId, Long contactId) throws DataAccessException {
        Object[] dataToInsert = {PROCESS_NAME, campaignId, contactId, AGENT_NAME,
            COMPONENT_NAME, ATOMIC_ACTIVITY_NAME, ACTION_NAME, OBJECT_NAME, ATTRIBUTE_NAME,
            ATTRIBUTE_VALUE, COMPONENT_STATE, END_STATE,
            DateTime.now(DateTimeZone.UTC).toDate(), HIBERNATE_VERSION
        };
        template.update(INSERT_STMT, dataToInsert, SQL_TYPES);
    }

    public static long singletonTest() {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update("DELETE FROM DG_RE_INTERACTION");
        template.update("UPDATE DG_RE_TARGETLIST SET current_state = 0");
        long now = System.nanoTime();

        List<Map<String, Object>> contacts = template.queryForList(select_sql);
        System.out.println("Retrieved " + contacts.size() + " contacts");
        for (Map<String, Object> contact : contacts) {
            Long campaignID = Long.parseLong(contact.get("CAMPAIGN_ID").toString());
            Long contactID = Long.parseLong(contact.get("CONTACT_ID").toString());
            insertInteraction(template, campaignID, contactID);
            updateContact(template, campaignID, contactID);
        }

        long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - now);
        System.out.println(duration + " milliseconds");
        return duration;
    }

    public static long bulkTest() {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update("DELETE FROM DG_RE_INTERACTION");
        template.update("UPDATE DG_RE_TARGETLIST SET current_state = 0");
        long now = System.nanoTime();

        List<Map<String, Object>> contacts = template.queryForList(select_sql);
        System.out.println("Retrieved " + contacts.size() + " contacts");
        int[] insertCounts = template.batchUpdate(INSERT_STMT,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Map<String, Object> contact = contacts.get(i);
                        Long campaignID = Long.parseLong(contact.get("CAMPAIGN_ID").toString());
                        Long contactID = Long.parseLong(contact.get("CONTACT_ID").toString());
                        ps.setString(1, PROCESS_NAME);
                        ps.setLong(2, campaignID);
                        ps.setLong(3, contactID);
                        ps.setString(4, AGENT_NAME);
                        ps.setString(5, COMPONENT_NAME);
                        ps.setString(6, ATOMIC_ACTIVITY_NAME);
                        ps.setString(7, ACTION_NAME);
                        ps.setString(8, OBJECT_NAME);
                        ps.setString(9, ATTRIBUTE_NAME);
                        ps.setString(10, ATTRIBUTE_VALUE);
                        ps.setLong(11, COMPONENT_STATE);
                        ps.setLong(12, END_STATE);
                        java.sql.Timestamp t = new Timestamp(System.currentTimeMillis());
                        ps.setTimestamp(13, t);
                        ps.setLong(14, HIBERNATE_VERSION);
                    }

                    public int getBatchSize() {
                        return contacts.size();
                    }

                });

        int[] updateCounts = template.batchUpdate(UPDATE_STMT,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Map<String, Object> contact = contacts.get(i);
                        Long campaignID = Long.parseLong(contact.get("CAMPAIGN_ID").toString());
                        Long contactID = Long.parseLong(contact.get("CONTACT_ID").toString());
                        ps.setString(1, CURRENT_COMPONENT_NAME);
                        ps.setLong(2, CURRENT_STATE);
                        ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                        ps.setLong(4, campaignID);
                        ps.setLong(5, contactID);
                    }

                    public int getBatchSize() {
                        return contacts.size();
                    }
                }
        );
        long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - now);
        System.out.println(duration + " milliseconds");
        System.out.println("insertCounts=" + insertCounts + ", updateCounts=" + updateCounts);
        return duration;
    }

    public static void main(String[] args) throws Exception {

        //initState(args[0]);
        int numTests = 10;
        long[] times = new long[numTests];
        for (int i = 0; i < numTests; i++) {
            initState(args[0]);
            //setTest();
            //singletonTest();
            times[i] = singletonTest();
        }
        stats(times);
    }

    private static void stats(long[] times) {
        long median = times[times.length / 2];
        long max = times[0];
        long min = times[0];
        long mean = 0;
        for (long time : times) {
            mean += time;
            if (time > max) {
                max = time;
            }
            if (time < min) {
                min = time;
            }
        }
        mean /= times.length;
        System.out.println("median=" + median + ", min=" + min + ", max=" + max + ", mean=" + mean);
    }
}
