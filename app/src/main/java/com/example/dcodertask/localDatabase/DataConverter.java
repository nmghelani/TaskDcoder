package com.example.dcodertask.localDatabase;

import com.example.dcodertask.model.ValueObject;

import java.util.Arrays;
import java.util.List;

import androidx.room.TypeConverter;

public class DataConverter {
    @TypeConverter
    public static int getNumberFromStar(ValueObject s) {
        return s.getNumber();
    }

    @TypeConverter
    public static ValueObject getStartsFromNumber(int numbers) {
        return new ValueObject(numbers);
    }

    @TypeConverter
    public static String getTagsFromList(List<String> tagList) {
        StringBuilder tagString = null;
        for (String tag : tagList) {
            if (tagString == null) {
                tagString = new StringBuilder();
                tagString.append(tag);
            } else {
                tagString.append(", ").append(tag);
            }
        }
        if (tagString == null) {
            tagString = new StringBuilder();
        }
        return tagString.toString();
    }

    @TypeConverter
    public static List<String> getTagsFromList(String tags) {
        return Arrays.asList(tags.split(", "));
    }
}
