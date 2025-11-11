package dev.gga.firebase.ops.note.mapper;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import dev.gga.firebase.ops.note.NoteRequest;
import dev.gga.firebase.ops.note.entity.NoteDto;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class NoteDtoMapper {

    public NoteDto toDto(final DocumentSnapshot d){
        String id = d.getId();
        String title = d.getString("title");
        String content = d.getString("content");
        Timestamp cts = d.getTimestamp("createdAt");
        Timestamp uts = d.getTimestamp("updatedAt");
        Instant createdAt = cts != null ? cts.toSqlTimestamp().toInstant() : null;
        Instant updatedAt = uts != null ? uts.toSqlTimestamp().toInstant() : null;
        return new NoteDto(id, title, content, createdAt, updatedAt);
    }

    public Map<String, Object> toMap(final NoteRequest noteRequest){
        Map<String, Object> map = new HashMap<>();
        map.put("title", noteRequest.title());
        map.put("content", noteRequest.content());
        map.put("createdAt", FieldValue.serverTimestamp());
        map.put("updatedAt", FieldValue.serverTimestamp());
        return map;
    }

    public Map<String, Object> toMapModify(final NoteRequest noteRequest, final String id){
        Map<String, Object> map = new HashMap<>();
        if(Objects.nonNull(noteRequest.title()) && !noteRequest.title().isBlank()){
            map.put("title", noteRequest.title());
        }
        if(Objects.nonNull(noteRequest.content()) && !noteRequest.content().isBlank()){
            map.put("content", noteRequest.content());
        }
        map.put("updatedAt", FieldValue.serverTimestamp());
        return map;
    }
}
