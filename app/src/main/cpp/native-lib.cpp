#include <jni.h>
#include <sstream>
#include "NativeHighlighter.h"

extern "C" {

static const char __unused *TAG = "native-lib";
static jmethodID setSpansId;
static NativeHighlighter nativeHighlighter;

/**
 * Initialize highlighter.
 */
JNIEXPORT void JNICALL
Java_io_jimeckerlein_jsshell_editor_Highlighter_initHighlighter(JNIEnv *env, jobject) {

    // Get put method of buffer:
    setSpansId = env->GetMethodID(
            env->FindClass("io/jimeckerlein/jsshell/editor/Highlighter"), "setSpans", "(I[I)V");

}

/**
 * Highlight given code.
 */
JNIEXPORT jboolean JNICALL
Java_io_jimeckerlein_jsshell_editor_Highlighter_findHighlights(JNIEnv *env, jobject instance,
                                                               jstring jCode) {

    // Get code from Java String:
    const char *code = env->GetStringUTFChars(jCode, 0);

    // If code string is empty, the highlighter does not need to run:
    if (strlen(code) == 0) return (jboolean) true;

    nativeHighlighter.run(code);

    auto length = nativeHighlighter.getSpanTypes().size();

    jintArray dataArray = (jintArray) env->NewIntArray((jsize) length * 3);

    // Bulk copy span data into array
    // Data is aligned behind each other: types, starts, ends
    // Each part-array has the exact same size, namely 'length':
    int *dst = (int *) env->GetPrimitiveArrayCritical(dataArray, nullptr);

    memcpy(dst, nativeHighlighter.getSpanTypes().data(), length * sizeof(int));
    memcpy(&dst[length], nativeHighlighter.getSpanStarts().data(), length * sizeof(int));
    memcpy(&dst[2 * length], nativeHighlighter.getSpanEnds().data(), length * sizeof(int));

    env->ReleasePrimitiveArrayCritical(dataArray, dst, 0);

    // Call setSpans() Java method
    env->CallVoidMethod(instance, setSpansId, length, dataArray);

    // Cleanup:
    env->DeleteLocalRef(dataArray);
    env->ReleaseStringUTFChars(jCode, code);
    return (jboolean) true;
}

} // extern "C"
