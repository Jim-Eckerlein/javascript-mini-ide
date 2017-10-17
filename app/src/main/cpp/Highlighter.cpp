//
// Created by jimec on 10/17/2017.
//

#include "Highlighter.h"

const char *Highlighter::KEYWORD_LIST[] = {
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
const char Highlighter::OPERATOR_LIST[] = {
        '+', '*', '/', '{', '}', '(', ')', ',', '|', '!', '?', '%', '^',
        '.', ';', '~', '=', '[', ']', '-', '&', '<', '>', ':', '\\'
};

Highlighter::Highlighter(const std::string &code) {
    mCode = code;
}

bool Highlighter::compare(std::string a, std::string b, int start) {
    return 0 == std::strncmp(&a[start], b.c_str(), b.length());
}

bool Highlighter::backCompare(std::string a, std::string b, int start) {
    return 0 == std::strncmp(&a[start - b.length() + 1], b.c_str(), b.length());
}

void Highlighter::run() {
    mSpanTypes.clear();
    mSpanStarts.clear();
    mSpanEnds.clear();

    int currentTextType = SPACE;
    char c = mCode[0];
    int spanStart = 0;

    // Highlight line:
    for (int i = 0;;) {

        bool reprocessCurrentCharacter = false;

        // Single line comment start
        if (compare(mCode, "//", i) &&
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
        else if (compare(mCode, "/*", i) &&
                 (currentTextType == NEUTRAL || currentTextType == SPACE)) {
            spanStart = i;
            currentTextType = COMMENT_MULTI_LINE;
        }

            // Multi line comment end
        else if (backCompare(mCode, "*/", i) && currentTextType == COMMENT_MULTI_LINE) {
            put(COMMENT_SPAN, spanStart, i + 1);
            currentTextType = SPACE;
        }

            // String starting
        else if (compare(mCode, "'", i) &&
                 (currentTextType == NEUTRAL || currentTextType == SPACE)) {
            spanStart = i;
            currentTextType = STRING;
        }

            // String ending
        else if (backCompare(mCode, "'", i) && currentTextType == STRING) {
            put(STRING_SPAN, spanStart, i + 1);
            currentTextType = SPACE;
        }

            // Keyword start
        else if (std::isalpha(c) && currentTextType == SPACE && isKeyword(mCode, i)
                 && (i == 0 || !std::isalnum(mCode[i - 1]))) {
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
        else if (compare(mCode, "0x", i) && currentTextType == SPACE &&
                 (i > 0 && std::isspace(mCode[i - 1]))) {
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
        if (i > mCode.length()) {
            break;
        } else if (i == mCode.length()) {
            // Append apparently \0 to code, so that spans have the change to end
            c = '\0';
        } else {
            c = mCode[i];
        }
    }
}

bool Highlighter::isKeyword(std::string code, int position) {
    int i;
    for (i = position; i < code.length(); i++) {
        if (!std::isalnum(code[i])) {
            break;
        }
    }
    code = code.substr((unsigned long) position, (unsigned long) (i - position));
    __android_log_write(ANDROID_LOG_ERROR, "Highlighter", code.c_str());
    for (std::string item : KEYWORD_LIST) {
        if (code == item) return true;
    }
    return false;
}

bool Highlighter::isOperator(char character) {
    for (char c : OPERATOR_LIST) {
        if (character == c)
            return true;
    }
    return false;
}

void Highlighter::put(int type, int start, int end) {
    mSpanTypes.push_back(type);
    mSpanStarts.push_back(start);
    mSpanEnds.push_back(end);
}

std::vector<int> Highlighter::getSpanTypes() {
    return mSpanTypes;
}

std::vector<int> Highlighter::getSpanStarts() {
    return mSpanStarts;
}

std::vector<int> Highlighter::getSpanEnds() {
    return mSpanEnds;
}
