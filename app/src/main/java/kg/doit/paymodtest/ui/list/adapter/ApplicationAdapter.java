package kg.doit.paymodtest.ui.list.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kg.doit.domain.model.App;
import kg.doit.paymodtest.databinding.ApplicationItemVhBinding;
import kg.doit.paymodtest.utils.ViewUtils;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationVH> {
    private final OnItemClick typeClicked;
    private final ArrayList<App> list = new ArrayList<>();

    @NonNull
    @Override
    public ApplicationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ApplicationVH holder = new ApplicationVH(ApplicationItemVhBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        ));

        holder.binding.getRoot().setOnClickListener(view ->
            typeClicked.onItemClick(list.get(holder.getAdapterPosition()).getType())
        );

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationVH holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ApplicationAdapter(OnItemClick itemClick) {
        this.typeClicked = itemClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<App> value) {
        list.clear();
        list.addAll(value);
        notifyDataSetChanged();
    }

    public interface OnItemClick {
        void onItemClick (String type);
    }
}
