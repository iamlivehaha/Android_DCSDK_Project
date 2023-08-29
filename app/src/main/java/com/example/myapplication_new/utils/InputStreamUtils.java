package com.example.myapplication_new.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamUtils {

    /**
     * convert inputstream to String
     * @param inputStream
     * @return converted String
     * @throws IOException read failed
     */
    public static String parseIsToString(InputStream inputStream)  throws IOException{
        StringBuilder builder = new StringBuilder();
        String line;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = bufferedReader.readLine())!=null)
        {
            builder.append(line);
        }
        String str = builder.toString();
        return str;
    }
}
