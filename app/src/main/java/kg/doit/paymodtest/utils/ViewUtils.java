package kg.doit.paymodtest.utils;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import kg.doit.domain.model.App;
import kg.doit.paymodtest.R;

public class ViewUtils {
    public static void setGlideImage(ImageView imageView, String url) {
        Glide.with(imageView)
                .load(url)
                .placeholder(R.drawable.place_holder)
                .into(imageView);
    }
}
