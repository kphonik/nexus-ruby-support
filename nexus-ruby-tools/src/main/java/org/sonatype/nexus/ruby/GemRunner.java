package org.sonatype.nexus.ruby;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jruby.runtime.builtin.IRubyObject;

public class GemRunner
{

    private final JRubyScriptingContainer ruby;
    
    private final IRubyObject runner;
    
    private final String baseUrl;
    
    private boolean listRemoteFirstRun = true;
    
    public GemRunner( JRubyScriptingContainer ruby, String baseUrl )
    {
        this.ruby = ruby;
        this.baseUrl = baseUrl;
        
        try
        {
            IRubyObject runnerClass = ruby.parseFile( "nexus/gem_runner.rb" ).run();
            runner = ruby.callMethod( runnerClass, "new", IRubyObject.class );
        } 
        catch ( FileNotFoundException e )
        {
            throw new RuntimeException( "error", e);
        }
    }
    
    public String install( String repoId, String... gems )
    {
        List<String> args = new ArrayList<String>();
        args.add( "install" );
        args.add( "-r" );
        addNoDocu( args );
        setSource( args, repoId );
        args.addAll( Arrays.asList( gems ) );
        return ruby.callMethod( runner, "exec", args.toArray(), String.class );
    }

    private void setSource(List<String> args, String repoId ) {
        args.add( "--clear-sources" );
        args.add( "--source" );
        args.add( baseUrl + repoId + "/" );
        args.add( "--update-sources" );
    }
    
    public String install( File... gems )
    {
        List<String> args = new ArrayList<String>();
        args.add( "install" );
        args.add( "-r" );
        addNoDocu( args );
        for( File gem: gems)
        {
            args.add( gem.getAbsolutePath() );
        }
        
        return ruby.callMethod( runner, "exec", args.toArray(), String.class );
    }

    private void addNoDocu(List<String> args) {
        args.add( "--no-rdoc" );
        args.add( "--no-ri" );
    }

    public String list()
    {
        return list( null );
    }
    
    public String list( String repoId )
    {
        List<String> args = new ArrayList<String>();
        args.add( "list" );
        if ( repoId == null )
        {
            args.add( "-l" );
        }
        else
        {
            if ( this.listRemoteFirstRun )
            {
                this.listRemoteFirstRun = false;
            }
            else
            {
                System.err.println( ">>>>>>>>>>>>> repeated calls here do only show the cached first call" );
            }
            args.add( "-r" );
            setSource( args, repoId );
        }
        
        return ruby.callMethod( runner, "exec", args.toArray(), String.class );
    }
    
    public String nexus( File config, File gem )
    {
        List<String> args = new ArrayList<String>();
        args.add( "nexus" );
        args.add( "--nexus-config" );
        args.add( config.getAbsolutePath() );
        args.add( gem.getAbsolutePath() );
        
        // make sure the custom gem command is loaded when the gem is installed
        ruby.callMethod( runner, "load_plugins" );        
        
        return ruby.callMethod( runner, "exec", args.toArray(), String.class );        
    }
}