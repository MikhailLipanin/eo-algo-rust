package org.eolang.algo;

import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.yegor256.xsline.Shift;
import com.yegor256.xsline.StClasspath;
import com.yegor256.xsline.StEndless;
import com.yegor256.xsline.TrDefault;
import com.yegor256.xsline.Train;
import com.yegor256.xsline.Xsline;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import org.cactoos.io.InputOf;
import org.cactoos.io.OutputTo;
import org.eolang.parser.Syntax;
import org.eolang.parser.XMIR;

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
     * Directory separator.
     */
    private final char sep;

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
        this.sep = File.separatorChar;
    }

    /**
     * Applies train of XSL-transformations.
     *
     * @param xml XML
     * @return XML
     */
    public static XML applyTrain(final XML xml) {
        final Train<Shift> train = new TrDefault<Shift>()
            .with(new StEndless(new StClasspath("/org/eolang/algo/memory-write.xsl")))
            .with(new StEndless(new StClasspath("/org/eolang/algo/memory-plus.xsl")))
            .with(new StEndless(new StClasspath("/org/eolang/algo/memory-init.xsl")));
        return new Xsline(train).pass(xml);
    }

    /**
     * Run transformation.
     */
    public void exec() throws IOException {
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
        final File dir = new File(
            String.format(
                "%s%cgenerated",
                this.SRC.toString().substring(0, this.SRC.toString().lastIndexOf(this.sep)), this.sep
            )
        );
        final String filename = this.SRC.getName()
            .substring(0, this.SRC.getName().lastIndexOf('.'));
        final File input = this.SRC;
        if (dir.exists()) {
            Transformation.deleteDirectory(dir);
        }
        dir.mkdir();
        final XML before;
        if ("eo".equals(this.EXT)) {
            before = Transformation.getParsedXml(Files.readString(input.toPath()));
        } else {
            before = Transformation.getParsedXml(new XMLDocument(Files.readString(input.toPath())));
        }
        Logger.info(this, "XMIR before transformations:\n%s", before);
        System.out.println(before);
        System.out.println("================");
        final XML after = Transformation.applyTrain(before);
        Logger.info(this, "XMIR after transformations:\n%s", after);
        System.out.println(after);
        final String ret;
        final File output;
        if ("eo".equals(this.EXT)) {
            ret = new XMIR(after).toEO();
            output = new File(
                String.format(
                    "%s%c%s_transformed.%s",
                    dir.getPath(), this.sep, filename, "eo"
                )
            );
        } else {
            ret = after.toString();
            output = new File(
                String.format(
                    "%s%c%s_transformed.%s",
                    dir.getPath(), this.sep, filename, "xmir"
                )
            );
        }
        output.createNewFile();
        try (FileWriter out = new FileWriter(output.getPath())) {
            out.write(ret);
            out.flush();
        }
    }

    /**
     * Takes XMIR-source as input,
     * converts it to ".eo" and calls overridden method.
     *
     * @param xml XML ".xmir" source
     * @return XML
     * @throws IOException When Parsing EO fails
     */
    public static XML getParsedXml(final XML xml) throws IOException {
        return Transformation.getParsedXml(
            new XMIR(xml).toEO()
        );
    }

    /**
     * Takes EO-source as input,
     * converts it to ".xmir" and applies "wrap-method-calls.xsl".
     *
     * @param source String EO-source
     * @return XML
     * @throws IOException When Parsing EO fails
     */
    public static XML getParsedXml(final String source) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new Syntax(
            "scenario",
            new InputOf(String.format("%s\n", source)),
            new OutputTo(baos)
        ).parse();
        final XML xml = new XMLDocument(baos.toByteArray());
        baos.close();
        return new Xsline(
            new TrDefault<Shift>()
                .with(new StClasspath("/org/eolang/parser/wrap-method-calls.xsl"))
        ).pass(xml);
    }

    /**
     * Recursively deletes given directory.
     *
     * @param directory File directory to delete
     * @return True if given directory has been deleted successfully
     */
    public static boolean deleteDirectory(final File directory) {
        boolean ret = true;
        if (directory.isDirectory()) {
            final File[] files = directory.listFiles();
            if (files != null) {
                for (final File file : files) {
                    ret &= Transformation.deleteDirectory(file);
                }
            }
        }
        ret &= directory.delete();
        return ret;
    }

}
