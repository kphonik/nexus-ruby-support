package org.sonatype.nexus.plugins.ruby.fs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.inject.Named;
import javax.inject.Singleton;

import org.codehaus.plexus.util.IOUtil;
import org.sonatype.nexus.proxy.ItemNotFoundException;
import org.sonatype.nexus.proxy.item.ContentGenerator;
import org.sonatype.nexus.proxy.item.ContentLocator;
import org.sonatype.nexus.proxy.item.PreparedContentLocator;
import org.sonatype.nexus.proxy.item.StorageFileItem;
import org.sonatype.nexus.proxy.repository.Repository;

@Singleton
@Named( GzipContentGenerator.ID )
public class GzipContentGenerator implements ContentGenerator {

    public static final String ID = "GzipContentGenerator";
    
    @Override
    public String getGeneratorId() {
        return ID;
    }

    @Override
    public ContentLocator generateContent(Repository repository, String path,
            StorageFileItem item) throws ItemNotFoundException {
        try {
            ByteArrayOutputStream gzipped = new ByteArrayOutputStream();
            OutputStream out = new GZIPOutputStream( gzipped );
            IOUtil.copy( item.getInputStream(), out );
            out.close();
            gzipped.close();

            return new PreparedContentLocator( new ByteArrayInputStream( gzipped.toByteArray() ),
                                               "application/x-gzip",
                                               gzipped.toByteArray().length );
        } catch (IOException e) {
            throw new ItemNotFoundException(item.getResourceStoreRequest(), repository, e);
        }
    }

}
