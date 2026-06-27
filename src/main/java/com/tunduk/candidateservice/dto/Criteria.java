package com.tunduk.candidateservice.dto;

import com.tunduk.candidateservice.model.enums.CriteriaResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Criteria {
    String key;
    CriteriaResult result;
    String comment;
}
