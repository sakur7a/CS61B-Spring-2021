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

    /**  */
    private HashMap<String, String> pathToBlobId;


    /* TODO: fill in the rest of this class. */


    Commit(String message, Date date, List<String> parents, HashMap<String, String> pathToBlobId) {
        this.message = message;
        this.timestamp = getTimeStamp(date);
        this.parents = parents;
        this.pathToBlobId = pathToBlobId;
    }


    /** Add a commit */
    public static void createCommit(String message, Date d, List<String> parent, List<String> blobId)  {
        Commit it = new Commit(message, d, parent, blobId);
        it.saveCommit();
    }

    public void saveCommit() {
        File outFile = Utils.join(OBJECTS_DIR, this.getUid());
        writeObject(outFile, this);
    }

    /** Calculate the uid of each commit */
    public String getUid() {
        return sha1(serialize(this));
    }

    /** Convert the Date class to a format string */
    private static String getTimeStamp(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
         return formatter.format(date);
    }





}
