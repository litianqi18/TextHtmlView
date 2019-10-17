package com.xiaoyao.htmltextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.style.BulletSpan;
import android.text.style.QuoteSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import com.scrat.app.richtext.R.styleable;
import com.scrat.app.richtext.glide.GlideApp;
import com.scrat.app.richtext.glide.GlideRequests;
import com.scrat.app.richtext.img.GlideImageGeter;
import com.scrat.app.richtext.span.MyBulletSpan;
import com.scrat.app.richtext.span.MyQuoteSpan;
import com.scrat.app.richtext.span.MyURLSpan;

public class RichTextView extends AppCompatTextView {
    private GlideRequests glideRequests;
    private int linkColor = 0;
    private boolean linkUnderline = true;
    private int quoteColor = 0;
    private int quoteStripeWidth = 0;
    private int quoteGapWidth = 0;
    private int bulletColor = 0;
    private int bulletRadius = 0;
    private int bulletGapWidth = 0;

    public RichTextView(Context context){
        super(context);
        this.init(context, (AttributeSet)null);
    }
    public RichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context,attrs);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.glideRequests = GlideApp.with(context);
        TypedArray array = context.obtainStyledAttributes(attrs, styleable.RichEditText);
        this.bulletColor = array.getColor(styleable.RichEditText_bulletColor, 0);
        this.bulletRadius = array.getDimensionPixelSize(styleable.RichEditText_bulletRadius, 0);
        this.bulletGapWidth = array.getDimensionPixelSize(styleable.RichEditText_bulletGapWidth, 0);
        this.linkColor = array.getColor(styleable.RichEditText_linkColor, 0);
        this.linkUnderline = array.getBoolean(styleable.RichEditText_linkUnderline, true);
        this.quoteColor = array.getColor(styleable.RichEditText_quoteColor, 0);
        this.quoteStripeWidth = array.getDimensionPixelSize(styleable.RichEditText_quoteStripeWidth, 0);
        this.quoteGapWidth = array.getDimensionPixelSize(styleable.RichEditText_quoteCapWidth, 0);
        array.recycle();
    }

    public void fromHtml(String source) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        //解析图片需要用解析类
        builder.append(HtmlParser.fromHtml(source, new GlideImageGeter(this, this.glideRequests)));
        this.switchToKnifeStyle(builder, 0, builder.length());
        this.setText(builder);
    }

    protected void switchToKnifeStyle(Editable editable, int start, int end) {
        BulletSpan[] bulletSpans = (BulletSpan[])editable.getSpans(start, end, BulletSpan.class);
        BulletSpan[] var5 = bulletSpans;
        int var6 = bulletSpans.length;

        int var7;
        int spanStart;
        for(var7 = 0; var7 < var6; ++var7) {
            BulletSpan span = var5[var7];
            spanStart = editable.getSpanStart(span);
            spanStart = editable.getSpanEnd(span);
            spanStart = 0 < spanStart && spanStart < editable.length() && editable.charAt(spanStart) == '\n' ? spanStart - 1 : spanStart;
            editable.removeSpan(span);
            MyBulletSpan bulletSpan = new MyBulletSpan(this.bulletColor, this.bulletRadius, this.bulletGapWidth);
            editable.setSpan(bulletSpan, spanStart, spanStart, 33);
        }
        QuoteSpan[] quoteSpans = (QuoteSpan[])editable.getSpans(start, end, QuoteSpan.class);
        QuoteSpan[] var15 = quoteSpans;
        var7 = quoteSpans.length;

        int var18;

        for(var18 = 0; var18 < var7; ++var18) {
            QuoteSpan span = var15[var18];
            spanStart = editable.getSpanStart(span);
            spanStart = editable.getSpanEnd(span);
            spanStart = 0 < spanStart && spanStart < editable.length() && editable.charAt(spanStart) == '\n' ? spanStart - 1 : spanStart;
            editable.removeSpan(span);
            MyQuoteSpan quoteSpan = new MyQuoteSpan(this.quoteColor, this.quoteStripeWidth, this.quoteGapWidth);
            editable.setSpan(quoteSpan, spanStart, spanStart, 33);
        }

        URLSpan[] urlSpans = (URLSpan[])editable.getSpans(start, end, URLSpan.class);
        URLSpan[] var17 = urlSpans;
        var18 = urlSpans.length;

        for(spanStart = 0; spanStart < var18; ++spanStart) {
            URLSpan span = var17[spanStart];
            spanStart = editable.getSpanStart(span);
            int spanEnd = editable.getSpanEnd(span);
            editable.removeSpan(span);
            MyURLSpan urlSpan = new MyURLSpan(span.getURL(), this.linkColor, this.linkUnderline);
            editable.setSpan(urlSpan, spanStart, spanEnd, 33);
        }

    }
}

