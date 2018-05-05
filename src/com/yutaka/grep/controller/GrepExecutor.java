package com.yutaka.grep.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Pattern;

import com.yutaka.grep.model.Arguments;
import com.yutaka.grep.util.AppUtil;
import com.yutaka.grep.util.Constants;
import com.yutaka.grep.util.FileUtil;

/**
 * grep 処理を行うクラス。 オプションによって処理が分岐する。
 */
public class GrepExecutor {
    /** オプション */
    private static final String DEFAULT_OPTION = "a";
    private static final String GREP_NON_MATCH = "v";
    private static final String GREP_BY_REGEXP = "e";
    private static final String GREP_WITH_LINE = "n";
    private static final String GREP_RECURSIVE = "r";
    /** 前後行の表示行数 */
    private static final int QUEUE_MAX_CAPACITY = 5;

    private static final String EXTENSION = "." + Constants.TARGET_EXTENSION;

    public void excecutor(Arguments argModel) {

        String option = AppUtil.string2optional(argModel.getOption().get(0)).orElse(DEFAULT_OPTION);
        // デバッグ用
        // System.out.println(option);
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
            case GREP_WITH_LINE:
                grepByKeywordWithLine(argModel.getAbsolutePath(), argModel.getSearchTarget());
                break;
            case GREP_RECURSIVE:
                grepRecursive(argModel.getAbsolutePath(), argModel.getSearchTarget(), new ArrayDeque<String>());
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
     * ファイルの内容に指定のキーワードが含まれているファイルのパスを抽出して出力する。
     *
     * @param dir
     *            引数に指定したディレクトリ
     * @param keyword
     *            引数に指定したキーワード
     */
    public void grepByKeyword(final String dir, final String keyword) throws FileNotFoundException, IOException {
        List<Path> grepTargetPaths = FileUtil.getPaths(dir, EXTENSION);
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
        List<Path> grepTargetPaths = FileUtil.getPaths(dir, EXTENSION);
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
        List<Path> grepTargetPaths = FileUtil.getPaths(dir, EXTENSION);
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

    /**
     * ヒットしたキーワードを含む行の前五行を行番号付きで表示する。
     *
     * @param dir
     * @param keyword
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void grepByKeywordWithLine(final String dir, final String keyword)
            throws FileNotFoundException, IOException {
        List<Path> grepTargetPaths = FileUtil.getPaths(dir, EXTENSION);
        String line = null;
        int lineNum = 0;
        boolean first = true;
        // サイズ5のFIFOキャッシュ
        Map<Integer, String> rowCache = new LinkedHashMap<Integer, String>(QUEUE_MAX_CAPACITY) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
                return size() > QUEUE_MAX_CAPACITY;
            }
        };
        for (Path grepTargetPath : grepTargetPaths) {
            try (BufferedReader reader = Files.newBufferedReader(grepTargetPath, StandardCharsets.UTF_8)) {
                while ((line = reader.readLine()) != null) {
                    lineNum++;
                    rowCache.put(lineNum, line);
                    // 一番最初に検索したキーワードより手前５行を出力する。
                    if (line.contains(keyword)) {
                        // System.out.println(grepTargetPath.toString());
                        if (!first)
                            System.out.println("-----------------");
                        if (first)
                            first = false;
                        break;
                    }
                }

                System.out.println(grepTargetPath);
                rowCache.entrySet().stream().map(e -> e.getKey() + ". " + e.getValue()).forEach(System.out::println);
                // 行キャッシュのリセット

                lineNum = 0;
                rowCache.clear();
            }
        }
    }

    /**
     * 親ディレクトリ階層化のディレクトリも含めてgrep対象とする。
     *
     * @param dir
     *            親ディレクトリ
     * @param keyword
     *            grepキーワード
     * @param dirCache
     *            走査対象ディレクトリ用キャッシュ
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void grepRecursive(final String dir, final String keyword, Queue<String> dirCache)
            throws FileNotFoundException, IOException {

        String parentPath = dir;
        String line = null;
        List<Path> grepTargetPaths = FileUtil.getPathsContainsDirectories(parentPath, EXTENSION);
        List<String> grepedPaths = new ArrayList<>();
        for (Path grepTargetPath : grepTargetPaths) {
            // ディレクトリの場合、探索対象のディレクトリとしてキャッシュに格納する。
            if (grepTargetPath.toFile().isDirectory()) {
                dirCache.add(grepTargetPath.toString());
                continue;
            }
            try (BufferedReader reader = Files.newBufferedReader(grepTargetPath, StandardCharsets.UTF_8)) {
                while ((line = reader.readLine()) != null) {
                    if (line.contains(keyword.trim())) {
                        grepedPaths.add(grepTargetPath.toString());
                    }
                }
            }
        }
        // 現階層にて、抽出パスが一件以上存在する場合、親ディレクトリと同時に出力する。
        if (grepedPaths.size() > 0) {
            System.out.println("親ディレクトリ : " + parentPath);
            grepedPaths.forEach(System.out::println);
        }

        // ディレクトリのキャッシュ内に未走査のディレクトリが存在している場合、このメソッドを再帰呼び出しする。
        if (dirCache.size() > 0) {
            String queueFile = dirCache.remove().toString();
            // System.out.println(queueFile);
            grepRecursive(queueFile, keyword, dirCache);
        }
    }

}
