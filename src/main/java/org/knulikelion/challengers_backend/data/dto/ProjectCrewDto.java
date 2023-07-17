package org.knulikelion.challengers_backend.data.dto;

import lombok.*;

public class ProjectCrewDto {


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Info {
        private Long id;
        private String name;
        private String role;
        private String position;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class ProjectCrewRequestDto {
        private Long id;
        private String name;
        private String role;
        private String position;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class ProjectCrewResponseDto {
        private Long id;
        private String name;
        private String role;
        private String position;
    }

}
