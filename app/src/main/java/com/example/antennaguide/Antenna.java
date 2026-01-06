package com.example.antennaguide;

public class Antenna {
    private String id;
    private String name;
    private String shortDesc;
    private String fullDesc;
    private String principle;
    private String application;
    private int iconResId;

    public Antenna(String id, String name, String shortDesc, String fullDesc,
                   String principle, String application, int iconResId) {
        this.id = id;
        this.name = name;
        this.shortDesc = shortDesc;
        this.fullDesc = fullDesc;
        this.principle = principle;
        this.application = application;
        this.iconResId = iconResId;
    }

    // Геттеры
    public String getId() { return id; }
    public String getName() { return name; }
    public String getShortDesc() { return shortDesc; }
    public String getFullDesc() { return fullDesc; }
    public String getPrinciple() { return principle; }
    public String getApplication() { return application; }
    public int getIconResId() { return iconResId; }
}
