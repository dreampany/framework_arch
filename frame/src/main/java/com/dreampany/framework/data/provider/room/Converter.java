package com.dreampany.framework.data.provider.room;

import android.arch.persistence.room.TypeConverter;

import com.dreampany.framework.data.util.DataUtil;

import java.util.List;

/**
 * Created by air on 10/17/17.
 */

public class Converter {

    @TypeConverter
    public static String toString(List<String> values) {
        if (DataUtil.isEmpty(values)) {
            return null;
        }
        return DataUtil.join(values, DataUtil.DENIM);
    }

    @TypeConverter
    public static List<String> toList(String value) {
        if (DataUtil.isEmpty(value)) {
            return null;
        }
        return DataUtil.split(value, DataUtil.DENIM);
    }
}
