package com.buck.vsplay.global.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleResponseDto<T> {

    Integer status;
    T data;

    public SingleResponseDto(Integer status) {this.status = status;}

}
