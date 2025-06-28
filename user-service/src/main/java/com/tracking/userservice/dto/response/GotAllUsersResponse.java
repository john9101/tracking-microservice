package com.tracking.userservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter @Setter
public class GotAllUsersResponse {
    private List<Item> items;

    @Builder
    @Getter @Setter
    public static class Item{
        private Long id;
        private String username;
        private String name;
        private String email;
    }
}
