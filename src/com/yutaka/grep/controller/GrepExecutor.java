package com.yutaka.grep.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.yutaka.grep.model.Arguments;
import com.yutaka.grep.util.AppUtil;
import com.yutaka.grep.util.FileUtil;

/**
 * grep 処理を行うクラス。 オプションによって処理が分岐する。
 */
public class GrepExecutor {
    private static final String DEFAULT_OPTION = "a";
    private static final String GREP_NON_MATCH = "v";
    private static final String GREP_BY_REGEXP = "e";
    private static final int QUEUE_MAX_CAPACITY = 5;

    public void excecutor(Arguments argModel) {

        String option = AppUtil.string2optional(argModel.getOption().get(0)).orElse(DEFAULT_OPTION);
        // デバッグ用
        System.out.println(option);
        try {
            switch (option) {
            case "i":
                break;
            case GREP_BY_REGEXP:
                grepByRegexp(argModel.getAbsolutePath(), argModel.getSearchTarget());
                break;
            case GREP_NON_MATCH:
                grepNonMatch(argModel.getAbsolutePath(), argModel.getSearchTarget());
                break;
            case "n":
                break;
            default:
                grepByKeyword(argModel.getAbsolutePath(), argModel.getSearchTarget());
            }

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
    }

    /**
     * キーワードで検証する。
     *
     * @param line
     *            対象行
     * @param searchTarget
     *            検索キーワード
     * @return
     */
    // public static boolean matchTestByKeyword(String line, String
    // searchTarget) {
    // return line.contains(searchTarget);
    // }

    /**
     * ファイルの内容に指定のキーワードが含まれているファイルのパスを抽出して出力する。
     *
     * @param dir
     *            引数に指定したディレクトリ
     * @param keyword
     *            引数に指定したキーワード
     */
    public void grepByKeyword(final String dir, final String keyword) throws FileNotFoundException, IOException {
        List<Path> grepTargetPaths = FileUtil.getUnderParentPaths(dir, ".txt");
        String line = null;

        for (Path grepTargetPath : grepTargetPaths) {
            try (BufferedReader reader = Files.newBufferedReader(grepTargetPath, StandardCharsets.UTF_8)) {
                while ((line = reader.readLine()) != null) {
                    if (line.contains(keyword)) {
                        System.out.println(grepTargetPath.toString());
                        break;
                    }
                }
            }
        }
    }

    /**
     * ファイルの内容に指定の正規表現に一致する行が存在する場合、ファイルのパスを出力する。
     *
     * @param dir
     *            引数にしていたディレクトリ
     * @param keyword
     *            引数に指定したキーワード
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void grepByRegexp(final String dir, final String keyword) throws FileNotFoundException, IOException {
        List<Path> grepTargetPaths = FileUtil.getUnderParentPaths(dir, ".txt");
        Pattern pattern = Pattern.compile(keyword);
        String line = null;
        for (Path grepTargetPath : grepTargetPaths) {
            try (BufferedReader reader = Files.newBufferedReader(grepTargetPath, StandardCharsets.UTF_8)) {
                while ((line = reader.readLine()) != null) {
                    /* 正規表現に一致する場合 */
                    if (pattern.matcher(line).find()) {
                        System.out.println(grepTargetPath);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 検索ワードに一致しないファイルのみ出力する。
     *
     * @param dir
     * @param keyword
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void grepNonMatch(final String dir, final String keyword) throws FileNotFoundException, IOException {
        List<Path> grepTargetPaths = FileUtil.getUnderParentPaths(dir, ".txt");
        String line = null;
        boolean result = false;
        for (Path grepTargetPath : grepTargetPaths) {
            try (BufferedReader reader = Files.newBufferedReader(grepTargetPath, StandardCharsets.UTF_8)) {
                while ((line = reader.readLine()) != null) {
                    if (line.contains(keyword)) {
                        result = true;
                        break;
                    }
                }
            }
            if (result) {
                result = false;
            } else {
                System.out.println(grepTargetPath);
            }
        }
    }

    public void grepByKeywordWithLine(final String dir, final String keyword)
            throws FileNotFoundException, IOException {
        List<Path> grepTargetPaths = FileUtil.getUnderParentPaths(dir, ".txt");
        String line = null;
        int lineNum = 0;
        Map<Integer, String> rowMap = new LinkedHashMap<>();
        for (Path grepTargetPath : grepTargetPaths) {
            try (BufferedReader reader = Files.newBufferedReader(grepTargetPath, StandardCharsets.UTF_8)) {
                while ((line = reader.readLine()) != null) {
                    lineNum++;
                    if (rowMap.size() == QUEUE_MAX_CAPACITY) {
                        //TODO LinkedLHashMapをFIFOキャッシュとした実装にする。
                    }
                    if (line.contains(keyword)) {
                        System.out.println(grepTargetPath.toString());
                        break;
                    }
                }
            }
        }
    }

}
