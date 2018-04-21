package com.yutaka.grep.model;

import java.util.List;

/**
 * 取得した引数のパラメタオブジェクト。
 */
public class Arguments {

    /** ファイルパス（絶対パス） */
    private String absolutePath;

    /** 対象キーワードor正規表現 */
    private String searchTarget;

    /** オプション */
    private List<String> option;

    /**
     * コンストラクタ
     * @param absolutePath
     * @param searchTarget
     * @param option
     */
    public Arguments(String absolutePath, String searchTarget, List<String> option){
        this.absolutePath = absolutePath;
        this.searchTarget = searchTarget;
        this.option = option;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getSearchTarget() {
        return searchTarget;
    }

    public void setSearchTarget(String searchTarget) {
        this.searchTarget = searchTarget;
    }

    public List<String> getOption() {
        return option;
    }

    public void setOption(List<String> option) {
        this.option = option;
    }

}

