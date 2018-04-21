package com.yutaka.grep.main;

import com.yutaka.grep.controller.CommandLineGetter;
import com.yutaka.grep.controller.GrepExecutor;
import com.yutaka.grep.model.Arguments;

/**
 * メイン処理。
 *
 */
public class Main {

    public static void main(String[] args) {

        /* 与えられた引数をオブジェクトに格納する。 */
        Arguments argModel = CommandLineGetter.getArguments(args);

        // デバッグ用
        System.out.println(argModel.getAbsolutePath() + " " + argModel.getSearchTarget());

        /* grepを実行する。 */
        GrepExecutor exec = new GrepExecutor();
        exec.excecutor(argModel);


    }
}
