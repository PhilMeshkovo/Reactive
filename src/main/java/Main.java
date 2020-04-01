import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<String> strings = List.of("Ivan","Petr","Philipp");
        Observable.just(strings)
                .observeOn(Schedulers.computation())
                .flatMap(s -> {
                    System.out.println("flatMap and map в потоке: " + Thread.currentThread().getName());
//                    throw new IOException("недоступно");
                    return Observable.fromIterable(s);
                })
                .map(o -> "<b> " + o + " </b>")
                .observeOn(Schedulers.computation())
                .subscribe(a ->{
                    System.out.println(a + " поток: " + Thread.currentThread().getName());
                }, e -> {
                    System.out.println("error: " + e);
                }, () -> {
                    System.out.println("Поток завершен. Больше данных нет.");
                });
        System.out.println(Thread.currentThread().getName() + "\nend");
        Thread.sleep(500);
    }
}
