package app.trackwizz.connect.utils;

import java.util.List;

public class InitialOrderRuleDataModel_WordMatch {

    private List<String> content;
    private List<String> contentWithMoreThanOneWord;
    private List<String> contentWithOneWord;

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public List<String> getContentWithMoreThanOneWord() {
        return contentWithMoreThanOneWord;
    }

    public void setContentWithMoreThanOneWord(List<String> contentWithMoreThanOneWord) {
        this.contentWithMoreThanOneWord = contentWithMoreThanOneWord;
    }

    public List<String> getContentWithOneWord() {
        return contentWithOneWord;
    }

    public void setContentWithOneWord(List<String> contentWithOneWord) {
        this.contentWithOneWord = contentWithOneWord;
    }
}

