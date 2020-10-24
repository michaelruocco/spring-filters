package uk.co.mruoc.spring.filter;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class HeaderAdapter {

    private final Map<String, Collection<String>> values;

    public HeaderAdapter(Map<String, Collection<String>> values) {
        this.values = new CaseInsensitiveMap<>(values);
    }

    public int size() {
        return values.size();
    }

    public Collection<String> getNames() {
        return values.keySet();
    }

    public boolean isPresent(String name) {
        return values.containsKey(name);
    }

    public Collection<String> get(String name) {
        return values.getOrDefault(name, Collections.emptyList());
    }

    public String getAsString(String name) {
        return String.join(",", get(name));
    }

}
