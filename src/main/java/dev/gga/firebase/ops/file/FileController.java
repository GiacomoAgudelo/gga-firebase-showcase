package dev.gga.firebase.ops.file;

import com.google.cloud.storage.BlobId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.HashSet;

@RestController
@RequestMapping("v1/files")
public class FileController {

    private final FileService fileService;

    private final FileControllerMapper fileControllerMapper;

    public FileController(final FileService fileService, final FileControllerMapper fileControllerMapper) {
        this.fileService = fileService;
        this.fileControllerMapper = fileControllerMapper;
    }


    @GetMapping("")
    public ResponseEntity<HashSet<BlobId>> getFilesMetadataInfo(){
        return ResponseEntity.ok(fileService.retriveAllBlobInfo());
    }

    @GetMapping("/{id:.+}")
    public ResponseEntity<StreamingResponseBody> getFile(@PathVariable("id") final String id){
        var optionalFileDto = fileService.retriveFile(id);
        if(optionalFileDto.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        final FileDto fileDto = optionalFileDto.get();
        return fileControllerMapper.toResponseEntity(fileDto);
    }

    @GetMapping("/{id:.+}/url")
    public ResponseEntity<String> getSignedUrlFile(@PathVariable("id") final String id){
        var optionalFileDto = fileService.retriveSignUrl(id);
        if(optionalFileDto.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        final FileDto fileDto = optionalFileDto.get();
        return ResponseEntity.ok(fileDto.signedUrl());
    }


}
