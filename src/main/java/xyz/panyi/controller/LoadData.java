package xyz.panyi.controller;

import com.alibaba.fastjson.JSONArray;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import xyz.panyi.model.Mature;

import java.io.*;
import java.util.Scanner;

@Component
public class LoadData implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("应用启动 。。。。。");
        System.out.println("导入数据 。。。。。");

        long t1 = System.currentTimeMillis();

        InputStreamReader reader = null;
        try {
            File jsonFile = ResourceUtils.getFile("classpath:list.json");
            String content = readFromInputStream(new FileInputStream(jsonFile));
            //System.out.println("content = " + content);
            MatureController.matureList = JSONArray.parseArray(content, Mature.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        long t2 = System.currentTimeMillis();
        System.out.println("导入数据耗时 = " + (t2 - t1));

        System.out.println("数据总量 = " + MatureController.matureList.size());
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream , "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
