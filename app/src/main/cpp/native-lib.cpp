#include <jni.h>

extern "C"
JNIEXPORT jint JNICALL
Java_io_jimeckerlein_jsshell_editor_Highlighter_nativeTest(JNIEnv *, jobject, jint x) {
    return x * x;
}
