package com.dreampany.framework.data.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.util.SparseArray;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.primitives.Longs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.collections4.ListUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class DataUtil {
    public static final String equal = "=";
    public static final String space = " ";
    public static final String space3 = "   ";
    public static final String comma = ", ";
    public static final String ask = "?";
    public static final String or = "or";
    public static final String NewLine = "\n";
    public static final String NewLine2 = "\n\n";
    public static final String DENIM = "#&#";
    public static final String WORD_REGEX = "[a-zA-Z]+";
    public static final int KB = 1024;
    public static final int MB = 1024 * KB;
    public static final long GB = 1024 * MB;
    public static final long TB = 1024 * GB;
    public static final long PB = 1024 * TB;

    private static final Gson gson = new Gson();

    private DataUtil() {
    }

    private static final int sNameLength = 3;
    private static final int sPasswordLength = 6;


    public static boolean isValidUrl(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }

    public static boolean isValidName(String name) {
        return !TextUtils.isEmpty(name) && name.length() >= sNameLength;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= sPasswordLength;
    }

    public static boolean isEmpty(String dataValue) {
        return TextUtils.isEmpty(dataValue);
    }

    public static boolean isEmpty(String... dataValues) {
        if (dataValues == null) {
            return true;
        }
        for (String dataValue : dataValues) {
            if (isEmpty(dataValue)) return true;
        }
        return false;
    }

    public static boolean isEmpty(Integer dataValue) {
        return dataValue == 0;
    }

    public static boolean isEmpty(Integer... dataValues) {
        for (Integer dataValue : dataValues) {
            if (isEmpty(dataValue)) return true;
        }
        return false;
    }

    public static boolean allEmpty(Integer... dataValues) {
        for (Integer dataValue : dataValues) {
            if (!isEmpty(dataValue)) return false;
        }
        return true;
    }

    public static boolean isEmpty(Long dataValue) {
        return dataValue == 0L;
    }

/*    public static boolean isEmpty(Status dataValue) {
        return dataValue == null;
    }*/

/*    public static boolean isEmpty(State dataValue) {
        return dataValue == null;
    }*/

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static int toSize(Collection list) {
        return !isEmpty(list) ? list.size() : 0;
    }

/*    public static boolean isAbsoluteEmpty(List list) {
        return list == null || list.isAbsoluteEmpty();
    }*/

    public static boolean isEmpty(Object object) {
        return object == null;
    }

    public static boolean isEmpty(byte[] data) {
        return data == null || data.length <= 0;
    }

    public static long getSmaller(long left, long right) {
        return (left > right) ? right : left;
    }

    public static String getSmaller(String left, String right) {
        return (left.compareTo(right) > 0) ? right : left;
    }

    public static long getGreater(long left, long right) {
        return (left > right) ? left : right;
    }

    public static String getGreater(String left, String right) {
        return (left.compareTo(right) > 0) ? left : right;
    }

    public static boolean equals(long left, long right) {
        return left == right;
    }

    public static boolean equals(Object left, Object right) {
        if (isEmpty(left) && isEmpty(right)) return true;
        if (isEmpty(left) || isEmpty(right)) return false;

        if (left.equals(right)) return true;

        return false;
    }

    public static boolean equals(String left, String right) {
        if (isEmpty(left) && isEmpty(right)) return true;

        if (left.equals(right)) return true;

        return false;
    }

    public static List sort(List items) {
        if (!isEmpty(items)) {
            Collections.sort(items);
        }
        return items;
    }

    public static int alter(int value) {
        return -value;
    }

    public static int absolute(int value) {
        return Math.abs(value);
    }

    public static long getMax(long... values) {
        return Longs.max(values);
    }

    public static long getSha256() {
        String uuid = UUID.randomUUID().toString();
        return getSha256(uuid);
    }

    public static long getSha256(String data) {
        if (DataUtil.isEmpty(data)) {
            return 0L;
        }
        return getSha256(data.getBytes());
    }

    public static long getSha256(byte[] data) {
        if (DataUtil.isEmpty(data)) {
            return 0L;
        }
        return Math.abs(Hashing.sha256().newHasher().putBytes(data).hash().asLong());
    }

    public static long getSha256(File file) {
        try {
            HashCode hash = Files.asByteSource(file).hash(Hashing.sha256());
            return Math.abs(hash.asLong());
        } catch (IOException e) {
            return 0L;
        }
    }

    public static void setValue(ContentValues values, String key, String value) {
        if (!isEmpty(value)) {
            values.put(key, value);
        }
    }

    public static void setValue(ContentValues values, String key, Integer value) {
        if (!isEmpty(value)) {
            values.put(key, value);
        }
    }

    public static void setValue(ContentValues values, String key, Long value) {
        if (!isEmpty(value)) {
            values.put(key, value);
        }
    }

    public static void setValue(ContentValues values, String key, Boolean value) {
        values.put(key, value ? 1 : 0);
    }

/*    public static void setValue(ContentValues values, String key, Status value) {
        if (!isEmpty(value)) {
            values.put(key, value.toString());
        }
    }

    public static void setValue(ContentValues values, String key, State value) {
        if (!isEmpty(value)) {
            values.put(key, value.toString());
        }
    }*/

/*
    public static Status getStatus(String value) {
        return Status.valueOf(value);
    }

    public static State getState(String value) {
        return State.valueOf(value);
    }
*/

    public static String concat(String[] columns) {
        return concat(null, columns);
    }

    public static String concat(String leftTable, String[] leftColumns, String rightTable, String[] rightColumns) {
        return concat(leftTable, leftColumns) + "," + concat(rightTable, rightColumns);
    }

    public static String concat(String table, String[] columns) {
        StringBuilder projectionIn = new StringBuilder();
        String joinTable = table == null ? "" : table + ".";
        boolean first = true;
        for (String column : columns) {
            if (first) {
                projectionIn.append(joinTable).append(column);
                first = false;
                continue;
            }
            projectionIn.append(",").append(joinTable).append(column);
        }

        return projectionIn.toString();
    }

    public static String concatSelect(String[] selectValues) {
        StringBuilder projectionIn = new StringBuilder();
        boolean first = true;
        for (String selectValue : selectValues) {
            if (!first)
                projectionIn.append(",");

            first = false;
            projectionIn.append("'").append(selectValue).append("'");
        }

        return projectionIn.toString();
    }

    public static String concatSelect(Long[] selectValues) {
        StringBuilder projectionIn = new StringBuilder();
        boolean first = true;
        for (long selectValue : selectValues) {
            if (!first)
                projectionIn.append(",");

            first = false;
            projectionIn.append("'").append(selectValue).append("'");
        }

        return projectionIn.toString();
    }

    public static String concatOr(String selection, int repeat) {
        StringBuilder select = new StringBuilder();
        for (int index = 1; index <= repeat; index++) {
            select.append(selection).append(space).append(equal).append(space).append(ask);
            if (index < repeat) select.append(space).append(or).append(space);
        }
        return select.toString();
    }

    public static int readIntValue(Cursor cursor, String columnName, int defaultValue) {

        if (cursor != null) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
        }

        return defaultValue;
    }

    public static long readLongValue(Cursor cursor, String columnName, long defaultValue) {

        if (cursor != null) {
            return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
        }

        return defaultValue;
    }

    public static String readStringValue(Cursor cursor, String columnName, String defaultValue) {

        if (cursor != null) {
            return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
        }

        return defaultValue;
    }

    public static String[] toStringArray(List<Long> values) {
        String[] array = new String[values.size()];
        for (int index = 0; index < values.size(); index++) {
            array[index] = Long.toString(values.get(index));
        }
        return array;
    }

    public static byte[] toBytes(String data) {
        return !isEmpty(data) ? data.getBytes() : null;
    }

/*    public static byte[] toBytes(State data) {
        if (!isEmpty(data)) {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putInt(data.ordinal());
            return buffer.array();
        }
        return null;
    }*/

    public static String toString(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        return new String(data);
    }

/*    public static State toState(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int ordinal = buffer.getInt();
        return State.valueOf(ordinal);
    }*/

    public final static long ONE_SECOND = 1000;
    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long ONE_DAY = ONE_HOUR * 24;

    /**
     * converts time (in milliseconds) to human-readable format
     * "<w> days, <x> hours, <y> minutes and (z) seconds"
     */
    public static String millisToLongDHMS(long duration) {
        StringBuffer res = new StringBuffer();
        boolean isDay = false, isHr = false, isMin = false;
        long temp = 0;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                isDay = true;
                duration -= temp * ONE_DAY;
                res.append(temp >= 10 ? temp : "0" + temp).append("d ");
            }
            temp = duration / ONE_HOUR;
            if (temp > 0) {
                isHr = true;
                duration -= temp * ONE_HOUR;
                res.append(temp >= 10 ? temp : "0" + temp).append("h ");
            }
            if (isDay)
                return res.toString() + ((temp > 0) ? "" : "00h");
            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                isMin = true;
                duration -= temp * ONE_MINUTE;
                res.append(temp >= 10 ? temp : "0" + temp).append("m ");
            }
            if (isHr)
                return res.toString() + ((temp > 0) ? "" : "00m");

            temp = duration / ONE_SECOND;
            if (temp > 0) {
                res.append(temp >= 10 ? temp : "0" + temp).append("s");
            }
            return res.toString() + ((temp > 0) ? "" : "00s");
        } else {
            return "0s";
        }
    }

    public static int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public static String[] toStringArray(String jArray) {
        if (jArray == null)
            return null;
        try {
            JSONArray array = new JSONArray(jArray);
            String[] arr = new String[array.length()];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = array.optString(i);
            }
            return arr;
        } catch (JSONException jse) {
            return null;
        }
    }

    public static byte[] copy(byte[] src, int from) {
        return Arrays.copyOfRange(src, from, src.length);
    }

    public static ByteBuffer copyToBuffer(byte[] src, int offset) {
        byte[] data = Arrays.copyOfRange(src, offset, src.length);
        return ByteBuffer.wrap(data);
        //return ByteBuffer.wrap(src, offset, src.length - offset);
    }

/*    public static String toString(List<State> states) {
        String value = null;
        if (!DataUtil.isEmpty(states)) {
            StringBuilder builder = new StringBuilder();
            for (State state : states) {
                if (builder.length() > 0) {
                    builder.append(space);
                }
                builder.append(state.value());
            }
            value = builder.toString();
        }
        return value;
    }

    public static List<State> toStates(String value) {
        List<State> states = null;
        if (!DataUtil.isEmpty(value)) {
            states = new ArrayList<>();
            String[] values = value.split(space);
            for (String v : values) {
                states.add(State.valueOf(v));
            }
        }
        return states;
    }*/

    public static String toBase64(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static byte[] toBase64(String data) {
        return Base64.decode(data, Base64.DEFAULT);
    }

    public static String toJson(List<String> values) {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return gson().toJson(values, listType);
    }

    public static List<String> toList(String value) {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return gson().fromJson(value, listType);
    }

    public static String toString(List<String> values) {
        String value = null;
        if (!DataUtil.isEmpty(values)) {
            StringBuilder builder = new StringBuilder();
            for (String v : values) {
                if (builder.length() > 0) {
                    builder.append(comma);
                }
                builder.append(v);
            }
            value = builder.toString();
        }
        return value;
    }

    public static String toJoinString(List<String> values) {
        String value = null;
        if (!DataUtil.isEmpty(values)) {
            StringBuilder builder = new StringBuilder();
            for (String v : values) {
                if (builder.length() > 0) {
                    builder.append(space);
                }
                builder.append(v);
            }
            value = builder.toString();
        }
        return value;
    }

    public static void joinString(StringBuilder builder, String value) {
        List<String> values = getWords(value);
        if (!DataUtil.isEmpty(values)) {
            for (String v : values) {
                if (builder.length() > 0) {
                    builder.append(space);
                }
                builder.append(v);
            }
        }
    }

    public static void join(StringBuilder builder, String first, String second, String sep, String denim) {
        builder.append(first).append(sep).append(second).append(denim);
    }

    public static void join(StringBuilder builder, String first, String denim) {
        builder.append(first).append(denim);
    }

    public static String join(List<String> values) {
        String value = null;
        if (!DataUtil.isEmpty(values)) {
            StringBuilder builder = new StringBuilder();
            for (String v : values) {
                builder.append(v);
            }
            value = builder.toString();
        }
        return value;
    }

    public static String join(List<String> values, String denim) {
        String value = null;
        if (!DataUtil.isEmpty(values)) {
            StringBuilder builder = new StringBuilder();
            for (String v : values) {
                if (builder.length() > 0) {
                    builder.append(denim);
                }
                builder.append(v);
            }
            value = builder.toString();
        }
        return value;
    }

    public static List<String> split(String text, String denim) {
        String[] split = text.split(denim);
        List<String> arrays = new ArrayList<>(Arrays.asList(split));
        return arrays;
    }

    public static String toFirstString(List<String> values) {
        String value = null;
        if (!DataUtil.isEmpty(values)) {
            StringBuilder builder = new StringBuilder();
            for (String v : values) {
                if (builder.length() > 0) {
                    builder.append(comma);
                }
                builder.append(v);
                break;
            }
            value = builder.toString();
        }
        return value;
    }

/*    public static List<String> getWords(String text) {
        String regex = "([^a-zA-Z']+)'*\\1*";
        String[] split = text.split(regex);
        List<String> items = new ArrayList<>();
        for (int index = 0; index < split.length; index++) {


        }
        return items;
    }*/

    public static List<String> getWords(String text) {
        Matcher matcher = Pattern.compile(WORD_REGEX).matcher(text);
        Set<String> items = new HashSet<>();
        while (matcher.find()) {
            items.add(matcher.group());
        }
        return new ArrayList<>(items);
    }



/*    public static List<String> toLowerCase(List<String> items) {
        List<String> result = items.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        return result;
    }*/

    public static String[] splitWithSpace(String item) {
        return item.split(space);
    }

    synchronized public static Gson gson() {
        return gson;
    }

    public static int count(Collection collection) {
        if (isEmpty(collection)) {
            return 0;
        }
        return collection.size();
    }

    public static float string2Float(String value) {
        if (isEmpty(value))
            return -1;

        return Float.parseFloat(value);
    }

    public static boolean equals(final Collection<?> list1, final Collection<?> list2) {
        return ListUtils.isEqualList(list1, list2);
    }

    public static <T> List<T> asList(SparseArray<T> sparseArray) {
        if (sparseArray == null) return null;
        List<T> arrayList = new ArrayList<T>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }

    public static List<String> strToList(String text) {
        List<String> list = new ArrayList<>(text.length());
        for (int index = 0; index < text.length(); index++) {
            list.add(String.valueOf(text.charAt(index)));
        }
        return list;
    }

    public static boolean equals(final double left, final double right) {
        return left == right;
    }

    public static String getReadableDuration(long duration) {
        long seconds = duration / 1000;
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        if (h <= 0) {
            return String.format(Locale.ENGLISH, "%02d:%02d", m, s);
        }
        return String.format(Locale.ENGLISH, "%02d:%02d:%02d", h, m, s);
    }

    public static String formatReadableSize(long value, boolean si) {
        int unit = si ? 1000 : 1024;
        if (value < unit) return value + " B";
        int exp = (int) (Math.log(value) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.ENGLISH, "%.1f %sB", value / Math.pow(unit, exp), pre);
    }

    public static String formatReadableCount(long value, boolean si) {
        int unit = si ? 1000 : 1024;
        if (value < unit) return String.valueOf(value);
        int exp = (int) (Math.log(value) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.ENGLISH, "%.1f %s", value / Math.pow(unit, exp), pre);
    }
}
