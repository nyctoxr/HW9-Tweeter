package service;

import repository.LikesRepository;
import repository.TweetRepository;
import repository.UserRepository;

public class LikeService {
    private TweetRepository tweetRepository;
    private LikesRepository likesRepository;
    private UserRepository userRepository;
    public LikeService() {
        tweetRepository = new TweetRepository();
        likesRepository = new LikesRepository();
        userRepository = new UserRepository();
    }

    public void set

}
