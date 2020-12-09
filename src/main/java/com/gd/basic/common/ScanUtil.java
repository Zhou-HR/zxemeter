package com.gd.basic.common;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author ZhouHR
 */
@Log4j2
public class ScanUtil {

    public static List<Class> getAllClass(String packageName) {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + packageName.replaceAll("\\.", "\\/") + "/**/*.class";
        List<Class> classList = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    String className = reader.getClassMetadata().getClassName();
                    classList.add(Class.forName(className));
                }
            }
        } catch (IOException e) {
            log.error(e);
        } catch (ClassNotFoundException e) {
            log.error(e);
        }
        return classList;
    }
}
