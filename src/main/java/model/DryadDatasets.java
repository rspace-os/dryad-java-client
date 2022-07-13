package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DryadDatasets {

    @JsonProperty("_links")
    private DryadLinks links;
    @JsonProperty("_embedded")
    private DryadEmbedded embedded;



}
