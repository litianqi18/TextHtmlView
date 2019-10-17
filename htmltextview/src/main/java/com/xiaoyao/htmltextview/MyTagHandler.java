package com.xiaoyao.htmltextview;

import android.text.Editable;
import android.text.Html;
import android.text.style.BulletSpan;
import android.text.style.StrikethroughSpan;

import org.xml.sax.XMLReader;

public class MyTagHandler implements Html.TagHandler {
    private static final String BULLET_LI = "li";
    private static final String STRIKETHROUGH_S = "s";
    private static final String STRIKETHROUGH_STRIKE = "strike";
    private static final String STRIKETHROUGH_DEL = "del";

    public MyTagHandler() {
    }

    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if(tag.equals("ul") && !opening) output.append("\n");
        if(tag.equals("li") && opening) output.append("\n\t•");
        if (opening) {
            if (tag.equalsIgnoreCase("li")) {
                if (output.length() > 0 && output.charAt(output.length() - 1) != '\n') {
                    output.append("\n");
                }

                this.start(output, new MyTagHandler.Li());
            } else if (tag.equalsIgnoreCase("s") || tag.equalsIgnoreCase("strike") || tag.equalsIgnoreCase("del")) {
                this.start(output, new MyTagHandler.Strike());
            }
        } else if (tag.equalsIgnoreCase("li")) {
            if (output.length() > 0 && output.charAt(output.length() - 1) != '\n') {
                output.append("\n");
            }

            this.end(output, MyTagHandler.Li.class, new BulletSpan());
        } else if (tag.equalsIgnoreCase("s") || tag.equalsIgnoreCase("strike") || tag.equalsIgnoreCase("del")) {
            this.end(output, MyTagHandler.Strike.class, new StrikethroughSpan());
        }

    }

    private void start(Editable output, Object mark) {
        output.setSpan(mark, output.length(), output.length(), 17);
    }

    private void end(Editable output, Class kind, Object... replaces) {
        Object last = getLast(output, kind);
        int start = output.getSpanStart(last);
        int end = output.length();
        output.removeSpan(last);
        if (start != end) {
            Object[] var7 = replaces;
            int var8 = replaces.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                Object replace = var7[var9];
                output.setSpan(replace, start, end, 33);
            }
        }

    }

    private static Object getLast(Editable text, Class kind) {
        Object[] spans = text.getSpans(0, text.length(), kind);
        if (spans.length == 0) {
            return null;
        } else {
            for(int i = spans.length; i > 0; --i) {
                if (text.getSpanFlags(spans[i - 1]) == 17) {
                    return spans[i - 1];
                }
            }

            return null;
        }
    }

    private static class Strike {
        private Strike() {
        }
    }

    private static class Li {
        private Li() {
        }
    }
}

