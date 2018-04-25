package com.yutaka.grep.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * プロパティファイル情報クラス。
 */
public class Property {

    private static Properties properties;

    /**
     * Propertiesオブジェクトを取得する。
     * 作成されていなければ、プロパティファイルから読み込みを行う。
     * 作成されている場合、シングルトンとしてオブジェクトを使いまわす。
     *
     * @return
     */
    public static Properties getProperties() {
        if(properties != null){
            return properties;
        }
         Path path = Paths.get("/resources/app.properties");
         try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
             properties.load(reader);

         } catch (FileNotFoundException e) {
             e.printStackTrace();
             throw new RuntimeException(e);
         } catch (IOException e) {
             e.printStackTrace();
             throw new RuntimeException(e);
         } catch (Exception e) {
             e.printStackTrace();
             throw new RuntimeException(e);
         }

        return properties;
    }

}
