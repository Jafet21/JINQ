package open.jinq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.*;

public interface Sequence<T> extends Iterable<T> {

    default <R> Sequence<R> select(Function<T, R> mapper) {
        return () -> new MapperIterator<>(iterator(), mapper);
    }

    default Sequence<T> where(Predicate<T> filter) {
        return () -> new FilterIterator<>(iterator(), filter);
    }

    default Sequence<T> skip(int amount) {
        return () -> {
            Iterator<T> it = iterator();
            for (int i = 0; i < amount && it.hasNext(); i++) {
                it.next();
            }
            return it;
        };
    }

    default Sequence<T> take(int amount) {
        return () -> new Iterator<T>() {
            int count = 0;
            Iterator<T> it = iterator();

            @Override
            public boolean hasNext() {
                return count < amount && it.hasNext();
            }

            @Override
            public T next() {
                return it.next();
            }
        };
    }

    default List<T> toList() {
        List<T> list = new ArrayList<>();
        forEach(list::add);
        return list;
    }

    static <T> Sequence<T> of(Iterable<T> iterable) {
        return () -> new DefaultIterator<>(iterable.iterator());
    }

    static <T> Sequence<T> of(T... array) {
        return () -> new ArrayIterator<>(array);
    }
}

class Skipper<T> implements Iterator<T> {

}

class ArrayIterator<T> implements Iterator<T> {

    private final T[] array;
    private int index;

    ArrayIterator(T[] array) {
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return index < array.length;
    }

    @Override
    public T next() {
        return array[index++];
    }
}

class DefaultIterator<T> implements Iterator<T> {

    private final Iterator<T> items;

    public DefaultIterator(Iterator<T> items) {
        this.items = items;
    }

    @Override
    public boolean hasNext() {
        return items.hasNext();
    }

    @Override
    public T next() {
        return items.next();
    }
}

class MapperIterator<T, R> implements Iterator<R> {

    private final Function<T, R> mapper;
    private final Iterator<T> items;

    MapperIterator(Iterator<T> items, Function<T, R> mapper) {
        this.mapper = mapper;
        this.items = items;
    }

    @Override
    public boolean hasNext() {
        return items.hasNext();
    }

    @Override
    public R next() {
        return mapper.apply(items.next());
    }
}

class FilterIterator<T> implements Iterator<T> {

    private final Predicate<T> predicate;
    private final Iterator<T> items;
    private T next;

    public FilterIterator(Iterator<T> items, Predicate<T> predicate) {
        this.predicate = predicate;
        this.items = items;
    }

    @Override
    public boolean hasNext() {
        while (items.hasNext()) {
            next = items.next();
            if (predicate.test(next)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T next() {
        return next;
    }
}
