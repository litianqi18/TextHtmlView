package com.xiaoyao.htmltextview;

import android.text.Editable;
import android.text.Html;

import org.xml.sax.XMLReader;

public class UiTagHandler implements Html.TagHandler {

    public UiTagHandler(){

    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if(tag.equals("ul") && !opening) output.append("\n");
        if(tag.equals("li") && opening) output.append("\n\t•");
    }
}