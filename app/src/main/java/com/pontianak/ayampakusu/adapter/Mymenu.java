package com.pontianak.ayampakusu.adapter;

public class Mymenu {
    public int action;
    public String image;
    public String title;
    public String id_menu;

    public Mymenu(int action, String image, String title) {
        this.action = action;
        this.image = image;
        this.title = title;
        this.id_menu = "0";
    }

    public Mymenu(int action, String image, String title, String id_menu) {
        this.action = action;
        this.image = image;
        this.title = title;
        this.id_menu = id_menu;
    }
}
