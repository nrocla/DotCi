package com.groupon.jenkins.extensions;

import com.groupon.jenkins.buildtype.plugins.*;
import com.groupon.jenkins.dynamic.build.*;
import hudson.*;
import hudson.model.*;
import hudson.tasks.*;
import jenkins.tasks.*;
import org.apache.commons.beanutils.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class GenericSimpleBuildStepPlugin extends DotCiPluginAdapter {
    private Descriptor<?> pluginDescriptor;
    private Object options;

    public GenericSimpleBuildStepPlugin(Descriptor<?> pluginDescriptor, Object options) {
        super("");
        this.pluginDescriptor = pluginDescriptor;
        this.options = options;
    }


    @Override
    public boolean perform(DynamicBuild dynamicBuild, Launcher launcher, BuildListener listener) {
        SimpleBuildStep plugin = getPlugin();
        try {
            plugin.perform(dynamicBuild,launcher,listener);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private SimpleBuildStep getPlugin(){

        try {
            SimpleBuildStep plugin = (SimpleBuildStep) pluginDescriptor.clazz.newInstance();
            Map<String,Object> pluginOptions = (Map<String, Object>) options;
           for(String method: pluginOptions.keySet()) {
               BeanUtils.setProperty(plugin,method,pluginOptions.get(method));
           }
            return plugin;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
