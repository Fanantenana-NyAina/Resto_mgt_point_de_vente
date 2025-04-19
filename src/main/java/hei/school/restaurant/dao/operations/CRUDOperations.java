package hei.school.restaurant.dao.operations;

import java.util.List;

public interface CRUDOperations<I> {
    List<I> getAll(Integer page, Integer size);
    List<I> saveAll(List<I> items);
}
