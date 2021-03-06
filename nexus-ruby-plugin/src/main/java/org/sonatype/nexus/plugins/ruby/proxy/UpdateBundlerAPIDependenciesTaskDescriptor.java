package org.sonatype.nexus.plugins.ruby.proxy;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.formfields.FormField;
import org.sonatype.nexus.formfields.RepoComboFormField;
import org.sonatype.nexus.tasks.descriptors.AbstractScheduledTaskDescriptor;

@Singleton
@Named( "UpdateBundlerAPIDependencies" )
public class UpdateBundlerAPIDependenciesTaskDescriptor
    extends AbstractScheduledTaskDescriptor
{
    public static final String ID = "UpdateBundlerAPIDependenciesTask";

    public static final String REPO_FIELD_ID = "repositoryId";

    private final RepoComboFormField repoField = new RepoComboFormField( REPO_FIELD_ID,
                                                                         FormField.MANDATORY );

    public String getId()
    {
        return ID;
    }

    public String getName()
    {
        return "Update Bundler API Dependencies";
    }

    @SuppressWarnings( "rawtypes" )
    public List<FormField> formFields()
    {
        List<FormField> fields = new ArrayList<FormField>();

        fields.add( repoField );
        
        return fields;
    }
}