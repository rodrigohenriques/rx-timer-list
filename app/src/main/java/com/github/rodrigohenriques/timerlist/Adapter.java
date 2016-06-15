package com.github.rodrigohenriques.timerlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class Adapter extends RecyclerView.Adapter<Adapter.CustomViewHolder> {
    private final static String TAG = Adapter.class.getSimpleName();

    private final List<String> items;
    private final PublishSubject<Event> publishSubject = PublishSubject.create();
    private final Map<String, Subscription> subscriptions;
    private final Context context;

    public Adapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
        this.subscriptions = new HashMap<>();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(context).inflate(R.layout.holder_layout, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        String name = items.get(position);
        holder.bind(name);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName, textViewTimer;
        private Subscription tempSubscription;

        public CustomViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewTimer = (TextView) itemView.findViewById(R.id.textViewTime);
        }

        private void bind(String name) {
            textViewName.setText(name);

            if (tempSubscription != null)
                tempSubscription.unsubscribe();

            tempSubscription = publishSubject
                    .filter(event -> event.getName().equals(name))
                    .map(event -> event.getElapsedTime())
                    .map(elapsedTime -> String.format("%ds", elapsedTime))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(formattedElapsedTime -> textViewTimer.setText(formattedElapsedTime), Adapter.this::handleError);

            itemView.setOnClickListener(v -> {
                Subscription subscription = subscriptions.get(name);

                if (subscription == null || subscription.isUnsubscribed()) {
                    subscription = startTimer(name);
                    subscriptions.put(name, subscription);
                } else {
                    subscription.unsubscribe();
                    textViewTimer.setText("0s");
                }
            });
        }

        private Subscription startTimer(String name) {
            return Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                    .onBackpressureDrop()
                    .map(milliseconds -> new Event(name, milliseconds))
                    .subscribe(publishSubject::onNext, Adapter.this::handleError);
        }
    }

    private void handleError(Throwable t) {
        Log.e(TAG, "failure listening events", t);
    }
}