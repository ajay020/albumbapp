package project.gallery.com.createfolder;

import java.util.concurrent.atomic.AtomicInteger;

public class FolderModel {
    String folderName;
    private  Integer folderId;
    private static final AtomicInteger idGenerator  = new AtomicInteger(100);

    public FolderModel() {
        folderId = idGenerator.getAndIncrement();
    }

    public Integer getFolderId() {
        return folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
