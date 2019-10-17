<blockquote>TextHtmlView大部分文件都来源于：https://github.com/huzhenjie/RichTextEditor</blockquote>
<b>但是因为它只有编辑器没有显示器也就是只有EditText而没有TextView导致在我的项目中有一定的麻烦所以我就给封装了一个TextView</b>
<blockquote>用法</blockquote>
<pre>&ltcom.xiaoyao.htmltextview.RichTextView
        android:id="@+id/text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!"/></pre>
<pre>RichTextView richTextView = (RichTextView) findViewById(R.id.text);
richTextView.fromHtml("&ltul>&ltli>this is li&lt/li>&lt/ul>&ltb>加重&lt/b>");</pre>
