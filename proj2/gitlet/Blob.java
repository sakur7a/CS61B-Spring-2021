package gitlet;

import java.io.File;
import java.io.Serializable;
import static gitlet.Repository.OBJECTS_DIR;
import static gitlet.Utils.*;

public class Blob implements Serializable {
    private String uid;

    private byte[] bytes;

    private File fileName;

    /**
     * 从文件内容创建一个 Blob.
     * @param bytes 文件的字节数组内容
     */
    public Blob(byte[] bytes) {
        this.bytes = bytes;
        this.uid = sha1(bytes);
    }

    public void save() {
        File blobFile = join(OBJECTS_DIR, uid);
        if (!blobFile.exists()) {
            writeContents(blobFile, bytes);
        }
    }

    public String getUid() {
        return uid;
    }


}
