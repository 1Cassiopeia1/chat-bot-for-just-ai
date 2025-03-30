package com.example.testtaskjustai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VkCallbackRequest {

    private String type;
    private Object object;
    @JsonProperty("group_id")
    private Integer groupId;

}
