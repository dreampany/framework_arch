package com.dreampany.framework.data.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.litesuits.common.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by nuc on 12/31/2016.
 */

public final class FileUtil {
    private FileUtil() {
    }


    public static long getFileHash(String path) {
        File file = new File(path);
        return DataUtil.getSha256(file);
    }

    public static long getFileSize(String path) {
        File file = new File(path);
        return file.length();
    }

    public static boolean isFileExists(String path) {
        if (TextUtils.isEmpty(path)) return false;
        return new File(path).exists();
    }

    public static boolean recursiveDelete(String uri) {
        File file = new File(uri);
        if (file.exists()) {
            //check if the file is a directory
            if (file.isDirectory()) {
                String[] files = file.list();
                if (files.length > 0) {
                    for (String s : files) {
                        //call deletion of file individually
                        recursiveDelete(uri + System.getProperty("file.separator") + s);
                    }
                }
            }

            file.setWritable(true);
            boolean result = file.delete();
            return result;
        } else {
            return false;
        }
    }


    public static String getMimeType(Context context, Uri uri) {

        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            return cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
    }

    /**
     * @param context
     * @param fileUri
     * @return mimeType
     */
    public static String getMimeType(Context context, String fileUri) {

        Uri uri = Uri.fromFile(new File(fileUri));

        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            return cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
    }

    public static String getMimeType(String path) {
        File file = new File(path);
        Uri selectedUri = Uri.fromFile(file);
        String fileExtension
                = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
        String mimeType
                = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        return mimeType;
    }


    /**
     * Determines the MIME baseEnum for a given filename.
     *
     * @param filename The file to determine the MIME baseEnum of.
     * @return The MIME baseEnum of the file, or a wildcard if none could be
     * determined.
     */
    public static String getType(final String filename) {
        // There does not seem to be a way to ask the OS or file itself for this
        // information, so unfortunately resorting to extension sniffing.

        int pos = filename.lastIndexOf('.');
        if (pos != -1) {
            String ext = filename.substring(filename.lastIndexOf('.') + 1,
                    filename.length());

            if (ext.equalsIgnoreCase("mp3"))
                return "audio/mpeg";
            if (ext.equalsIgnoreCase("aac"))
                return "audio/aac";
            if (ext.equalsIgnoreCase("wav"))
                return "audio/wav";
            if (ext.equalsIgnoreCase("ogg"))
                return "audio/ogg";
            if (ext.equalsIgnoreCase("mid"))
                return "audio/midi";
            if (ext.equalsIgnoreCase("midi"))
                return "audio/midi";
            if (ext.equalsIgnoreCase("wma"))
                return "audio/x-ms-wma";

            if (ext.equalsIgnoreCase("mp4"))
                return "video/mp4";
            if (ext.equalsIgnoreCase("avi"))
                return "video/x-msvideo";
            if (ext.equalsIgnoreCase("wmv"))
                return "video/x-ms-wmv";

            if (ext.equalsIgnoreCase("png"))
                return "image/png";
            if (ext.equalsIgnoreCase("jpg"))
                return "image/jpeg";
            if (ext.equalsIgnoreCase("jpe"))
                return "image/jpeg";
            if (ext.equalsIgnoreCase("jpeg"))
                return "image/jpeg";
            if (ext.equalsIgnoreCase("gif"))
                return "image/gif";

            if (ext.equalsIgnoreCase("xml"))
                return "text/xml";
            if (ext.equalsIgnoreCase("txt"))
                return "text/plain";
            if (ext.equalsIgnoreCase("cfg"))
                return "text/plain";
            if (ext.equalsIgnoreCase("csv"))
                return "text/plain";
            if (ext.equalsIgnoreCase("conf"))
                return "text/plain";
            if (ext.equalsIgnoreCase("rc"))
                return "text/plain";
            if (ext.equalsIgnoreCase("htm"))
                return "text/html";
            if (ext.equalsIgnoreCase("html"))
                return "text/html";

            if (ext.equalsIgnoreCase("pdf"))
                return "application/pdf";
            if (ext.equalsIgnoreCase("apk"))
                return "application/vnd.android.package-archive";

            // Additions and corrections are welcomed.
        }
        return "*/*";
    }


    private static long getRemainingSize(int packetId, long size, int maxPacketSize) {
        long offset = getOffset(packetId, maxPacketSize);
        return size - offset;
    }

    private static long getOffset(int packetId, int maxPacketSize) {
        if (packetId <= 0)
            return -1;
        return (packetId - 1) * maxPacketSize;
    }

    public static boolean hasPacket(int packetId, long size, int maxPacketSize) {
        return FileUtil.getRemainingSize(packetId, size, maxPacketSize) > 0L;
    }

    public static int maxPacketId(long size, int maxPacketSize) {
        int div = (int) (size / maxPacketSize);
        int modular = (int) (size % maxPacketSize);
        return div + (modular > 0 ? 1 : 0);
    }

    public static int getPercentage(int packetId, long size, int maxBytes) {
        float ratio = (float) (packetId * maxBytes) / size;

        int percentage = (int) (ratio * 100);

        if (percentage > 100) {
            percentage = 100;
        }

        return percentage;
    }

    public static int getPercentage(long packetSize, long size) {
        float ratio = (float) packetSize / size;

        int percentage = (int) (ratio * 100);

        if (percentage > 100) {
            percentage = 100;
        }

        return percentage;
    }

    public static int getPacketCount(long size, int maxBytes) {
        return (int) ((size / maxBytes) + (size % maxBytes > 0 ? 1 : 0));
    }

    public static byte[] readPacket(int packetId, String uri, long size, int maxBytes) {
        try {
            long remainingSize = FileUtil.getRemainingSize(packetId, size, maxBytes);

            if (remainingSize > 0L) {

                int packetSize = maxBytes;
                long offset = FileUtil.getOffset(packetId, maxBytes);

                if (remainingSize < packetSize) {
                    packetSize = (int) remainingSize;
                }

                ByteBuffer buffer = ByteBuffer.allocate(packetSize);
                FileChannel fileChannel = new RandomAccessFile(uri, "r").getChannel();
                int readBytes = fileChannel.read(buffer, offset);
                fileChannel.close();
                return buffer.array();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return null;
    }

    public static void writePacket(int packetId, byte[] packetData, String uri, int maxBytes) {

        try {
/*            if (packet.getParentUri() == null) return 0L;*/

            File file = new File(uri);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            long offset = FileUtil.getOffset(packetId, maxBytes);

            randomAccessFile.seek(offset);
            randomAccessFile.write(packetData);
            long totalBytesWritten = randomAccessFile.length();

            randomAccessFile.close();

//            return totalBytesWritten;

/*            ByteBuffer buffer = ByteBuffer.wrap(packetData);
            File file = new File(uri);
            FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();

            long offset = FileUtil.getOffset(packetId, maxBytes);

            //fileChannel.position(offset);
            fileChannel.writeEntry(buffer, offset);
            long totalBytesWritten = fileChannel.size();

            fileChannel.close();*/


        } catch (IOException ioException) {
            ioException.printStackTrace();
            //          return 0L;
        }
    }

    /* Checks if external storage is available for read and writeEntry */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File makeDir(File parent, String child) {
        File file = new File(parent, child);
        if (file.exists() && file.isDirectory()) return file;
        if (file.mkdirs()) {
            return file;
        }
        return null;
    }

    public static File makeExternalDir(String name) {
        File dir = makeDir(Environment.getExternalStorageDirectory(), name);
        if (dir != null && dir.isDirectory()) {
            return dir;
        }
        return null;
    }

    private static File makeExternalDownloadDir(String dir) {
        return makeDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), dir);
    }


    public static File makeDir(String parentDir, String childDir) {
        File publicDir = makeExternalDownloadDir(parentDir);
        File dir = makeDir(publicDir, childDir);
        if (!dir.mkdirs()) {
            LogKit.error("Directory not created " + dir.toString());
        }
        return dir;
    }

    public static File makeFile(File dir, String name) {
        File file = new File(dir, name);
        if (file.exists() && file.isFile()) return file;

        try {
            if (file.createNewFile()) {
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String resolveFilePath(File dir, String name) {
        File file = new File(dir, name);
        return file.getAbsolutePath();
    }

/*    public static File makeFile(File dir, String name) {
        File file = new File(dir, name);
        if (file.exists()) {
            // nothing to create.
            return file;
        }

        try {
            if (file.createNewFile()) {
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

       /* boolean b=true;
        // Try with Storage Access Framework.
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP && FileUtil.isOnExtSdCard(file, context)) {
            DocumentFile document = getDocumentFile(file.getParentFile(), true,context);
            // getDocumentFile implicitly creates the directory.
            try {
                b=document.createFile(MimeTypes.getMimeType(file),file.getName())!=null;
                return b;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return null;
    }
*/

    public static FileChannel getReadChannel(String uri) {
        try {
            File file = new File(uri);
            return new RandomAccessFile(file, "r").getChannel();
            //return randomAccessFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static FileChannel getWriteChannel(String uri) {
        try {
            File file = new File(uri);
            return new RandomAccessFile(file, "rw").getChannel();
            //return randomAccessFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void scanFile(Context context, String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(context.getApplicationContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } else {
            Uri contentUri = Uri.fromFile(new File(path));
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
            context.getApplicationContext().sendBroadcast(mediaScanIntent);
        }
    }

    public static String getNameWithExtension(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static String getFileName(Context context, Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("file")) {
            fileName = uri.getLastPathSegment();
        } else {
            Cursor cursor = null;
            try {
                cursor = context.getApplicationContext().getContentResolver().query(uri, new String[]{
                        OpenableColumns.DISPLAY_NAME
                }, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    LogKit.verbose("name is " + fileName);
                }
            } finally {

                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        if (fileName == null) {
            fileName = new File(uri.toString()).getName();
        }

        return fileName;
    }

    public static File getFile(String uri) {
        return new File(uri);
    }

    public static byte[] getFileData(String path) {
        try {
            return FileUtils.readFileToByteArray(getFile(path));
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] readAssets(Context context, String path) {
        try {
            InputStream stream = context.getApplicationContext().getAssets().open(path);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            return buffer;
        } catch (IOException e) {
            return null;
        }
    }

    public static List<String> readAssetsAsStrings(Context context, String path) {
        try {
            InputStream stream = context.getApplicationContext().getAssets().open(path);
            Scanner scanner = new Scanner(stream);
            List<String> items = new ArrayList<>();

            while (scanner.hasNextLine()) {
                items.add(scanner.nextLine());
            }
            return items;
        } catch (IOException e) {
            return null;
        }
    }

    public static String[] readFilesFromAssets(Context context, String dir) {
        try {
            return context.getApplicationContext().getAssets().list(dir);
        } catch (IOException e) {
            return null;
        }
    }

    public static String appendForPath(String... paths) {
        StringBuilder builder = new StringBuilder();

        for (String path : paths) {
            if (builder.length() > 0) {
                builder.append(File.separator);
            }
            builder.append(path);
        }

        return builder.toString();
    }

    ////////android-simple-storage//////////////////////////////////////////////////////////////////////////////////////////
/*    private static Storage storage;

    private static Storage getStorage(Context context) {
        if (storage == null) {
            if (SimpleStorage.isExternalStorageWritable()) {
                storage = SimpleStorage.getExternalStorage();
            } else {
                storage = SimpleStorage.getInternalStorage(context.getApplicationContext());
            }
        }
        return storage;
    }

    public static void deleteFile(Context context, String uri) {
        Storage storage = getStorage(context);

    }*/

}
