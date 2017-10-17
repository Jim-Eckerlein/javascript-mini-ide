#include <jni.h>
#include "NativeHighlighter.h"

extern "C" {

JNIEXPORT jboolean JNICALL
Java_io_jimeckerlein_jsshell_MainActivity_test(JNIEnv *env, jobject instance) {

    jclass mainActivityClass = env->FindClass("io/jimeckerlein/jsshell/MainActivity");
    if (nullptr == mainActivityClass) {
        return (jboolean) false;
    }

    jmethodID mid = env->GetMethodID(mainActivityClass, "foo", "([I)V");
    if (nullptr == mid) {
        return (jboolean) false;
    }

    std::vector<int> v{{1, 2, 3, 4, 5, 6, 7, 8}};
    jintArray array = env->NewIntArray((jsize) v.size());

    void *dst = env->GetPrimitiveArrayCritical(array, nullptr);
    memcpy(dst, v.data(), v.size() * sizeof(int));
    env->ReleasePrimitiveArrayCritical(array, dst, 0);

    env->CallVoidMethod(instance, mid, array);
    return (jboolean) true;
}

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

    NativeHighlighter nativeHighlighter{code};

    nativeHighlighter.run();

    for (int i : nativeHighlighter.getSpanTypes()) {
        env->CallVoidMethod(jTypeBuffer, putMethodId, i);
    }
    for (int i : nativeHighlighter.getSpanStarts()) {
        env->CallVoidMethod(jSpanStartBuffer, putMethodId, i);
    }
    for (int i : nativeHighlighter.getSpanEnds()) {
        env->CallVoidMethod(jSpanEndBuffer, putMethodId, i);
    }

    // Cleanup:
    env->ReleaseStringUTFChars(jCode, code);
    env->DeleteLocalRef(bufferClass);
    return (jboolean) true;
}

} // extern "C"
