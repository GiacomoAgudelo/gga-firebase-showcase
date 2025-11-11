package dev.gga.firebase.ops.archive;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/v1/archives")
public class ArchiveController {

    private final ArchiveService archiveService;

    public ArchiveController(final ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @PostMapping(produces = "application/zip")
    public ResponseEntity<StreamingResponseBody> createZip(@RequestBody final ZipRequest req){
        var streamingResponseBody = archiveService.createZip(req);
        String downloadName = (req.downloadName() != null && !req.downloadName().isBlank())
                ? req.downloadName()
                : "files.zip";

        ContentDisposition cd = ContentDisposition.attachment().filename(downloadName).build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, cd.toString())
                .contentType(MediaType.valueOf("application/zip"))
                .body(streamingResponseBody);
    }


}
