package dev.gga.firebase.ops.note;

import com.google.cloud.firestore.DocumentSnapshot;
import dev.gga.firebase.ops.note.entity.NoteDto;
import dev.gga.firebase.ops.note.mapper.NoteDtoMapper;
import dev.gga.firebase.ops.repository.FirestoreRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class NoteService {

    private static final String COLLECTION = "notes";
    private final FirestoreRepository firestoreRepository;
    private final NoteDtoMapper noteDtoMapper;

    public NoteService(final FirestoreRepository firestoreRepository, final NoteDtoMapper noteDtoMapper) {
        this.firestoreRepository = firestoreRepository;
        this.noteDtoMapper = noteDtoMapper;
    }

    public NoteDto createNote(final NoteRequest noteRequest) throws ExecutionException, InterruptedException {
        var data = noteDtoMapper.toMap(noteRequest);
        String noteId = firestoreRepository.create(COLLECTION, data);
        DocumentSnapshot documentSnapshot = firestoreRepository.get(COLLECTION, noteId);
        return noteDtoMapper.toDto(documentSnapshot);
    }

    public NoteDto updateNote(final String id, final NoteRequest noteRequest) throws ExecutionException, InterruptedException {
        var map = noteDtoMapper.toMap(noteRequest);
        firestoreRepository.update(COLLECTION, id, map);
        DocumentSnapshot documentSnapshot = firestoreRepository.get(COLLECTION, id);
        return noteDtoMapper.toDto(documentSnapshot);
    }

    public NoteDto modifyNote(final String id, final NoteRequest noteRequest) throws ExecutionException, InterruptedException {
        var map = noteDtoMapper.toMapModify(noteRequest, id);
        firestoreRepository.modify(COLLECTION, id, map);
        DocumentSnapshot documentSnapshot = firestoreRepository.get(COLLECTION, id);
        return noteDtoMapper.toDto(documentSnapshot);
    }

    public void deleteNote(final String id) throws ExecutionException, InterruptedException {
        firestoreRepository.delete(COLLECTION, id);
    }
}
