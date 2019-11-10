package com.github.nnnnusui.minecraft.customtitle;

import java.io.ByteArrayOutputStream;

public class CompileTimeException extends Exception {
    public CompileTimeException(String message){
        super(message);
    }
    public CompileTimeException(ByteArrayOutputStream stream){
        this(new String(stream.toByteArray()));
    }
}
