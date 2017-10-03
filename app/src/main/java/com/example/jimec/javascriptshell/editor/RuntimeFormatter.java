package com.example.jimec.javascriptshell.editor;

public class RuntimeFormatter {
    
    static int format(StringBuilder code, int cursorPos) {
        
        int indent = 0;
        int leadingSpaces = 0;
        int leadingSpacesCurrentLine = 0;
        boolean inCurrentLine = true;
        int leadingClosingBraces = 0;
        int tailingOpeningBraces = 0;
        int lineBreakPos = 0;
        
        for (int i = cursorPos; i >= 0; i--) {
            
            char c = code.charAt(i);
            
            if (inCurrentLine) {
                if (' ' == c) {
                    leadingSpacesCurrentLine++;
                }
                else if ('\n' != c) {
                    leadingSpacesCurrentLine = 0;
                }
                
                if ('}' == c) {
                    leadingClosingBraces++;
                }
                else if ('\n' == c) {
                    indent -= leadingClosingBraces;
                    inCurrentLine = false;
                    lineBreakPos = i + 1;
                }
                else if (' ' != c) {
                    leadingClosingBraces = 0;
                }
            }
            
            else {
                if ('{' == c) {
                    tailingOpeningBraces++;
                }
                else if ('}' == c) {
                    tailingOpeningBraces--;
                }
                
                if (c == ' ') {
                    leadingSpaces++;
                }
                else if ('\n' != c) {
                    leadingSpaces = 0;
                }
                if (c == '\n') {
                    break;
                }
            }
            
        }
        
        indent += tailingOpeningBraces;
        indent += leadingSpaces / 4;
        
        // Indent cannot be negative:
        indent = Math.max(0, indent);
        
        // Remove leading spaces of the current line:
        code.delete(lineBreakPos, lineBreakPos + leadingSpacesCurrentLine);
        
        // Insert indent:
        for (int in = 0; in < indent; in++) {
            code.insert(lineBreakPos, "    ");
        }
        
        return cursorPos - leadingSpacesCurrentLine + indent * 4;
    }
    
}
