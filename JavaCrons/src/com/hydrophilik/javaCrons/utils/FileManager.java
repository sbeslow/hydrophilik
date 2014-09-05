package com.hydrophilik.javaCrons.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by scottbeslow on 9/5/14.
 */
public class FileManager {

    public static List<String> readFileLines(String filePathAndName) {

        List<String> retLines = new ArrayList<String>();

        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(filePathAndName));

            while ((sCurrentLine = br.readLine()) != null) {
                retLines.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return retLines;
    }
}
