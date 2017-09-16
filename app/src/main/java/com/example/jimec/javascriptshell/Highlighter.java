package com.example.jimec.javascriptshell;

import java.security.InvalidParameterException;
import java.util.ArrayList;

class Highlighter {

    private static final String COLOR_KEYWORD = "steelblue";
    private static final String COLOR_STRING = "yellowgreen";
    private static final String COLOR_COMMENT = "gray";
    private static final String COLOR_OPERATOR = "deeppink";
    private static final String COLOR_NUMBER = "orange";
    private static final String CURSOR = "▏";

    private Highlighter() {
    }

    private enum Category {
        Code,
        String,
        MultiLineComment,
        SingleLineComment
    }

    private static class Span {
        String mText;
        Category mCategory;

        Span(String code, Category category) {
            mText = code;
            mCategory = category;
        }
    }

    static class Cursor {
        int mLine = 0;
        int mCol = 0;

        Cursor() {
        }

        Cursor(int line, int col) {
            mLine = line;
            mCol = col;
        }

        void nextCol() {
            mCol++;
        }

        void nextLine() {
            mCol = 0;
            mLine++;
        }

        @Override
        public String toString() {
            return "" + mLine + ":" + mCol;
        }
    }

    /**
     * Masks the special HTML characters.
     * In addition, escape sequences are stripped out.
     * - & => &amp;
     * - < => &lt;
     * - > => &gt;
     *
     * @param text Text possibly containing unmasked HTML characters.
     * @return Same text, but HTML chars safely masked away.
     */
    private static String maskHtmlChars(String text) {
        text = text.replace("&", "&amp;");
        text = text.replace("<", "&lt;");
        text = text.replace(">", "&gt;");
        text = text.replace("\r", "");
        text = text.replace("\n", " <br>");
        return text;
    }

    private static String highlightCode(String text, Cursor cursor, Cursor position) {
        text = maskHtmlChars(text);

        // Search operators:
        text = text.replaceAll("([+*/{}(),|;~.=\\[\\]-]|&amp;|&lt;|gt;)", "<span style=\"color:" + COLOR_OPERATOR + "\">$1</span>");
        // Search numbers:
        text = text.replaceAll("([0-9])", "<span style=\"color:" + COLOR_NUMBER + "\">$1</span>");
        // Search keywords:
        for (String keyword : new String[]{
                "function", "var", "let", "return",
                "if", "for", "while", "do", "switch", "case", "default",
                "break", "continue",
                "new", "true", "false", "null", "undefined",
                "this", "print", "in"
        })
            text = text.replaceAll("\\b" + keyword + "\\b", "<span style=\"color:" + COLOR_KEYWORD + "\">" + keyword + "</span>");

        text = insertCursor(text, cursor, position);
        return text;
    }

    private static String highlightString(String text, Cursor cursor, Cursor position) {
        text = maskHtmlChars(text);
        text = "<span style=\"color:" + COLOR_STRING + "\"><i>" + text + "</i></span>";
        text = insertCursor(text, cursor, position);
        return text;
    }

    private static String highlightComment(String text, Cursor cursor, Cursor position) {
        text = maskHtmlChars(text);
        text = "<span style=\"color:" + COLOR_COMMENT + "\"><i>" + text + "</i></span>";
        text = insertCursor(text, cursor, position);
        return text;
    }

    private static String insertCursor(String text, Cursor cursor, Cursor position) {
        for (int i = 0; i < text.length(); i++) {

            // Check for line-break:
            if(i + 4 < text.length() && "<br>".equals(text.substring(i, i + 4))) {
                i += 3;
                position.nextLine();
                continue;
            }

            char c = text.charAt(i);

            if ('<' == c) {
                // Skip HTML tag
                for (i++; text.charAt(i) != '>'; i++) {
                    if (i == text.length()) {
                        throw new InvalidParameterException("Corrupt HTML tag, does never end");
                    }
                }
                continue;
            }

            if ('&' == c) {
                // Skip HTML masked character
                for (i++; text.charAt(i) != ';'; i++) {
                    if (i == text.length()) {
                        throw new InvalidParameterException("Corrupt HTML masked character, does never end");
                    }
                }
                continue;
            }

            if (position.mLine == cursor.mLine && position.mCol == cursor.mCol) {
                position.nextCol();
                return
                        text.substring(0, i)
                                + "<span style=\"position:relative\">"
                                + c
                                + "<span style=\"color:black; font-style:normal; position:absolute; top:0px; left:0px;\">"
                                + CURSOR
                                + "</span></span>"
                                + (i < text.length() ? text.substring(i + 1) : "");
            }
            position.nextCol();


        }

        return text;
    }

    /**
     * Generates an HTML string version of the supplied plain text, adding highlighting HTML tags.
     *
     * @param plain Plain JavaScript code
     * @return HTML decorated code
     */
    static String toHtml(String plain, Cursor cursor) {
        Category currentCategory = Category.Code;
        Category commentSurroundingCategory = Category.Code;
        ArrayList<Span> spans = new ArrayList<>();
        int last = 0;

        for (int i = 0; i < plain.length(); i++) {
            if (i < plain.length() - 1 && plain.substring(i, i + 1).compareTo("'") == 0) {
                if (currentCategory == Category.Code) {
                    // Code span ends
                    spans.add(new Span(plain.substring(last, i), Category.Code));
                    currentCategory = Category.String;
                    last = i;
                } else if (currentCategory == Category.String) {
                    // String span ends
                    i++; // Include terminating ' into string span
                    spans.add(new Span(plain.substring(last, i), Category.String));
                    currentCategory = Category.Code;
                    last = i;
                }
            } else if (i < plain.length() - 2 && plain.substring(i, i + 2).compareTo("/*") == 0) {
                if (currentCategory == Category.Code) {
                    // MultiLineComment span starts inside a code span
                    commentSurroundingCategory = Category.Code;
                    spans.add(new Span(plain.substring(last, i), Category.Code));
                    currentCategory = Category.MultiLineComment;
                    last = i;
                } else if (currentCategory == Category.String) {
                    // MultiLineComment span starts inside a string span
                    commentSurroundingCategory = Category.String;
                    spans.add(new Span(plain.substring(last, i), Category.String));
                    currentCategory = Category.MultiLineComment;
                    last = i;
                }
            } else if (i < plain.length() - 2 && plain.substring(i, i + 2).compareTo("*/") == 0) {
                // MultiLineComment span ends
                i += 2; // Include terminating */ into comment span
                spans.add(new Span(plain.substring(last, i), Category.MultiLineComment));
                currentCategory = commentSurroundingCategory;
                last = i;
            } else if (i < plain.length() - 2 && plain.substring(i, i + 2).compareTo("//") == 0) {
                if (currentCategory == Category.Code) {
                    // SingleLineComment span starts inside a code span
                    commentSurroundingCategory = Category.Code;
                    spans.add(new Span(plain.substring(last, i), Category.Code));
                    currentCategory = Category.SingleLineComment;
                    last = i;
                } else if (currentCategory == Category.String) {
                    // SingleLineComment span starts inside a string span
                    commentSurroundingCategory = Category.String;
                    spans.add(new Span(plain.substring(last, i), Category.String));
                    currentCategory = Category.SingleLineComment;
                    last = i;
                }
            } else if (i < plain.length() - 1 && plain.substring(i, i + 1).compareTo("\n") == 0) {
                if (currentCategory == Category.SingleLineComment) {
                    // SingleLineComment span ends
                    spans.add(new Span(plain.substring(last, i), Category.SingleLineComment));
                    currentCategory = commentSurroundingCategory;
                    last = i;
                }
            }
        }

        if (last != plain.length() - 1 || plain.length() == 1) {
            // Cleanup:
            spans.add(new Span(plain.substring(last), currentCategory));
        }

        // Build final HTML string by adding HTML spans and add cursor
        Cursor position = new Cursor();
        StringBuilder html = new StringBuilder();
        for (Span span : spans) {
            switch (span.mCategory) {
                case Code:
                    html.append(highlightCode(span.mText, cursor, position));
                    break;
                case String:
                    html.append(highlightString(span.mText, cursor, position));
                    break;
                case MultiLineComment:
                case SingleLineComment:
                    html.append(highlightComment(span.mText, cursor, position));
                    break;
            }
        }
        return html.toString();
    }

    public static void main(String[] args) {
        String code = " ";
        String html = toHtml(code, new Cursor(0, 0));
        System.out.println(html);
    }

}
