package by.bsu.neuralnetworkgallery.entity;

import java.util.Objects;

public class Style {
    private String id;
    private String title;
    private String url;
    private String description;

    public Style(){}

    public Style(String id, String title, String url, String description) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Style style = (Style) o;
        return Objects.equals(id, style.id) &&
                Objects.equals(title, style.title) &&
                Objects.equals(url, style.url) &&
                Objects.equals(description, style.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, url, description);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Style{");
        sb.append("id='").append(id).append('\'');
        sb.append(", Title='").append(title).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static class Builder{
        private Style style;

        public Builder(){
            style = new Style();
        }

        public Builder withId(String id){
            style.setId(id);
            return this;
        }

        public Builder withTitle(String title){
            style.setTitle(title);
            return this;
        }

        public Builder withUrl(String url){
            style.setUrl(url);
            return this;
        }

        public Builder withDescription(String description){
            style.setDescription(description);
            return this;
        }

        public Style build(){
            return style;
        }
    }
}
