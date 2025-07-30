package com.example.myspringboot.Entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "journal_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntry {

    @Id
    private ObjectId id;

    @NotBlank(message = "Title Required")
    private String title;

    @NotBlank(message = "Please fill with content")
    private String content;
    private LocalDateTime date;

    private String userName;

}
