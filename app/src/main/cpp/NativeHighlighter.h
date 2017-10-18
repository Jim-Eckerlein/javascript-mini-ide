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

class NativeHighlighter {

private:

    static const int SPACE = 100;
    static const int NEUTRAL = 0;
    static const int STRING = 1;
    static const int COMMENT_SINGE_LINE = 2;
    static const int COMMENT_MULTI_LINE = 3;
    static const int KEYWORD = 4;
    static const int NUMBER = 5;
    static const int HEX_NUMBER = 6;
    static const int HEX_NUMBER_PREFIX = 7;
    static const int DECIMAL_PART_NUMBER = 8;

    static const int KEYWORD_SPAN = 0;
    static const int STRING_SPAN = 1;
    static const int OPERATOR_SPAN = 2;
    static const int COMMENT_SPAN = 3;
    static const int NUMBER_SPAN = 4;


    static const char *KEYWORD_LIST[];
    static const char OPERATOR_LIST[];

    std::string mCode;
    std::vector<int> mSpanTypes, mSpanStarts, mSpanEnds;

    static const char __unused *TAG;

public:

    NativeHighlighter(const std::string &code);

    void run();

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
