package com.sarahdev.chinesecheckers.persistance;

import android.content.Context;

import com.sarahdev.chinesecheckers.model.DataModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Files {
    private final static String FILENAME="chineseCheckers.txt";
    private final static String FILENAME_TMP = "ccTmp.txt";

    public static synchronized DataModel readFile(final Context context){
        DataModel dataModel = new DataModel();
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            dataModel = (DataModel) ois.readObject();
        } catch (IOException | ClassNotFoundException ignore) {
        }
        return dataModel;
    }

    public static synchronized void writeFile(final Context context, final DataModel dataModel) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME_TMP, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dataModel);
            oos.close();
            fos.close();
            renameAppFile(context, FILENAME_TMP, FILENAME);
        } catch (IOException ignore) {
        }
    }

    public static synchronized void renameAppFile(final Context context, String tmpFilename, String filename) {
        File originalFile = context.getFileStreamPath(tmpFilename);
        File newFile = new File(originalFile.getParent(), filename);
        if (newFile.exists())
            context.deleteFile(filename);
        originalFile.renameTo(newFile);
    }
}
