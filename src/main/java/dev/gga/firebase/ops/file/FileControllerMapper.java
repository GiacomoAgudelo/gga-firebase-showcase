package dev.gga.firebase.ops.file;

import com.google.cloud.storage.Blob;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Component
public class FileControllerMapper {

    public ResponseEntity<StreamingResponseBody> toResponseEntity(final FileDto fileDto) {
        var file = fileDto.file();
        var id = fileDto.id();
        var body = fileDto.streamingResponseBody();

        var size = file.getSize();
        var cd = this.buildContentDisposition(id);
        var contentType = this.getContentType(file);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(size)
                .header(HttpHeaders.CONTENT_DISPOSITION, cd.toString())
                .body(body);
    }

    public ContentDisposition buildContentDisposition(final String id) {
        var filename = id.contains("/") ? id.substring(id.lastIndexOf('/') + 1) : id;
        ContentDisposition cd = ContentDisposition
                .inline()
                .filename(filename).build();
        return cd;
    }

    private String getContentType(final Blob file) {
        return file.getContentType() != null ? file.getContentType()
                : MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
}
