package com.cssrc.ibms.core.db.mybatis.query.scan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import com.cssrc.ibms.core.db.mybatis.query.annotion.Table;
/**
 * 
 * <p>Title:TableScaner</p>
 * @author Yangbo 
 * @date 2016-8-8下午03:07:23
 */
public class TableScaner {
	public static List<Class<?>> findTableScan(Resource[] basePackage)
			throws IOException, ClassNotFoundException {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
				resourcePatternResolver);

		List candidates = new ArrayList();
		if (basePackage == null)
			return candidates;
		for (Resource resource : basePackage) {
			if (!resource.isReadable())
				continue;
			MetadataReader metadataReader = metadataReaderFactory
					.getMetadataReader(resource);

			if (isCandidate(metadataReader)) {
				candidates.add(Class.forName(metadataReader.getClassMetadata()
						.getClassName()));
			}
		}
		return candidates;
	}

	private static boolean isCandidate(MetadataReader metadataReader)
			throws ClassNotFoundException {
		try {
			Class c = Class.forName(metadataReader.getClassMetadata()
					.getClassName());

			if (c.getAnnotation(Table.class) != null)
				return true;
		} catch (Throwable e) {
		}
		return false;
	}
}
