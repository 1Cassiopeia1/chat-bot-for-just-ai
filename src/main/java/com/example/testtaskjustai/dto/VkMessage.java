package com.example.testtaskjustai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VkMessage {
    @JsonProperty("from_id")
    private int fromId;
    private String text;
    @JsonProperty("peer_id")
    private int peerId;
}
