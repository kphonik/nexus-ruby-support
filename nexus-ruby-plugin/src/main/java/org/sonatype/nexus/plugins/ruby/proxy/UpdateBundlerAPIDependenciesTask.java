package org.sonatype.nexus.plugins.ruby.proxy;


import javax.inject.Named;

@Named( UpdateBundlerAPIDependenciesTaskDescriptor.ID )
public class UpdateBundlerAPIDependenciesTask
    extends AbstractProxyScheduledTask
{

    public static final String ACTION = "UPDATEBUNDLERAPIDEPENDENCIES";

    @Override
    protected String getRepositoryFieldId()
    {
        return UpdateBundlerAPIDependenciesTaskDescriptor.REPO_FIELD_ID;
    }
    
    @Override
    protected void doRun( ProxyRubyRepository rubyRepository )
            throws Exception
    {
        rubyRepository.updateBundlerDependencies();
    }
    
    @Override
    protected String getAction()
    {
        return ACTION;
    }
    
    @Override
    protected String getMessage()
    {
        if ( getRepositoryId() != null )
        {
            return "Updating bundler-api dependencies of repository " + getRepositoryName();
        }
        else
        {
            return "Updating bundler-api dependencies of all registered gem proxy repositories";
        }
    }
}