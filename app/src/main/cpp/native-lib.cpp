#include <jni.h>
#include "Highlighter.h"

extern "C"
JNIEXPORT jboolean JNICALL
Java_io_jimeckerlein_jsshell_editor_Highlighter_findHighlights(
        JNIEnv *env, jobject,
        jstring jCode,
        jobject jTypeBuffer,
        jobject jSpanStartBuffer,
        jobject jSpanEndBuffer) {

    // Get code from Java String:
    const char *code = env->GetStringUTFChars(jCode, 0);

    // If code string is empty, the highlighter does not need to run:
    if (strlen(code) == 0) return (jboolean) true;

    // Get Java reallocating int buffer class:
    jclass bufferClass = env->FindClass("io/jimeckerlein/jsshell/editor/ReallocatingIntBuffer");
    if (nullptr == bufferClass) {
        return (jboolean) false;
    }

    // Get put method of buffer:
    jmethodID putMethodId = env->GetMethodID(bufferClass, "put", "(I)V");
    if (nullptr == putMethodId) {
        env->DeleteLocalRef(bufferClass);
        return (jboolean) false;
    }

    Highlighter nativeHighlighter{
            code,
            [&](int type, int start, int end) {
                env->CallVoidMethod(jTypeBuffer, putMethodId, type);
                env->CallVoidMethod(jSpanStartBuffer, putMethodId, start);
                env->CallVoidMethod(jSpanEndBuffer, putMethodId, end);
            }};

    nativeHighlighter.run();

    // Cleanup:
    env->ReleaseStringUTFChars(jCode, code);
    env->DeleteLocalRef(bufferClass);
    return (jboolean) true;
}
