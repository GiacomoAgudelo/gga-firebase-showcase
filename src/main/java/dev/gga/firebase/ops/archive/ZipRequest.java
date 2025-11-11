package dev.gga.firebase.ops.archive;

import java.util.HashSet;

public record ZipRequest(
        HashSet<String> paths,
        boolean preservePaths,
        String downloadName
) {
}
