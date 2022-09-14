package br.com.meta.desafio.mapper;

import br.com.meta.desafio.model.FileAttribute;
import br.com.meta.desafio.model.FileCache;

public class FileMapper {
    public static FileCache toCache(FileAttribute attribute) {
        final FileCache cache = new FileCache();
        cache.setSize(attribute.getSize());
        cache.setLines(attribute.getLines());
        cache.setExtension(attribute.getExtension());

        return cache;
    }

    public static FileAttribute toAttribute(FileCache cache) {
        final FileAttribute attribute = new FileAttribute();
        attribute.setSize(cache.getSize());
        attribute.setExtension(cache.getExtension());
        attribute.setLines(cache.getLines());

        return attribute;
    }
}
