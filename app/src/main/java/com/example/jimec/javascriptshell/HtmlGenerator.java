package com.example.jimec.javascriptshell;

import java.util.ArrayList;

/**
 * Workflow of the HTML Generator:
 * <p>
 * 1) Create raw and line start/end spans
 * 2) Create cursor span
 * 3) Separate raw spans into code, string and comment, creating HTML spans
 */
public class HtmlGenerator {

    private static final String CURSOR_HTML = "<span class='cursor-container'><span id='cursor'></span></span>";
    private static final String LINE_PREFIX_HTML = "&#xFEFF;";
    final ArrayList<Span> mSpans = new ArrayList<>();

    public static void main(String[] args) {
        LineList lines = new LineList();
        HtmlGenerator htmlGenerator = new HtmlGenerator();

        lines.write("function() {\nprintf('hello world!');\n}");

        System.out.println(htmlGenerator.generateHtml(lines));
    }

    String generateHtml(LineList lines) {
        mSpans.clear();

        // Create initial HTML
        mSpans.add(new Span(Span.HTML, "<!doctype html>" +
                "<html><head>\n" +
                "    <meta charset='utf-8'/>\n" +
                "    <style>*{font-family:monospace;font-size:1.1rem}body,html{color:#111;margin:0;padding:0;width:100%}td{width:auto;padding:0}.code-table{width:100%;border-collapse:collapse}.line-number{color:#999;padding:2px 8px 2px 2px;width:1%;white-space:nowrap}.code-line{white-space:pre;padding-left:8px}.active-line .code-line{background-color:#eee;border-radius:8px}.active-line .line-number{color:#444}.cursor-container{position:relative}#cursor{position:absolute;display:block;top:0;left:0;background-color:#f50;width:2px;height:100%}.code-highlight-keyword{color:#4682b4}.code-highlight-string{color:#9acd32}.code-highlight-comment{color:gray}.code-highlight-operator{color:#ff1493}.code-highlight-number{color:orange}</style>\n" +
                "</head><body>\n" +
                "    <table class='code-table'>\n"
        ));

        // Create line-by-line HTML
        for (int lineNumber = 0; lineNumber < lines.getLines().size(); lineNumber++) {
            Line line = lines.getLines().get(lineNumber);
            LineList.Cursor cursor = lines.getCursor();

            if (cursor.mLine == lineNumber) {
                // This is the active line
                mSpans.add(new Span(Span.HTML, "<tr class='active-line'>"));
            } else {
                mSpans.add(new Span(Span.HTML, "<tr>"));
            }
            mSpans.add(new Span(Span.HTML, "<td class='line-number'>" + (lineNumber + 1) + "</td>" +
                    "<td class='code-line'>" + LINE_PREFIX_HTML));

            // Create indent:
            for (int indent = 0; indent < line.getIndent(); indent++) {
                mSpans.add(new Span(Span.HTML, "    "));
            }

            // Create cursor and raw spans:
            if (cursor.mLine != lineNumber) {
                // This line does not contain the cursor => simply copy whole content into a raw span
                mSpans.add(new Span(Span.RAW, line.getCode()));
            } else if (cursor.mCol == 0) {
                // Cursor is at the beginning of the line
                mSpans.add(new Span(Span.CURSOR, CURSOR_HTML));
                mSpans.add(new Span(Span.RAW, line.getCode()));
            } else if (cursor.mCol == line.getCode().length()) {
                // Cursor is at the end of the line
                mSpans.add(new Span(Span.RAW, line.getCode()));
                mSpans.add(new Span(Span.CURSOR, CURSOR_HTML));
            } else {
                // Cursor is somewhere in the middle of the line
                mSpans.add(new Span(Span.RAW, line.getCode().substring(0, cursor.mCol)));
                mSpans.add(new Span(Span.CURSOR, CURSOR_HTML));
                mSpans.add(new Span(Span.RAW, line.getCode().substring(cursor.mCol)));
            }

            mSpans.add(new Span(Span.HTML, "</tr>\n"));
        }

        // TODO: Expand raw spans to code, string and comment

        // Create final HTML
        mSpans.add(new Span(Span.HTML, "\n</table>\n" +
                "<script>\n" +
                "let cursor = document.getElementById('cursor');\n" +
                "        let old;\n" +
                "        setInterval(() => {\n" +
                "            let now = Math.trunc(new Date().getMilliseconds() / 500);\n" +
                "            if(now !== old) {\n" +
                "                cursor.style.visibility = cursor.style.visibility === 'visible' ? 'hidden' : 'visible';\n" +
                "                old = now;\n" +
                "            }\n" +
                "        }, 100);" +
                "</script>\n" +
                "</body>\n" +
                "</html>"));

        // Concatenate all spans into one string
        StringBuilder sb = new StringBuilder();
        for (Span span : mSpans) {
            sb.append(span.mText);
        }
        return sb.toString();
    }

    void regenerateCursor(LineList lines) {
        // TODO: implement
    }

    private class Span {
        static final int RAW = 0;
        static final int HTML = 1;
        static final int CURSOR = 2;
        static final int CODE = 3;
        static final int STRING = 4;
        static final int COMMENT = 5;

        int mType;
        String mText;

        Span() {
        }

        Span(int type, String text) {
            mType = type;
            mText = text;
        }
    }

}
