package com.researchspace.dryad.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DryadEmbedded {

    @JsonProperty("stash:datasets")
    private List<DryadDataset> datasets;

}
