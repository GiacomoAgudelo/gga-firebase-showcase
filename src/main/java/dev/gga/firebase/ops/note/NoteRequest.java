package dev.gga.firebase.ops.note;

import jakarta.validation.constraints.NotBlank;

public record NoteRequest(
@NotBlank String title,
String content) {}
