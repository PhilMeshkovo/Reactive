import io.reactivex.Observable;

import java.io.IOException;

public class Main1 {
    public static void main(String[] args) {
        Observable<String> tweets = Observable.create(subscriber -> {
            subscriber.onNext("Tweet 1");
            subscriber.onNext("Tweet 2 ");
            subscriber.onNext(" Tweet 3");
            subscriber.onError(new IOException("упс"));
            subscriber.onComplete();
        });
        tweets
                .map(e -> e.trim())
                .subscribe( n -> {
            System.out.println("*" + n + "*");
        },e -> {
                    System.out.println("my error: " + e);
                }, () -> {
                    System.out.println("All done");
                });
    }
}
