package org.sonatype.nexus.plugins.ruby;

import java.io.File;
import java.io.IOException;

import de.saumya.mojo.gems.MavenArtifact;

/**
 * This component is the central "ruby interaction" point, and is meant to focus all "ruby related" calls to make it
 * easy to swap out and use proper stuff instead. What we have now is POC nexus-ruby-tools, and gemGenerateIndexes is
 * not implemented. The "proper" stuff should use JRuby and invoke the proper Gem:: classes doing the actual work.
 *
 * @author cstamas
 */
public interface RubyGateway
{
    /**
     * Queries is the MavenArtifact convertable to Gem.
     *
     * @param pom Pom of the artifact.
     * @return true if yes, false otherwise.
     */
    boolean canConvert( MavenArtifact mart );

    /**
     * Just builds proper GEM filename out from information in POM object.
     *
     * @param pom Pom of the artifact.
     * @return
     */
    String getGemFileName( MavenArtifact mart );

    /**
     * Does Maven2 artifact conversion into Gem and writes the Gem to the target file. The file written to target should
     * be a complete Ruby Gem.
     *
     * @param mart
     * @param target
     * @throws IOException
     */
    void createGemFromArtifact( MavenArtifact mart, File target )
        throws IOException;

    /**
     * Does Maven2 artifact conversion into Gem Stub with only a gemspec file and writes the Gem to the target file.
     * The file written to target should be a Ruby Gem without data.tar.gz.
     *
     * @param mart
     * @param target
     * @throws IOException
     */
    void createGemStubFromArtifact(MavenArtifact mart, File target)
        throws IOException;

    /**
     * Creates a Gem::Specification from the Maven2 artifact Pom and writes it into the designated target File as Yaml
     * serialized file (for latter indexer use, this is the gemspec of "lazy" Gems).
     *
     * @param pom
     * @param target
     * @throws IOException
     */
    void createAndWriteGemspec( MavenArtifact mart, File target )
        throws IOException;

    /**
     * Invokes "gem generate_index --directory=${basedir}". Should do the same as the CLI command written above.
     *
     * @param basedir
     * @param if true, update happens, oherwise full reindex
     */
    void gemGenerateIndexes( File basedir, boolean update );

    /**
     * Invokes Nexus modified "gem generate_index --directory=${basedir}". Should do the same as the CLI command written
     * above, but will avoid to crank up GEM files, and will use .gemspec files instead, generated by Nexus.
     *
     * @param basedir
     * @param if true, update happens, oherwise full reindex
     */
    void gemGenerateLazyIndexes( File basedir, boolean update );
}
