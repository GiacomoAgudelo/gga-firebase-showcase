package dev.gga.firebase.ops.archive;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import dev.gga.firebase.ops.archive.interfaces.StreamingWriter;
import dev.gga.firebase.ops.gcs.infra.proxy.BucketProxy;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ArchiveService {

    private final BucketProxy bucketProxy;

    public ArchiveService(BucketProxy bucketProxy) {
        this.bucketProxy = bucketProxy;
    }


    public StreamingWriter zipWriter(List<String> paths, boolean preservePaths) {
        List<String> snapshot = (paths == null) ? List.of() : List.copyOf(paths);
        return out -> writeZip(out, snapshot, preservePaths);
    }


    private void writeZip(OutputStream out, List<String> paths, boolean preservePaths) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            Set<String> usedNames = new HashSet<>();
            ByteBuffer buf = ByteBuffer.allocate(1024 * 1024); // 1MB

            for (String raw : paths) {
                if (raw == null || raw.isBlank()) continue;
                String objectName = sanitize(raw);
                Optional<Blob> optionalBlob = bucketProxy.getBlobById(objectName);
                if (optionalBlob.isEmpty()){
                    continue; // oppure lancia se preferisci fallire
                }
                var file = optionalBlob.get();
                String entryName = toZipEntryName(objectName, preservePaths, usedNames);

                ZipEntry entry = new ZipEntry(entryName);
                Long size = file.getSize();
                if (size != null && size >= 0) entry.setSize(size);
                zos.putNextEntry(entry);

                try (ReadChannel reader = file.reader()) {
                    int read;
                    while ((read = reader.read(buf)) > 0) {
                        zos.write(buf.array(), 0, read);
                        buf.clear();
                    }
                }
                zos.closeEntry();
            }
            zos.finish();
            zos.flush();
        }
    }

    private static String sanitize(String path) {
        String s = path.replace('\\','/').replaceAll("^/+","");
        return s;
    }

    private static String baseName(String objectName) {
        String s = objectName.replace('\\','/');
        int last = s.lastIndexOf('/');
        return (last >= 0 && last < s.length() - 1) ? s.substring(last + 1) : s;
    }

    private static String toZipEntryName(String objectName, boolean preservePaths, Set<String> used) {
        String candidate = preservePaths ? objectName : baseName(objectName);
        // evita duplicati: aggiungi suffisso (1), (2), ...
        if (!used.contains(candidate)) {
            used.add(candidate);
            return candidate;
        }
        String name = candidate;
        String ext = "";
        int dot = candidate.lastIndexOf('.');
        if (dot > 0) { name = candidate.substring(0, dot); ext = candidate.substring(dot); }
        int i = 1;
        String withIdx;
        do { withIdx = name + " (" + i++ + ")" + ext; } while (used.contains(withIdx));
        used.add(withIdx);
        return withIdx;
    }

    public StreamingResponseBody createZip(final ZipRequest req) {
        var streamingWriter = this.zipWriter(req.paths().stream().toList(), req.preservePaths());
        return streamingWriter::writeTo;
    }
}
