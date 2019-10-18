package com.xiaoyao.htmltextview;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.text.style.CharacterStyle;
import android.text.style.ImageSpan;
import android.text.style.ParagraphStyle;
import android.text.style.QuoteSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import com.scrat.app.richtext.parser.MyTagHandler;

public class HtmlParser {
    public HtmlParser() {
    }

    public static Spanned fromHtml(String source, Html.ImageGetter imageGetter) {
        return Html.fromHtml(source, imageGetter, new MyTagHandler());
    }

    public static Spanned fromHtml(String source) {
        return Html.fromHtml(source, (Html.ImageGetter)null, new MyTagHandler());
    }

    public static String toHtml(Spanned text) {
        StringBuilder out = new StringBuilder();
        withinHtml(out, text);
        return tidy(out.toString());
    }

    private static void withinHtml(StringBuilder out, Spanned text) {
        int next;
        for(int i = 0; i < text.length(); i = next) {
            next = text.nextSpanTransition(i, text.length(), ParagraphStyle.class);
            ParagraphStyle[] styles = (ParagraphStyle[])text.getSpans(i, next, ParagraphStyle.class);
            if (styles.length == 2) {
                if (styles[0] instanceof BulletSpan && styles[1] instanceof QuoteSpan) {
                    withinBulletThenQuote(out, text, i, next++);
                } else if (styles[0] instanceof QuoteSpan && styles[1] instanceof BulletSpan) {
                    withinQuoteThenBullet(out, text, i, next++);
                } else {
                    withinContent(out, text, i, next);
                }
            } else if (styles.length == 1) {
                if (styles[0] instanceof BulletSpan) {
                    withinBullet(out, text, i, next++);
                } else if (styles[0] instanceof QuoteSpan) {
                    withinQuote(out, text, i, next++);
                } else {
                    withinContent(out, text, i, next);
                }
            } else {
                withinContent(out, text, i, next);
            }
        }

    }

    private static void withinBulletThenQuote(StringBuilder out, Spanned text, int start, int end) {
        out.append("<ul><li>");
        withinQuote(out, text, start, end);
        out.append("</li></ul>");
    }

    private static void withinQuoteThenBullet(StringBuilder out, Spanned text, int start, int end) {
        out.append("<blockquote>");
        withinBullet(out, text, start, end);
        out.append("</blockquote>");
    }

    private static void withinBullet(StringBuilder out, Spanned text, int start, int end) {
        out.append("<ul>");

        int next;
        for(int i = start; i < end; i = next) {
            next = text.nextSpanTransition(i, end, BulletSpan.class);
            BulletSpan[] spans = (BulletSpan[])text.getSpans(i, next, BulletSpan.class);
            BulletSpan[] var7 = spans;
            int var8 = spans.length;

            BulletSpan var10000;
            int var9;
            for(var9 = 0; var9 < var8; ++var9) {
                var10000 = var7[var9];
                out.append("<li>");
            }

            withinContent(out, text, i, next);
            var7 = spans;
            var8 = spans.length;

            for(var9 = 0; var9 < var8; ++var9) {
                var10000 = var7[var9];
                out.append("</li>");
            }
        }

        out.append("</ul>");
    }

    private static void withinQuote(StringBuilder out, Spanned text, int start, int end) {
        int next;
        for(int i = start; i < end; i = next) {
            next = text.nextSpanTransition(i, end, QuoteSpan.class);
            QuoteSpan[] quotes = (QuoteSpan[])text.getSpans(i, next, QuoteSpan.class);
            QuoteSpan[] var7 = quotes;
            int var8 = quotes.length;

            QuoteSpan var10000;
            int var9;
            for(var9 = 0; var9 < var8; ++var9) {
                var10000 = var7[var9];
                out.append("<blockquote>");
            }

            withinContent(out, text, i, next);
            var7 = quotes;
            var8 = quotes.length;

            for(var9 = 0; var9 < var8; ++var9) {
                var10000 = var7[var9];
                out.append("</blockquote>");
            }
        }

    }

    private static void withinContent(StringBuilder out, Spanned text, int start, int end) {
        int next;
        for(int i = start; i < end; i = next) {
            next = TextUtils.indexOf(text, '\n', i, end);
            if (next < 0) {
                next = end;
            }

            int nl;
            for(nl = 0; next < end && text.charAt(next) == '\n'; ++nl) {
                ++next;
            }

            withinParagraph(out, text, i, next - nl, nl);
        }

    }

    private static void withinParagraph(StringBuilder out, Spanned text, int start, int end, int nl) {
        int next;
        int i;
        for(i = start; i < end; i = next) {
            next = text.nextSpanTransition(i, end, CharacterStyle.class);
            CharacterStyle[] spans = (CharacterStyle[])text.getSpans(i, next, CharacterStyle.class);
            CharacterStyle[] var8 = spans;
            int style = spans.length ;

            for(int var10 = 0; var10 < style ; ++var10) {
                CharacterStyle span = var8[var10];
                if (span instanceof StyleSpan) {
                    int style2 = ((StyleSpan)span).getStyle();
                    if ((style2 & 1) != 0) {
                        out.append("<b>");
                    }

                    if ((style2 & 2) != 0) {
                        out.append("<i>");
                    }
                }

                if (span instanceof UnderlineSpan) {
                    out.append("<u>");
                }

                if (span instanceof StrikethroughSpan) {
                    out.append("<del>");
                }

                if (span instanceof URLSpan) {
                    out.append("<a href=\"");
                    out.append(((URLSpan)span).getURL());
                    out.append("\">");
                }

                if (span instanceof ImageSpan) {
                    out.append("<img width=\"100%\" src=\"");
                    out.append(((ImageSpan)span).getSource());
                    out.append("\">");
                    i = next;
                }
            }

            withinStyle(out, text, i, next);

            for(int j = spans.length - 1; j >= 0; --j) {
                if (spans[j] instanceof URLSpan) {
                    out.append("</a>");
                }

                if (spans[j] instanceof StrikethroughSpan) {
                    out.append("</del>");
                }

                if (spans[j] instanceof UnderlineSpan) {
                    out.append("</u>");
                }

                if (spans[j] instanceof StyleSpan) {
                    style = ((StyleSpan)spans[j]).getStyle();
                    if ((style & 1) != 0) {
                        out.append("</b>");
                    }

                    if ((style & 2) != 0) {
                        out.append("</i>");
                    }
                }
            }
        }

        for(i = 0; i < nl; ++i) {
            out.append("<br>");
        }

    }

    private static void withinStyle(StringBuilder out, CharSequence text, int start, int end) {
        out.append(text.subSequence(start, end));
    }

    private static String tidy(String html) {
        return html.replaceAll("</ul>(<br>)?", "</ul>").replaceAll("</blockquote>(<br>)?", "</blockquote>");
    }
}