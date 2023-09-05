package ua.zp.starinet.starinet_02072015;

/**
 * Created by root on 15.12.15.
 */
public class TicketsViewClass {
    public String dbCreated;
    public String dbText;
    public int dbAdminID;

    TicketsViewClass(String created, String text, int adminID) {
        this.dbCreated = created;
        this.dbText = text;
        this.dbAdminID = adminID;
    }
}
