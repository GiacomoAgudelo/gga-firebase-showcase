package dev.gga.firebase.ops.folder;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import dev.gga.firebase.ops.gcs.infra.adapter.ObjectStorage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FolderService {

    private final ObjectStorage objectStorage;

    public FolderService(final ObjectStorage objectStorage) {
        this.objectStorage = objectStorage;
    }


    public FolderListing listChildren(String rawPrefix) {
        String prefix = normDir(rawPrefix); // "docs/" | "" per root
        Page<Blob> page = objectStorage.getBlobByPrefix(prefix);

        Set<String> subfolders = new TreeSet<>();
        List<FileItem> files = new ArrayList<>();

        for (Blob b : page.iterateAll()) {
            String name = b.getName();
            String rest = name.substring(prefix.length());
            int slash = rest.indexOf('/');
            if (slash >= 0) {
                subfolders.add(rest.substring(0, slash + 1)); // es. "2025/"
            } else {
                files.add(new FileItem(name, Optional.ofNullable(b.getSize()).orElse(0L), b.getContentType()));
            }
        }

        files.sort(Comparator.comparing(FileItem::objectName));
        return new FolderListing(prefix, new ArrayList<>(subfolders), files);
    }

    private static String normDir(String p) {
        if (p == null || p.isBlank()) return "";
        String s = p.replace('\\','/');
        if (s.startsWith("/")) s = s.substring(1);
        return s.endsWith("/") ? s : s + "/";
    }
}
