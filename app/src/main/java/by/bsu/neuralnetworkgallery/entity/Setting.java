package by.bsu.neuralnetworkgallery.entity;

public class Setting {
    private String name;
    private int id;

    public Setting(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
