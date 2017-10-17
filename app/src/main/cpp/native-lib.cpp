#include <jni.h>

static int SPACE = 100;
static int NEUTRAL = 0;
static int STRING = 1;
static int COMMENT_SINGE_LINE = 2;
static int COMMENT_MULTI_LINE = 3;
static int KEYWORD = 4;
static int NUMBER = 5;
static int HEX_NUMBER = 6;
static int HEX_NUMBER_PREFIX = 7;
static int DECIMAL_PART_NUMBER = 8;

extern "C"
JNIEXPORT jboolean JNICALL
Java_io_jimeckerlein_jsshell_editor_Highlighter_findHighlights(
        JNIEnv *env, jobject jInstance,
        jstring jCode,
        jobject jPositionBuffer,
        jobject jTypeBuffer)
{
    jclass bufferClass = env->FindClass("io/jimeckerlein/jsshell/editor/ReallocatingIntBuffer");
    if(nullptr == bufferClass) {
        return (jboolean) false;
    }

    jmethodID put = env->GetMethodID(bufferClass, "put", "(I)V");
    if(nullptr == put) {
        env->DeleteLocalRef(bufferClass);
        return (jboolean) false;
    }

    env->CallVoidMethod(jPositionBuffer, put, 27);

    const char *code = env->GetStringUTFChars(jCode, 0);

    env->ReleaseStringUTFChars(jCode, code);
    env->DeleteLocalRef(bufferClass);
    return (jboolean) true;
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_jimeckerlein_jsshell_editor_Highlighter_nativeTest(JNIEnv *, jobject, jint x) {
    return x * x;
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_jimeckerlein_jsshell_JNITest_sum(JNIEnv *env, jobject instance, jint x, jint y) {
    return x + y;
}
