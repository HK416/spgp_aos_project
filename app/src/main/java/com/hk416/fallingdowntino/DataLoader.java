package com.hk416.fallingdowntino;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class DataLoader {
    public static final class DataBlock {
        public static final String BEST_TAG = "best";
        public static final String LIKE_TAG = "like";

        public long bestDistance = 0;
        public long numLikes = 0;
    }

    private static final String TAG = DataLoader.class.getSimpleName();
    private static final String FILE_NAME = "user_data.json";

    public static DataBlock load(Context context) {
        DataBlock block = new DataBlock();
        try {
            FileInputStream fileInputStream = context.openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            JsonReader jsonReader = new JsonReader(inputStreamReader);

            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String tag = jsonReader.nextName();
                if (tag.equals(DataBlock.BEST_TAG)) {
                    block.bestDistance = jsonReader.nextLong();
                } else if (tag.equals(DataBlock.LIKE_TAG)) {
                    block.numLikes = jsonReader.nextLong();
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            jsonReader.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "::load >> 파일을 찾을 수 없습니다!");
            return block;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return block;
    }

    public static void store(Context context, DataBlock block) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            JsonWriter jsonWriter = new JsonWriter(outputStreamWriter);
            jsonWriter.beginObject();
            jsonWriter.name(DataBlock.BEST_TAG);
            jsonWriter.value(block.bestDistance);
            jsonWriter.name(DataBlock.LIKE_TAG);
            jsonWriter.value(block.numLikes);
            jsonWriter.endObject();
            jsonWriter.close();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
