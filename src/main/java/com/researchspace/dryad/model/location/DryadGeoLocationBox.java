package com.researchspace.dryad.model.location;

import lombok.Data;

@Data
public class DryadGeoLocationBox {
    private String swLongitude;
    private String swLatitude;
    private String neLongitude;
    private String neLatitude;

}
