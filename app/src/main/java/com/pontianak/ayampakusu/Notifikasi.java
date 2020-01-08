package com.pontianak.ayampakusu;

public class Notifikasi {
    public static final String TABLE_NAME = "notifikasi";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE_NOTIF = "title_notif";
    public static final String COLUMN_CONTENT_NOTIF = "content_notif";
    public static final String COLUMN_LOGO_NOTIF = "logo_notif";
    public static final String COLUMN_ACTION_NOTIF = "action_notif";
    public static final String COLUMN_ID_ACTION = "id_action_notif";



    int id;
    String title_notif;
    String content_notif;
    String logo_notif;
    String action_notif;
    String id_action_notif;


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE_NOTIF + " TEXT,"
                    + COLUMN_CONTENT_NOTIF + " TEXT,"
                    + COLUMN_LOGO_NOTIF + " TEXT,"
                    + COLUMN_ACTION_NOTIF + " TEXT,"
                    + COLUMN_ID_ACTION + " TEXT"
                    + ")";

    public Notifikasi(int id, String title_notif, String content_notif, String logo_notif, String action_notif, String id_action_notif) {
        this.id = id;
        this.title_notif = title_notif;
        this.content_notif = content_notif;
        this.logo_notif = logo_notif;
        this.action_notif = action_notif;
        this.id_action_notif = id_action_notif;
    }

    public Notifikasi() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle_notif() {
        return title_notif;
    }

    public void setTitle_notif(String title_notif) {
        this.title_notif = title_notif;
    }

    public String getContent_notif() {
        return content_notif;
    }

    public void setContent_notif(String content_notif) {
        this.content_notif = content_notif;
    }

    public String getLogo_notif() {
        return logo_notif;
    }

    public void setLogo_notif(String logo_notif) {
        this.logo_notif = logo_notif;
    }

    public String getAction_notif() {
        return action_notif;
    }

    public void setAction_notif(String action_notif) {
        this.action_notif = action_notif;
    }

    public String getId_action_notif() {
        return id_action_notif;
    }

    public void setId_action_notif(String id_action_notif) {
        this.id_action_notif = id_action_notif;
    }
}
