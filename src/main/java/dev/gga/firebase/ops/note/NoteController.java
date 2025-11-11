package dev.gga.firebase.ops.note;

import dev.gga.firebase.ops.note.entity.NoteDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/v1/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(final NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("")
    public ResponseEntity<NoteDto> createNote(@RequestBody final NoteRequest noteRequest) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(noteService.createNote(noteRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDto> updateNote(@PathVariable("id") final String id, @RequestBody final NoteRequest noteRequest) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(noteService.updateNote(id, noteRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NoteDto> modifyNote(@PathVariable("id") final String id, @RequestBody final NoteRequest noteRequest) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(noteService.modifyNote(id, noteRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable("id") final String id) throws ExecutionException, InterruptedException {
        noteService.deleteNote(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
