package com.dreampany.framework.data.api.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.dreampany.framework.data.util.LogKit;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.LinkedList;

/**
 * Created by air on 10/11/17.
 */

public final class Storage {

    private static final String tag = Storage.class.getSimpleName();

    private static Storage instance;
    private final Context context;
    private String switchedDirectory;

    private Storage(Context context) {
        this.context = context.getApplicationContext();
        switchedDirectory = getExternalDirectory();
    }

    synchronized public static Storage onInstance(Context context) {
        if (instance == null) {
            if (context == null) {
                throw new IllegalArgumentException("Context should not be null");
            }
            instance = new Storage(context);
        }
        return instance;
    }

    public String getExternalDirectory() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public String getExternalDirectory(String publicDirectory) {
        return Environment.getExternalStoragePublicDirectory(publicDirectory).getAbsolutePath();
    }

    public String getInternalRootDirectory() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    public String getInternalFilesDirectory() {
        return context.getFilesDir().getAbsolutePath();
    }

    public String getInternalCacheDirectory() {
        return context.getCacheDir().getAbsolutePath();
    }

    public static boolean isExternalWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean switchExternalDirectory(String directory) {
        String parent = getExternalDirectory();
        String newPath = parent + File.separator + directory;
        boolean created = createDirectory(newPath);
        if (created) {
            switchedDirectory = newPath;
        }
        return created;
    }


    public boolean createDirectory(String path) {
        File directory = new File(path);
        if (directory.exists()) {
            LogKit.verbose(tag, "Directory '" + path + "' already exists");
            return false;
        }
        return directory.mkdirs();
    }



    public String createExternalDirectory(String folder) {
        String path = getExternalDirectory() + File.separator + folder;

        if (isDirectoryExists(path)) {
            return path;
        }

        if (createDirectory(path)) {
            return path;
        }
        return null;
    }

    public String createExternalDirectory(String folder, String subFolder) {
        String path = getExternalDirectory() + File.separator + folder + File.separator + subFolder;

        if (isDirectoryExists(path)) {
            return path;
        }

        if (createDirectory(path)) {
            return path;
        }
        return null;
    }

    public boolean createDirectory(String path, boolean override) {

        // Check if directory exists. If yes, then delete all directory
        if (override && isDirectoryExists(path)) {
            deleteDirectory(path);
        }

        // Create new directory
        return createDirectory(path);
    }

    public boolean deleteDirectory(String path) {
        return deleteDirectoryImpl(path);
    }

    public boolean isDirectoryExists(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    public boolean createFile(String path, String content) {
        return createFile(path, content.getBytes());
    }

    public boolean createFile(String path, Storable storable) {
        return createFile(path, storable.getBytes());
    }

    public boolean createFile(String path, byte[] content) {
        try {
            OutputStream stream = new FileOutputStream(new File(path));

            // encrypt if needed
/*            if (mConfiguration != null && mConfiguration.isEncrypted()) {
                content = encrypt(content, Cipher.ENCRYPT_MODE);
            }*/

            stream.write(content);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            LogKit.error(tag, "Failed create file " + e.toString());
            return false;
        }
        return true;
    }

    public boolean createFile(String path, Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return createFile(path, byteArray);
    }

    public boolean copy(String fromPath, String toPath) {
        File file = getFile(fromPath);
        if (!file.isFile()) {
            return false;
        }

        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(file);
            outStream = new FileOutputStream(new File(toPath));
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (Exception e) {
            LogKit.error(tag, "Failed copy " + e.toString());
            return false;
        } finally {
            closeSilently(inStream);
            closeSilently(outStream);
        }
        return true;
    }

    public boolean move(String fromPath, String toPath) {
        if (copy(fromPath, toPath)) {
            return getFile(fromPath).delete();
        }
        return false;
    }

    public File getFile(String path) {
        return new File(path);
    }

    public boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    public boolean isFileExist(String path) {
        return new File(path).exists();
    }

    public byte[] readFile(String path) {
        final FileInputStream stream;
        try {
            stream = new FileInputStream(new File(path));
            return readFile(stream);
        } catch (FileNotFoundException e) {
            LogKit.verbose(tag, "Failed to read file to input stream: " + e.toString());
            return null;
        }
    }

    protected byte[] readFile(final FileInputStream stream) {
        class Reader extends Thread {
            byte[] array = null;
        }

        Reader reader = new Reader() {
            public void run() {
                LinkedList<ImmutablePair<byte[], Integer>> chunks = new LinkedList<ImmutablePair<byte[], Integer>>();

                // read the file and build chunks
                int size = 0;
                int globalSize = 0;
                do {
                    try {
                        int chunkSize = /*mConfiguration != null ? mConfiguration.getChuckSize() : */8192;
                        // read chunk
                        byte[] buffer = new byte[chunkSize];
                        size = stream.read(buffer, 0, chunkSize);
                        if (size > 0) {
                            globalSize += size;

                            // add chunk to list
                            chunks.add(new ImmutablePair<byte[], Integer>(buffer, size));
                        }
                    } catch (Exception e) {
                        // very bad
                    }
                } while (size > 0);

                try {
                    stream.close();
                } catch (Exception e) {
                    // very bad
                }

                array = new byte[globalSize];

                // append all chunks to one array
                int offset = 0;
                for (ImmutablePair<byte[], Integer> chunk : chunks) {
                    // flush chunk to array
                    System.arraycopy(chunk.left, 0, array, offset, chunk.right);
                    offset += chunk.right;
                }
            }
        };

        reader.start();
        try {
            reader.join();
        } catch (InterruptedException e) {
            LogKit.verbose(tag,"Failed on reading file from storage while the locking Thread", e);
            return null;
        }

/*        if (mConfiguration != null && mConfiguration.isEncrypted()) {
            return encrypt(reader.array, Cipher.DECRYPT_MODE);
        } else {
            return reader.array;
        }*/
        return reader.array;
    }

    /**
     * Delete the directory and all sub content.
     *
     * @param path The absolute directory path. For example:
     *             <i>mnt/sdcard/NewFolder/</i>.
     * @return <code>True</code> if the directory was deleted, otherwise return
     * <code>False</code>
     */
    private boolean deleteDirectoryImpl(String path) {
        File directory = new File(path);

        // If the directory exists then delete
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files == null) {
                return true;
            }
            // Run on all sub files and folders and delete them
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectoryImpl(files[i].getAbsolutePath());
                } else {
                    files[i].delete();
                }
            }
        }
        return directory.delete();
    }

    private void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}
