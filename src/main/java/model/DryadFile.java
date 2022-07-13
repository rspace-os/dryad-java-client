package model;

import lombok.Data;

@Data
public class DryadFile {

    private String path;
    private Integer size;
    private String mimeType;
    private String status;


}
