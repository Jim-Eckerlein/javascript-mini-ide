package com.example.jimec.javascriptshell;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.example.jimec.javascriptshell.Util.strncmp;

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
    private static final String[] KEYWORDS = {
            "function", "var", "let", "return",
            "if", "for", "while", "do", "switch", "case", "default",
            "break", "continue",
            "new", "true", "false", "null", "undefined",
            "this", "print", "in"
    };
    private static final Pattern HIGHLIGHT_OPERATOR_REGEX = Pattern.compile("([+*/{}(),|.;~=\\[\\]-]|&amp;|&lt;|&gt;)");
    private static final Pattern HIGHLIGHT_NUMBER_REGEX = Pattern.compile("(0x[0-9A-F]+|\\d+)");

    private final ArrayList<Span> mSpans = new ArrayList<>();

    public static void main(String[] args) {
        LineList lines = new LineList();
        HtmlGenerator htmlGenerator = new HtmlGenerator();

        lines.write("print('hello'");

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
                mSpans.add(new Span(Span.LINE, "<tr class='active-line'>"));
            } else {
                mSpans.add(new Span(Span.LINE, "<tr>"));
            }
            mSpans.add(new Span(Span.HTML, "<td class='line-number'>" + (lineNumber + 1) + "</td>" +
                    "<td class='code-line'>" + LINE_PREFIX_HTML));

            // Create indent:
            for (int indent = 0; indent < line.getIndent(); indent++) {
                mSpans.add(new Span(Span.HTML, "    "));
            }

            mSpans.add(new Span(Span.RAW, line.getCode()));
            // Create cursor and raw spans:
            /*if (cursor.mLine != lineNumber) {
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
            }*/

            mSpans.add(new Span(Span.HTML, "</tr>\n"));
        }

        {
            boolean insideMultilineComment = false;
            int commentSurroundingSpan = Span.CODE;
            int currentSpanType = Span.CODE;

            // Expand raw spans to code, string and comment
            for (int iSpan = 0; iSpan < mSpans.size(); iSpan++) {

                if (Span.RAW != mSpans.get(iSpan).mType) continue;

                int i = 0;
                int lastSpanStart = 0;
                String text = mSpans.get(iSpan).mText;
                ArrayList<Span> newSpans = new ArrayList<>();

                try {
                    for (; i < mSpans.get(iSpan).mText.length(); i++) {

                        // Multiline comment start
                        if (strncmp(text, "/*", i)) {
                            if (i != 0) {
                                newSpans.add(new Span(currentSpanType, text.substring(lastSpanStart, i)));
                            }
                            commentSurroundingSpan = currentSpanType;
                            currentSpanType = Span.COMMENT_MULTILINE;
                            lastSpanStart = i;
                            insideMultilineComment = true;
                            continue;
                        }

                        // Multiline comment end
                        if (strncmp(text, "*/", i) && currentSpanType == Span.COMMENT_MULTILINE) {
                            i += 2; // Include */
                            newSpans.add(new Span(currentSpanType, text.substring(lastSpanStart, i)));
                            currentSpanType = commentSurroundingSpan;
                            lastSpanStart = i;
                            insideMultilineComment = false;
                            continue;
                        }

                        if (insideMultilineComment) continue;

                        // Single line comment
                        if (strncmp(text, "//", i)) {
                            if (i != 0) {
                                newSpans.add(new Span(currentSpanType, text.substring(lastSpanStart, i)));
                            }
                            currentSpanType = Span.COMMENT_SINGLE_LINE;
                            lastSpanStart = i;
                            continue;
                        }

                        // String starting
                        if (strncmp(text, "'", i) && currentSpanType == Span.CODE) {
                            if (i != 0) {
                                newSpans.add(new Span(currentSpanType, text.substring(lastSpanStart, i)));
                            }
                            currentSpanType = Span.STRING;
                            lastSpanStart = i;
                            continue;
                        }

                        // String ending
                        if (strncmp(text, "'", i) && currentSpanType == Span.STRING) {
                            if (i != 0) {
                                i++;
                                newSpans.add(new Span(currentSpanType, text.substring(lastSpanStart, i)));
                            }
                            currentSpanType = Span.CODE;
                            lastSpanStart = i;
                        }
                    }
                } catch (StringIndexOutOfBoundsException ignored) {
                    // It is ok if comparisons like */ exceed the current line, they simply should fail and finish the loop
                }

                // Wrap rest
                if (lastSpanStart < text.length()) {
                    newSpans.add(new Span(currentSpanType, text.substring(lastSpanStart)));
                }

                // Generate highlighting HTML of string and comment blocks,
                // code is highlighted inline, without extra spans for operators, keywords, etc.
                for (Span span : newSpans) {
                    if (span.mType == Span.STRING) {
                        span.mText = "<span class='code-highlight-string'>" + span.mText + "</span>";
                    }
                    if (span.mType == Span.COMMENT_SINGLE_LINE) {
                        span.mText = "<span class='code-highlight-comment'>" + span.mText + "</span>";
                    }
                    if (span.mType == Span.COMMENT_MULTILINE) {
                        span.mText = "<span class='code-highlight-comment'>" + span.mText + "</span>";
                    }
                }

                // Replace old raw span by new spans:
                mSpans.remove(iSpan);
                mSpans.addAll(iSpan, newSpans);
            }
        }

        // Expand code spans to support highlighting
        for (Span span : mSpans) {
            if (Span.CODE != span.mType) continue;

            String text = span.mText;

            // Search operators:
            text = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
            text = HIGHLIGHT_OPERATOR_REGEX.matcher(text).replaceAll("<span class='code-highlight-operator'>$1</span>");

            // Search numbers:
            text = HIGHLIGHT_NUMBER_REGEX.matcher(text).replaceAll("<span class='code-highlight-number'>$1</span>");

            // Search keywords:
            for (String keyword : KEYWORDS) {
                text = text.replaceAll("\\b" + keyword + "\\b", "<span class='code-highlight-keyword'>" + keyword + "</span>");
            }

            span.mText = text;
        }

        // Insert cursor
        {
            int line = -1;
            int col = -1;
            LineList.Cursor cursor = lines.getCursor();

            for (Span span : mSpans) {

                if (Span.LINE == span.mType) {
                    line++;
                }
                if (span.mType == Span.LINE || span.mType == Span.HTML || cursor.mLine != line) {
                    continue;
                }

                // Found line with cursor
                for (int c = 0; c <= span.mText.length(); c++) {
                    if (c < span.mText.length() && '<' == span.mText.charAt(c)) {
                        // Skip HTML
                        while ('>' != span.mText.charAt(c)) {
                            c++;
                        }
                        continue;
                    }
                    col++;
                    if (col == cursor.mCol) {
                        // Found cursor
                        span.mText = span.mText.substring(0, c) + CURSOR_HTML + span.mText.substring(c);
                    }
                }
            }
        }

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
        static final int LINE = 10;
        static final int HTML = 1;
        static final int CURSOR = 2;
        static final int CODE = 3;
        static final int STRING = 4;
        static final int COMMENT_SINGLE_LINE = 5;
        static final int COMMENT_MULTILINE = 6;

        int mType;
        String mText;

        Span() {
        }

        Span(int type, String text) {
            mType = type;
            mText = text;
        }

        @Override
        public String toString() {
            switch (mType) {
                case RAW:
                    return "(RAW):" + mText;
                case HTML:
                    return "(HTML):" + mText;
                case CURSOR:
                    return "(CURSOR):" + mText;
                case CODE:
                    return "(CODE):" + mText;
                case STRING:
                    return "(STRING):" + mText;
                case COMMENT_SINGLE_LINE:
                    return "(COMMENT_SINGLE_LINE):" + mText;
                case COMMENT_MULTILINE:
                    return "(COMMENT_MULTILINE):" + mText;
                default:
                    return "(ERROR-TYPE " + mType + "):" + mText;
            }
        }
    }

}
