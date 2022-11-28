package kg.doit.paymodtest.ui.list.adapter;

import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import kg.doit.domain.model.App;
import kg.doit.domain.model.AppStatus;
import kg.doit.paymodtest.R;
import kg.doit.paymodtest.databinding.ApplicationItemVhBinding;
import kg.doit.paymodtest.utils.ViewUtils;

public class ApplicationVH extends RecyclerView.ViewHolder {

    public ApplicationItemVhBinding binding;

    public ApplicationVH(ApplicationItemVhBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(App item) {
        ViewUtils.setGlideImage(binding.rivImage, item.getLogo50Link());
        binding.tvTitle.setText(item.getTitle());
        setStatus(item.getStatus(), binding.tvStatus);
    }

    private void setStatus(AppStatus status, TextView textView) {
        switch (status) {
            case ACTUAL : {
                textView.setBackgroundResource(R.drawable.btn_shape_installed);
                textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.blue_344d67));
                textView.setText(R.string.Actual);
                break;
            }
            case DOWNLOADED : {
                textView.setBackgroundResource(R.drawable.ripple_submit);
                textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.white_f3ecb0));
                textView.setText(R.string.Install);
                break;
            }
            case NEED_TO_UPDATE: {
                textView.setBackgroundResource(R.drawable.ripple_btn_need_to_update);
                textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.white_f3ecb0));
                textView.setText(R.string.Update);
                break;
            }
            case NOT_DOWNLOADED: {
                textView.setBackgroundResource(R.drawable.ripple_download);
                textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.blue_344d67));
                textView.setText(R.string.Download);
                break;
            }
        }
    }


}
