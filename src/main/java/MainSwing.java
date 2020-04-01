import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class MainSwing extends JFrame {
    public static void main(String[] args) {
        new MainSwing().init();
    }

    private void init() {
        setSize(400,400);
        JTextArea text = new JTextArea();
        add(text, BorderLayout.CENTER);
        JButton load = new JButton("Heavy Load");
        add(load,BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        Observable<String> observable = Observable.create(subscriber -> {
            System.out.println("create: " + Thread.currentThread().getName());
            load.addActionListener(e -> {
                System.out.println("click: " + Thread.currentThread().getName());
                subscriber.onNext(e.getActionCommand());
            });
        });
        observable
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .doOnEach(e -> {
                    System.out.println("HEAVY WORK: " + e + " at " +  Thread.currentThread().getName());
                    Thread.sleep(2000);
                })
                .observeOn(new SO())
                .subscribe(e -> {
                    text.append(e + "\n");
                    System.out.println("message: " + e + " at " +  Thread.currentThread().getName());
                });
    }
    private class SO extends Scheduler{

        @Override
        public Worker createWorker() {
            return new Worker() {
                @Override
                public Disposable schedule(Runnable runnable, long l, TimeUnit timeUnit) {
                    SwingUtilities.invokeLater(runnable);
                    return new Disposable() {
                        @Override
                        public void dispose() {

                        }

                        @Override
                        public boolean isDisposed() {
                            return false;
                        }
                    };
                }

                @Override
                public void dispose() {

                }

                @Override
                public boolean isDisposed() {
                    return false;
                }
            };
        }
    }
}
