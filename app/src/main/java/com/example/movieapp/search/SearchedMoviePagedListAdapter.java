package com.example.movieapp.search;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.data.pojo.SearchedMovie;
import com.example.movieapp.data.reposotory.Status;
import com.example.movieapp.movieDetails.SingleMovie;

public class SearchedMoviePagedListAdapter extends PagedListAdapter<SearchedMovie.Results, RecyclerView.ViewHolder> {
    private Context context;
    public static final int MOVIE_VIEW_TYPE = 1;
    public static final int NETWORK_VIEW_TYPE = 2;
    public Status networkStatus;

    public SearchedMoviePagedListAdapter(@NonNull DiffUtil.ItemCallback<SearchedMovie.Results> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == MOVIE_VIEW_TYPE) {
            view = inflater.inflate(R.layout.movie_list_item, parent, false);
            return new MovieItemViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.network_state_item, parent, false);
            return new NetworkStateViewHodler(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE)
            ((MovieItemViewHolder) holder).bindItem(getItem(position), context);
        else if (getItemViewType(position) == NETWORK_VIEW_TYPE)
            ((NetworkStateViewHodler) holder).bind(networkStatus);
    }


    public boolean hasExtraRows() {
        return networkStatus != null && networkStatus != Status.SUCCESS_;
    }

    @Override
    public int getItemCount() {
        if (hasExtraRows())
            return super.getItemCount() + 1;
        else return super.getItemCount();
    }


    @Override
    public int getItemViewType(int position) {
        if (hasExtraRows() && position == getItemCount() - 1)
            return NETWORK_VIEW_TYPE;
        else
            return MOVIE_VIEW_TYPE;
    }


    public void setNetworkStatus(Status networkStatus) {
        Status oldStatus = this.networkStatus;
        boolean hadExtraRow = hasExtraRows();
        this.networkStatus = networkStatus;
        boolean hasExtraRow = hasExtraRows();
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow)
                notifyItemRemoved(super.getItemCount());
            else
                notifyItemInserted(super.getItemCount());
        } else if (hasExtraRow && oldStatus != networkStatus)
            notifyItemChanged(getItemCount() - 1);
    }

    static class MovieItemViewHolder extends RecyclerView.ViewHolder {
        public MovieItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindItem(SearchedMovie.Results movie, Context context) {
            TextView title = itemView.findViewById(R.id.cv_movie_title);
            title.setText(movie.getTitle());

            TextView date = itemView.findViewById(R.id.cv_movie_release_date);
            date.setText(movie.getReleaseDate());

            ImageView posterImageView = itemView.findViewById(R.id.cv_iv_movie_poster);

            Glide.with(context)
                    .load(context.getString(R.string.image_paht) + movie.getPosterPath())
                    .into(posterImageView)
                    .onLoadStarted(context.getResources().getDrawable(R.drawable.placeholder));

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, SingleMovie.class);
                intent.putExtra("id", movie.getId());
                context.startActivity(intent);

            });
        }
    }

    static class NetworkStateViewHodler extends RecyclerView.ViewHolder {
        public NetworkStateViewHodler(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Status status) {
            if (status.name().equals(Status.RUNNING_)) {
                itemView.findViewById(R.id.progress_bar_item).setVisibility(View.VISIBLE);
            } else {
                itemView.findViewById(R.id.progress_bar_item).setVisibility(View.GONE);
            }

            if (status.name().equals(Status.FAILED_)) {
                TextView textView = itemView.findViewById(R.id.error_msg_item);
                textView.setVisibility(View.VISIBLE);
                textView.setText(Status.FAILED_.name());
            } else if (status.name().equals(Status.END_OF_LIST)) {
                TextView textView = itemView.findViewById(R.id.error_msg_item);
                textView.setVisibility(View.VISIBLE);
                textView.setText(status.END_OF_LIST.name());
            } else {
                TextView textView = itemView.findViewById(R.id.error_msg_item);
                textView.setVisibility(View.GONE);
            }


        }
    }
}
