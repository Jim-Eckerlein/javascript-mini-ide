#include "NativeHighlighter.h"

const char __unused *NativeHighlighter::TAG = "NativeHighlighter";

const char *NativeHighlighter::KEYWORD_LIST[] = {
        "print", "sleep", "exit",
        "NaN", "Infinity", "arguments", "await", "break", "case", "catch",
        "class", "const", "continue", "debugger", "default", "delete", "do",
        "else", "enum", "eval", "export", "extends", "false", "",
        "finally", "for", "function", "if", "implements", "import",
        "in", "instanceof", "interface", "let", "new",
        "null", "package", "private", "protected", "public", "return", "static",
        "super", "switch", "this", "throw", "true",
        "try", "typeof", "var", "void", "while", "with", "yield"
};
const char NativeHighlighter::OPERATOR_LIST[] = {
        '+', '*', '/', '{', '}', '(', ')', ',', '|', '!', '?', '%', '^',
        '.', ';', '~', '=', '[', ']', '-', '&', '<', '>', ':', '\\'
};

bool NativeHighlighter::compare(std::string a, std::string b, int start) {
    return 0 == std::strncmp(&a[start], b.c_str(), b.length());
}

bool NativeHighlighter::backCompare(std::string a, std::string b, int start) {
    return 0 == std::strncmp(&a[start - b.length() + 1], b.c_str(), b.length());
}

void NativeHighlighter::run(const std::string &code) {
    mSpanTypes.clear();
    mSpanStarts.clear();
    mSpanEnds.clear();

    int currentTextType = SPACE;
    char c = code[0];
    int spanStart = 0;

    // Highlight line:
    for (int i = 0;;) {

        bool reprocessCurrentCharacter = false;

        // Skip continues chunks of white spaces
        if (currentTextType == SPACE && std::isspace(c));

            // Skip continues chunks of neutral text
        else if (currentTextType == NEUTRAL && std::isalnum(c));

            // Single line comment start
        else if (compare(code, "//", i) &&
                 (currentTextType == NEUTRAL || currentTextType == SPACE)) {
            spanStart = i;
            currentTextType = COMMENT_SINGE_LINE;
        }

            // Single line comment end
        else if (currentTextType == COMMENT_SINGE_LINE && c == '\n') {
            put(COMMENT_SPAN, spanStart, i + 1);
            currentTextType = SPACE;
        }

            // Multi line comment start
        else if (compare(code, "/*", i) &&
                 (currentTextType == NEUTRAL || currentTextType == SPACE)) {
            spanStart = i;
            currentTextType = COMMENT_MULTI_LINE;
        }

            // String ending at line ending (single and double quoted)
        else if ('\n' == c &&
                 (currentTextType == STRING_SINGLE_QUOTED ||
                  currentTextType == STRING_DOUBLE_QUOTED)
                ) {
            put(STRING_SPAN, spanStart, i + 1);
            currentTextType = SPACE;
        }

            // Multi line comment end
        else if (backCompare(code, "*/", i) && currentTextType == COMMENT_MULTI_LINE) {
            put(COMMENT_SPAN, spanStart, i + 1);
            currentTextType = SPACE;
        }

            // String (double quoted) starting
        else if (compare(code, "\"", i) &&
                 (currentTextType == NEUTRAL || currentTextType == SPACE)) {
            spanStart = i;
            currentTextType = STRING_DOUBLE_QUOTED;
        }

            // String (double quoted) ending
        else if (backCompare(code, "\"", i) && currentTextType == STRING_DOUBLE_QUOTED) {
            put(STRING_SPAN, spanStart, i + 1);
            currentTextType = SPACE;
        }

            // String (single quoted) starting
        else if (compare(code, "'", i) &&
                 (currentTextType == NEUTRAL || currentTextType == SPACE)) {
            spanStart = i;
            currentTextType = STRING_SINGLE_QUOTED;
        }

            // String (single quoted) ending
        else if (backCompare(code, "'", i) && currentTextType == STRING_SINGLE_QUOTED) {
            put(STRING_SPAN, spanStart, i + 1);
            currentTextType = SPACE;
        }

            // Keyword start
        else if (std::isalpha(c) && currentTextType == SPACE && isKeyword(code, i)
                 && (i == 0 || !std::isalnum(code[i - 1]))) {
            currentTextType = KEYWORD;
            spanStart = i;
        }

            // Keyword end
        else if (!std::isalnum(c) && currentTextType == KEYWORD) {
            put(KEYWORD_SPAN, spanStart, i);
            currentTextType = SPACE;
            reprocessCurrentCharacter = true;
        }

            // Hex number start
        else if (compare(code, "0x", i) && currentTextType == SPACE) {
            currentTextType = HEX_NUMBER_PREFIX;
            spanStart = i;
        }

            // Hex number start x
        else if (currentTextType == HEX_NUMBER_PREFIX && c == 'x') {
            currentTextType = HEX_NUMBER;
        }

            // Hex number end
        else if (currentTextType == HEX_NUMBER && (!std::isdigit(c)
                                                   && !(c >= 'a' && c <= 'f')
                                                   && !(c >= 'A' && c <= 'F')
        )) {
            put(NUMBER_SPAN, spanStart, i);
            currentTextType = SPACE;
            reprocessCurrentCharacter = true;
        }

            // Number start
        else if (std::isdigit(c) && currentTextType == SPACE) {
            currentTextType = NUMBER;
            spanStart = i;
        }

            // Decimal part of number
        else if (c == '.' && currentTextType == NUMBER) {
            currentTextType = DECIMAL_PART_NUMBER;
        }

            // Number end
        else if (!std::isdigit(c) &&
                 (currentTextType == NUMBER || currentTextType == DECIMAL_PART_NUMBER)) {
            currentTextType = NEUTRAL;
            reprocessCurrentCharacter = true;
            put(NUMBER_SPAN, spanStart, i);
        }

            // Operator
        else if (!std::isspace(c) && isOperator(c) &&
                 (currentTextType == NEUTRAL || currentTextType == SPACE)) {
            put(OPERATOR_SPAN, i, i + 1);
            currentTextType = SPACE;
        }

            // Neutral text
        else if (currentTextType == SPACE && std::isalnum(c)) {
            currentTextType = NEUTRAL;
        }

            // Space
        else if (currentTextType == NEUTRAL && std::isspace(c)) {
            currentTextType = SPACE;
        }

        if (reprocessCurrentCharacter) {
            continue;
        }

        i++;
        if (i > code.length()) {
            break;
        } else if (i == code.length()) {
            // Append apparently \0 to code, so that spans have the change to end
            c = '\0';
        } else {
            c = code[i];
        }
    }
}

bool NativeHighlighter::isKeyword(std::string code, int position) {
    int i = position;
    for (i; i < code.length() && std::isalnum(code[i]); i++);
    size_t length = (size_t) (i - position);
    for (std::string item : KEYWORD_LIST) {
        if (length == item.length() && 0 == std::strncmp(&code[position], item.c_str(), length)) {
            return true;
        }
    }
    return false;
}

bool NativeHighlighter::isOperator(char character) {
    for (char c : OPERATOR_LIST) {
        if (character == c)
            return true;
    }
    return false;
}

void NativeHighlighter::put(int type, int start, int end) {
    mSpanTypes.push_back(type);
    mSpanStarts.push_back(start);
    mSpanEnds.push_back(end);
}

std::vector<int> NativeHighlighter::getSpanTypes() {
    return mSpanTypes;
}

std::vector<int> NativeHighlighter::getSpanStarts() {
    return mSpanStarts;
}

std::vector<int> NativeHighlighter::getSpanEnds() {
    return mSpanEnds;
}
