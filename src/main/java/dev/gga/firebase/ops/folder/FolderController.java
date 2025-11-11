package dev.gga.firebase.ops.folder;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/folders")
public class FolderController {

    private final FolderService folderService;


    public FolderController(final FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("")
    public ResponseEntity<FolderListing> get(@RequestParam(required = false) final String prefix) {
        return ResponseEntity.ok(folderService.listChildren(prefix));
    }
}
