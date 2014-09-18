package com.hydrophilik.javaCrons.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public static String createAndWriteToFile(String filePathAndName, List<String> fileLines) {

        File file = new File(filePathAndName);

        FileWriter fw = null;
        BufferedWriter bw = null;

        // if file doesnt exists, then create it
        if (file.exists()) {
            if (!file.delete()) {
                return "Unable to delete existing file: " + filePathAndName;
            }
        }

        try {
            if (!file.createNewFile())
                return "Unable to create the file: " + filePathAndName;

            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);

            for (String line : fileLines) {
                bw.write(line + "\n");
            }
        } catch (IOException e) {
            return ExceptionUtils.getMessage(e);
        }

        finally {
            try {
                if (null != bw) bw.close();
                if (null != fw) fw.close();
            } catch(Exception e) {e.printStackTrace();}
        }


        return null;
    }
}
