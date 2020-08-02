package com.zhuravishkin.springbootasync.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @JsonProperty("tuple_name")
    private String name;
    @JsonProperty("tuple_age")
    private int age;
}
