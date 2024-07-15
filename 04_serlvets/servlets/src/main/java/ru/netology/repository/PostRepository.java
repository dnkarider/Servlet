package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Stub
public class PostRepository {
    private final Set<Post> posts = Collections.synchronizedSet(new HashSet());
//    private final List<Post> posts = new ArrayList<>();
    private List<Long> emptyIds = new ArrayList<>();
    private int idTracker;
    public Set<Post> all() {
        return posts;
    }

    public Optional<Post> getById(long id) {
        for (Post post : posts) {
            if (id != 0 && post.getId() == id) {
                return Optional.of(post);
            }
        }
        return Optional.empty();
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            if(!emptyIds.isEmpty()){
                long id = emptyIds.remove(0);
                posts.add(post);
                post.setId(id);
                return post;
            }
            posts.add(post);
            post.setId(idTracker + 1);
            idTracker++;
        } else if (post.getId() <= idTracker && post.getId() != 0) {
            if(emptyIds.contains(post.getId())){
                emptyIds.remove(post.getId());
                posts.add(post);
                return post;//проблема может быть в порядке листа, когда нужно будет достать элемент по индексу
            }
            for (Post post1 : posts) {
                if(post1.getId() == post.getId()){
                    posts.remove(post1);
                    posts.add(post);
                    break;
                }
            }
        } else if(post.getId() > idTracker && post.getId() != 0){
            posts.add(post);
            post.setId(idTracker + 1);
            idTracker++;
        }
        return post;
    }
//
    public void removeById(long id) {
        if (id < idTracker + 1 && id != 0) {
            for (Post post : posts) {
                if (post.getId() == id) {
                    posts.remove(post);
                    emptyIds.add(id);
                    break;
                }
            }
        }
    }
}
