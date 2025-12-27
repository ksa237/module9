package ru.netology.repository;

import ru.netology.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// Stub
public class PostRepository {

    private CopyOnWriteArrayList<Post> webData = new CopyOnWriteArrayList<>();
    //private ConcurrentHashMap<Long, Post> webData = new ConcurrentHashMap<>();


    public List<Post> all() {
        //ksa237++
        return webData;
        //return Collections.emptyList();
        //ksa237--
    }

    public Optional<Post> getById(long id) {
        //ksa237++
        var post = webData.get((int) id);

        if (post != null) {
            return Optional.of(post);
        }
        return Optional.empty();
        //ksa237--
    }

    public Post save(Post post) {
        //ksa237++
        //считаем что нужно записать новый пост.
        //добавляем данные в конец списка, получаем присовенный id для нового поста
        webData.add(post);
        post.setId(webData.size());
        //ksa237--

        return post;
    }

    public void removeById(long id) {
    }
}
