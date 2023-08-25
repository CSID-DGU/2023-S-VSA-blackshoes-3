package com.travelvcommerce.personalizedservice.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "view_tags")
public class ViewTag {
    @Id
    private String _id;

    private String userId;

    private List<Set<String>> tagIdSetList;

    private String tagId;

    @CreatedDate
    private String createdAt;

    @LastModifiedDate
    private String updatedAt;

    public void appendTagIdSet(Set<String> tagIdSet) {
        this.tagIdSetList.add(tagIdSet);
        this.updatedAt = Timestamp.valueOf(LocalDateTime.now()).toString();
    }

    public void initializeTagIdSet() {
        this.tagIdSetList = new ArrayList<>();
    }
}