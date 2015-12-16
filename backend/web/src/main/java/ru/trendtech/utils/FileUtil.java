package ru.trendtech.utils;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by petr on 23.03.2015.
 */

public class FileUtil {
    private static final ContentInfoUtil CONTENT_INFO_UTIL = new ContentInfoUtil();
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private static void exists(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
    }



    // write in file
    public static void write(String fileName, String text) {
        File file = new File(fileName);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());
            try {
                out.print(text);
            } finally {
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }




    public static String base64ToText(String base64) throws UnsupportedEncodingException {
        byte[] decoded = Base64.decodeBase64(base64);
         return new String(decoded, "UTF-8");
    }



    public static void writeToFile(String base64) throws IOException {
        byte[] decoded = Base64.decodeBase64(base64);
        ContentInfo match = CONTENT_INFO_UTIL.findMatch(new ByteArrayInputStream(decoded));
        String[] extensions = match.getFileExtensions();
        String ext = extensions[0];
        LOGGER.info("extension: "+ext);
        FileOutputStream fos = new FileOutputStream("C:\\output\\coordinates.txt");
        fos.write(decoded);
        fos.close();
    }




    //List<Long> clientIds = FileUtil.read("c:\\6.txt");
    public static ArrayList<String> readFile(String fileName) throws FileNotFoundException {
        ArrayList<String> strings = new ArrayList<>();
        File file = new File(fileName);
        exists(fileName);
        try {
            //Объект для чтения файла в буфер
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                //В цикле построчно считываем файл
                String s;
                while ((s = in.readLine()) != null) {
                    strings.add(s);
                    //sb.append(s);
                    //sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return strings;
    }


    // read file
    public static ArrayList<Long> read(String fileName) throws FileNotFoundException {
        ArrayList<Long> clientIds = new ArrayList<>();
        File file = new File(fileName);
        //Этот спец. объект для построения строки
        StringBuilder sb = new StringBuilder();

        exists(fileName);

        try {
            //Объект для чтения файла в буфер
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                //В цикле построчно считываем файл
                String s;
                while ((s = in.readLine()) != null) {
                    clientIds.add(Long.parseLong(s));
                    //sb.append(s);
                    //sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return clientIds;
    }
}
