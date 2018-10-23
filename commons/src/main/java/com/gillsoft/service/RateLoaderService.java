package com.gillsoft.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.gillsoft.model.Rate;
import com.gillsoft.model.RateLoader;

public class RateLoaderService {

    private static RateLoaderService service;
    private ServiceLoader<RateLoader> loader;
    private URLClassLoader classLoader;

    private RateLoaderService() {
    	createLoader();
    }

    public static synchronized RateLoaderService getInstance() {
        if (service == null) {
            service = new RateLoaderService();
        }
        return service;
    }
    
    private void createLoader() {
    	URL[] pluginList = null;
    	try {
			pluginList = (URL[]) Arrays
					.stream(new File(getPluginsDir()).listFiles(
							(dir, name) -> name.toLowerCase().endsWith(".jar") && name.toLowerCase().startsWith("rate-system")))
					.map(file -> {
						try {
							return file.toURI().toURL();
						} catch (MalformedURLException e) {
							return null;
						}
					}).filter(f -> f != null).toArray(URL[]::new);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	classLoader = new URLClassLoader(pluginList, Thread.currentThread().getContextClassLoader());
        loader = ServiceLoader.load(RateLoader.class, classLoader);
    }
    
    private String getPluginsDir() {
    	try {
			Resource resource = new ClassPathResource("rate.properties");
			Properties properties = PropertiesLoaderUtils.loadProperties(resource);
			return properties.getProperty("plugins.dir");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return "";
    }
    
    public List<String> getLoaders() {
    	if (loader == null) {
    		createLoader();
    	}
    	List<String> loaders = new ArrayList<>();
        try {
            Iterator<RateLoader> dictionaries = loader.iterator();
            while (dictionaries.hasNext()) {
            	RateLoader loader = dictionaries.next();
            	loaders.add(loader.getName());
            }
            classLoader.close();
    	} catch (IOException ioe) {
    		ioe.printStackTrace();
        } catch (ServiceConfigurationError serviceError) {
            serviceError.printStackTrace();
        } finally {
        	loader = null;
        }
        return loaders;
    }
    
    public List<Rate> loadRate(String pluginName, Date date) {
    	if (loader == null) {
    		createLoader();
    	}
    	try {
            Iterator<RateLoader> dictionaries = loader.iterator();
            while (dictionaries.hasNext()) {
            	RateLoader loader = dictionaries.next();
            	if (loader.getName().equals(pluginName)) {
            		return loader.loadRate(date);
            	}
            }
            classLoader.close();
    	} catch (IOException ioe) {
    		ioe.printStackTrace();
        } catch (ServiceConfigurationError serviceError) {
            serviceError.printStackTrace();
        } finally {
        	loader = null;
        }
    	return null;
    }

}