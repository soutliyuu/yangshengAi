package com.sp.yangshengai.pojo.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class EchartsResult<X,D> implements Serializable {

    @JsonProperty("xAxis")
    private List<X> xAxis = new ArrayList<>();

    private List<SeriesCount<D>> seriesCount = new ArrayList<>();





    @Data
    public static class SeriesCount<D> implements Serializable {

        private String name;

        private Double value = 0.0;


    }
}
