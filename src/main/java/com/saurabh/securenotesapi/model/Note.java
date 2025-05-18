package com.saurabh.securenotesapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Note {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    private String ownerUsername;
}
