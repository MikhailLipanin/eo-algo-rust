package org.eolang.algo;

import java.io.File;

public final class Transformation {

    /**
     * Source location.
     */
    private final File SRC;

    /**
     * Source extension.
     */
    private final String EXT;

    /**
     * Ctor.
     *
     * @param srcs The source
     * @param eo   True, if source has `.eo` extension
     */
    public Transformation(final File srcs, final boolean eo) {
        this.SRC = srcs;
        if (eo) {
            this.EXT = "eo";
        } else {
            this.EXT = "xmir";
        }
    }

    /**
     * Run transformation.
     */
    public void exec() {
        /*
        Full process:
        1) Parse `SRC` to XMIR
        2) Extend XMIR objects via XSL-s in `org.eolang.parser`
        3) Analyze, which objects in XMIR need to be mapped to Rust
        and mark them in XMIR via additional attribute
        4) Apply XSL-s with transformations such objects
        5) Save target XMIR

        required functionality:
        - parsing (EO -> XMIR)
        - applying Train<Shift> to XMIR
        - mapping func between EO-line-number and extended XMIR-line-number
        - downloading objects from reo???
        - ...
        - mapping object that starts in some `line` to `rust` object
         */
    }

}
