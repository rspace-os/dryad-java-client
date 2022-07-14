package model.location;

import lombok.Data;


@Data
public class DryadGeoLocation {

    private String place;
    private DryadGeoLocationPoint point;
    private DryadGeoLocationBox box;

}
