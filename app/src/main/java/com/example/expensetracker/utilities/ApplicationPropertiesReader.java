package com.example.expensetracker.utilities;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationPropertiesReader {
    private final Context context;
    private final Properties properties;

    public ApplicationPropertiesReader(Context context) {
        this.context = context;
        properties = new Properties();
    }

    public Properties getProperties(String fileName) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
