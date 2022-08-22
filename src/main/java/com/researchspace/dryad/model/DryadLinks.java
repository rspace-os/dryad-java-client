package com.researchspace.dryad.model;

public class DryadLinks {

    private Self self;
    private Next next;
    private Prev prev;
    private First first;
    private Last last;
    private Integer count;
    private Integer total;


    private class Self {
        private String href;
    }
    private class Next {
        private String href;
    }
    private class Prev {
        private String href;
    }
    private class First {
        private String href;
    }
    private class Last {
        private String href;
    }
}
