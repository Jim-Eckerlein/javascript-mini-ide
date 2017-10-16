package io.jim_eckerlein.jsshell.keyboard;

/**
 * Every key needs a reference to its keyboard
 */
interface KeyboardKeyConnection {
    
    void setKeyboard(KeyboardView keyboard);
    
}
