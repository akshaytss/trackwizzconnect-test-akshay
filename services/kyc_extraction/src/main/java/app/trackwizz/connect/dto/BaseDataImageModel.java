package app.trackwizz.connect.dto;

public class BaseDataImageModel {
    public String data;
    public String image;

    public BaseDataImageModel() {
    }

    public BaseDataImageModel(String data, String image) {
        this.data = data;
        this.image = image;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
