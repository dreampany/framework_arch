package com.dreampany.framework.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.dreampany.framework.data.enums.Type;
import com.dreampany.framework.data.enums.SQLiteQueryType;
import com.dreampany.framework.data.enums.SQLiteSelectionType;

import java.lang.reflect.Field;

/**
 * Created by nuc on 12/3/2016.
 */

public class SQLiteTask<T extends Base, X extends Type> extends Task<T> {

    private long hash;
    private int count;
    private SQLiteQueryType queryType;
    private SQLiteSelectionType selectionType;

    public SQLiteTask() {
    }

    public SQLiteTask(T item) {
        super(item);
    }

    public void setHash(long hash) {
        this.hash = hash;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setQueryType(SQLiteQueryType queryType) {
        this.queryType = queryType;
    }

    public void setSelectionType(SQLiteSelectionType selectionType) {
        this.selectionType = selectionType;
    }

    public long getHash() {
        return hash;
    }

    public long getCount() {
        return count;
    }

    public SQLiteQueryType getQueryType() {
        return queryType;
    }

    public SQLiteSelectionType getSelectionType() {
        return selectionType;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                //requires access to private field:
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                //System.out.println(ex);
                ex.printStackTrace();
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }
}
