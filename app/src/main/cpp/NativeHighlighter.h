#include <stdint.h>//
// Created by jimec on 10/17/2017.
//

#ifndef JSSHELL_HIGHLIGHTER_H
#define JSSHELL_HIGHLIGHTER_H

#include <android/log.h>
#include <string>
#include <cstring>
#include <functional>
#include <vector>

/**
 * C++ implementation of the highlighter.
 */
class NativeHighlighter {

private:

    enum {
        SPACE,
        NEUTRAL,
        STRING_SINGLE_QUOTED,
        STRING_DOUBLE_QUOTED,
        STRING_BROKEN_SINGLE_QUOTED,
        STRING_BROKEN_DOUBLE_QUOTED,
        TEMPLATE_STRING,
        TEMPLATE_STRING_ARGUMENT_HEAD,
        TEMPLATE_STRING_ARGUMENT,
        COMMENT_SINGE_LINE,
        COMMENT_MULTI_LINE,
        KEYWORD,
        NUMBER,
        HEX_NUMBER,
        HEX_NUMBER_PREFIX,
        DECIMAL_PART_NUMBER
    };

    /**
     * Integer constants mapping to spans used on Java side.
     * Must match up with Java constants.
     */
    static const int KEYWORD_SPAN = 0;
    static const int STRING_SPAN = 1;
    static const int OPERATOR_SPAN = 2;
    static const int COMMENT_SPAN = 3;
    static const int NUMBER_SPAN = 4;


    static const char *KEYWORD_LIST[];
    static const char OPERATOR_LIST[];

    std::vector<int> mSpanTypes, mSpanStarts, mSpanEnds;

    static const char __unused *TAG;

public:

    /**
     * Actual function performing the code parse and generates Android Text Spans.
     * @param code
     */
    void run(const std::string &code);

    std::vector<int> getSpanTypes();

    std::vector<int> getSpanStarts();

    std::vector<int> getSpanEnds();

private:

    void put(int type, int start, int end);

    bool isKeyword(std::string code, int position);

    bool isOperator(char character);

    static bool compare(std::string a, std::string b, int start);

    static bool backCompare(std::string a, std::string b, int start);

};


#endif //JSSHELL_HIGHLIGHTER_H
