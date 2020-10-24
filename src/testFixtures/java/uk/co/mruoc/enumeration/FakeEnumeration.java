package uk.co.mruoc.enumeration;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

@RequiredArgsConstructor
public class FakeEnumeration implements Enumeration<String> {

    private final Iterator<String> names;

    public FakeEnumeration(String... names) {
        this(Arrays.asList(names));
    }

    public FakeEnumeration(Collection<String> names) {
        this(names.iterator());
    }

    @Override
    public boolean hasMoreElements() {
        return names.hasNext();
    }

    @Override
    public String nextElement() {
        return names.next();
    }

}
