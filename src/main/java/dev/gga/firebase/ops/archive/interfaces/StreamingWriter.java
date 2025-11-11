package dev.gga.firebase.ops.archive.interfaces;

import java.io.IOException;
import java.io.OutputStream;

@FunctionalInterface
public interface StreamingWriter {
    void writeTo(OutputStream out) throws IOException;
}
