package org.neuroml.model.injectingplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.xml.sax.ErrorHandler;

import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;

/**
 * This class injects code into NeuroML classes
 * while they're being generated. Its used as a
 * plugin for the xjc ant task. This class is
 * based on an example given 
 * <a href="http://weblogs.java.net/blog/kohsuke/archive/2005/06/writing_a_plugi.html">here</a>.
 * 
 * @author mschachter
 */
public class CodeInjectingPlugin extends AbstractParameterizablePlugin
{
	public CodeInjectingPlugin() {
		
		super();
		System.err.println("INSTANTIATING CODEINJECTING PLUGIN");
	}


	@Override
	public String getOptionName()
	{
		return "Xneuromlcodeinjector";
	}


	public String getUsage()
	{
		return " -Xneuromlcodeinjector "; 
	}

	@Override
	public boolean run(Outline model, Options opt, ErrorHandler errorHandler)
	{	
		String wdir = System.getProperty("user.dir");
		String tdir = wdir + File.separator + "conf/jaxb";
		
		System.err.println("NeuroML JAXB Plugin");
		System.err.println("    Working Directory: " + wdir);
		System.err.println("    Template Directory: " + tdir);
		
		String fname, line;
		File f;
		FileReader r;  BufferedReader br;		
		for( ClassOutline co : model.getClasses() ) {		
			
			fname = tdir + File.separator + co.implClass.fullName();
			System.err.println("Checking "+fname);
			f = new File(fname);
			if (f.exists()) {
				System.err.println("     ** Injecting code:");
				System.err.println("          File : " + fname);
				System.err.println("          Class: " + co.implClass.fullName());
				
				try {
					r = new FileReader(f);
					br = new BufferedReader(r);					
					co.implClass.direct("\n\n//NEUROML PLUGIN INSERTED CODE\n");
					co.implClass.direct("//   From File : " + fname + "\n\n");
					while ((line = br.readLine()) != null) {
						co.implClass.direct(line + "\n");
					}			
					br.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
			else
			{
				System.err.println("File doesn't exist!");
			}
		}		
		return true;
	}
	
	

}
