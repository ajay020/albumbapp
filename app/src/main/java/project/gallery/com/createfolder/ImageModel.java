package project.gallery.com.createfolder;

public class ImageModel {
    String imagePath;
    boolean isChecked;

    ImageModel(String imagePath,boolean isChecked){
        this.imagePath = imagePath;
        this.isChecked = isChecked;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
