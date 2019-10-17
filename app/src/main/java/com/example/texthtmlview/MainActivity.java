package com.example.texthtmlview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xiaoyao.htmltextview.RichTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RichTextView richTextView = (RichTextView) findViewById(R.id.text);
        richTextView.fromHtml("<ul><li>1111111</li></ul><b>11111</b>");
    }
}
