package dev.gga.firebase.ops.folder;

import java.util.List;

public record FolderListing(String prefix, List<String> subfolders, List<FileItem> files) {
}
