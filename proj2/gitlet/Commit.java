package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author sakura
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The timestamp of this Commit. */
    private String timestamp;
    /** A list of parent commit IDs. For merge commits, this will have more than one. */
    private List<String> parents;

    /** 键 (Key)：是一个 String，代表被跟踪文件的路径和文件名 (例如 'src/Main.java' 或者 'a.txt')。
     值 (Value)：也是一个 String，代表这个文件内容所对应的 Blob 对象的 SHA-1 ID。 */
    private HashMap<String, String> pathToBlobId;


    /* TODO: fill in the rest of this class. */


    Commit(String message, Date date, List<String> parents, HashMap<String, String> pathToBlobId) {
        this.message = message;
        this.timestamp = convertToFormatTimestamp(date);
        this.parents = parents;
        this.pathToBlobId = pathToBlobId;
    }

    public void save() {
        File outFile = Utils.join(OBJECTS_DIR, this.getUid());
        writeObject(outFile, this);
    }

    /** Calculate the uid of each commit */
    public String getUid() {
        return sha1(serialize(this));
    }

    /** Convert the Date class to a format string */
    public String convertToFormatTimestamp(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
         return formatter.format(date);
    }

    /** 检查Commit是否跟踪文件 */
    public boolean contains(String filename) {
        return this.pathToBlobId.containsKey(filename);
    }

    public HashMap<String, String> getPathToBlobId() {
        return this.pathToBlobId;
    }

    public List<String> getParents() {
        return parents;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // 打印信息
    public void print() {
        System.out.println("===");
        System.out.println("commit " + getUid());
        System.out.println("Date: " + timestamp);
        System.out.println(message);
        System.out.println();
    }
}
