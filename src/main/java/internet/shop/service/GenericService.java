package internet.shop.service;

import java.util.List;

public interface GenericService<T, I> {
    T create(T driver);

    T get(I id);

    List<T> getAll();

    T update(T driver);

    boolean delete(I id);
}
