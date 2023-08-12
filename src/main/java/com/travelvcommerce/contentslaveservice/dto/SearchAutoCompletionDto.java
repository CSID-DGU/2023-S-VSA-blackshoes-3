package com.travelvcommerce.contentslaveservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchAutoCompletionDto {
    String videoName;
    String sellerName;

    @Getter
    @Builder
    public static class SearchAutoCompletionListDto {
        private List<String> autoCompletionList;
    }
}
