package com.travelvcommerce.contentslaveservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class GptDto {
    @Data
    @NoArgsConstructor
    public static class GptRequestDto implements Serializable {
        private String model;
        @JsonProperty("max_tokens")
        private Integer maxTokens;
        private Double temperature;
        private List<GptMessage> messages;

        @Builder
        public GptRequestDto(String question) {
            this.model = "gpt-3.5-turbo";
            this.maxTokens = 100;
            this.temperature = 0.0;
            this.messages = new ArrayList<>();
            this.messages.add(new GptMessage("system", "You are a helper for a travel-focused application. You must respond within the provided array only, separating your responses with array. For example, Q. Can you recommend a place to travel? A. ['일본', '동유럽', '서유럽', '동남아', '미국'] It is important to note that you should return between 1 and 5 items from the provided array, with an emphasis on accuracy over quantity. The available array is as follows: ['국내', '중국', '일본', '대만', '홍콩', '태국', '베트남', '서유럽', '남유럽', '동유럽', '북유럽', '동남아', '서남아', '중동', '미국', '캐나다', '중남미', '오세아니아', '남태평양', '아프리카', '자연', '도시', '역사', '문화', '예술', '식도락', '바다', '산', '숲', '모험', '액티비티', '테마파크', '레저', '골프', '건축', '음악', '미술', '전시', '축제', '공연', '휴양', '로맨틱']. You must return a maximum of 5 items and your response must be array like ['a','b','c','d','e']. And You must return item in the array i gave."));
            this.messages.add(new GptMessage("user", question));
        }
    }

    @Data
    @NoArgsConstructor
    public static class GptResponseDto {
        private String id;
        private String object;
        private long created;
        private String model;
        private List<GptChoice> choices;
        private Map<String, Integer> usage;

        @Builder
        public GptResponseDto(String id, String object, long created, String model, List<GptChoice> choices, Map<String, Integer> usage) {
            this.id = id;
            this.object = object;
            this.created = created;
            this.model = model;
            this.choices = choices;
            this.usage = usage;
        }

        @Data
        @NoArgsConstructor
        public static class GptChoice {
            private int index;
            private GptMessage message;
            private String finish_reason;
        }
    }


    @Data
    @NoArgsConstructor
    @Builder
    public static class GptMessage {
        private String role;
        private String content;

        public GptMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}