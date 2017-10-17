//
// Created by jimec on 10/17/2017.
//

#ifndef JSSHELL_HIGHLIGHTER_H
#define JSSHELL_HIGHLIGHTER_H

#include <android/log.h>
#include <string>
#include <cstring>
#include <functional>

class Highlighter {

private:

    const static int SPACE = 100;
    const static int NEUTRAL = 0;
    const static int STRING = 1;
    const static int COMMENT_SINGE_LINE = 2;
    const static int COMMENT_MULTI_LINE = 3;
    const static int KEYWORD = 4;
    const static int NUMBER = 5;
    const static int HEX_NUMBER = 6;
    const static int HEX_NUMBER_PREFIX = 7;
    const static int DECIMAL_PART_NUMBER = 8;

    const static int KEYWORD_SPAN = 0;
    const static int STRING_SPAN = 1;
    const static int OPERATOR_SPAN = 2;
    const static int COMMENT_SPAN = 3;
    const static int NUMBER_SPAN = 4;


    const static char *KEYWORD_LIST[];
    const static char OPERATOR_LIST[];

    std::function<void(int, int, int)> mPut;
    std::string mCode;

public:

    Highlighter(const std::string &code, std::function<void(int, int, int)> &&put);

    static bool compare(std::string a, std::string b, int start);

    static bool backCompare(std::string a, std::string b, int start);

    void run();

    bool isKeyword(std::string code, int position);

    bool isOperator(char character);

};



#endif //JSSHELL_HIGHLIGHTER_H
