package com.sophie.miller.portablecloset.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sophie.miller.portablecloset.R;
import com.sophie.miller.portablecloset.databinding.DialogEditStylesBinding;
import com.sophie.miller.portablecloset.databinding.ItemStyleBinding;
import com.sophie.miller.portablecloset.objects.Style;
import com.sophie.miller.portablecloset.ui.fragments.ClothesEditDetailFragment;
import com.sophie.miller.portablecloset.utils.Notifications;
import com.sophie.miller.portablecloset.utils.StringHandler;
import com.sophie.miller.portablecloset.viewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * open in try catch to avoid crashes
 */
public class EditStylesDialog extends Dialog implements View.OnClickListener {

    private MainViewModel viewModel;
    private Fragment fragment;
    private Context context;
    private DialogEditStylesBinding binding;
    private StylesDialogAdapter stylesDialogAdapter;

    public EditStylesDialog(@NonNull Fragment fragment, @NonNull MainViewModel viewModel, int theme) {
        super(fragment.getActivity(), theme);
        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.viewModel = viewModel;
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DialogEditStylesBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());
        //adjust on keyboard
        Objects.requireNonNull(getWindow()).setFlags(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.editStylesRecView.setLayoutManager(new LinearLayoutManager(context));
        stylesDialogAdapter = new StylesDialogAdapter(viewModel, fragment, context);
        binding.editStylesRecView.setAdapter(stylesDialogAdapter);
        binding.editStylesAddBtn.setOnClickListener(this);
        binding.editStylesClose.setOnClickListener(v -> dismiss());

    }
    /**
     * set dialogClosed in the fragment
     *
     */
    @Override
    public void dismiss() {
        super.dismiss();
        if (fragment instanceof ClothesEditDetailFragment) {
            ((ClothesEditDetailFragment) fragment).dialogClosed();
        }
    }

    @Override
    public void onClick(View v) {
        String newStyle = StringHandler.getText(binding.editStylesEdittext);
        if (newStyle.length() < 1) {
            Notifications.makeToast(context, context.getString(R.string.please_enter_name));
        } else if (viewModel.database.styleDao().checkIfExists(newStyle) > 0) {
            Notifications.makeToast(context, context.getString(R.string.style_esists));
        } else {
            viewModel.database.styleDao().insertStyle(new Style(newStyle));
        }
        binding.editStylesEdittext.setText("");
    }
}

class StylesDialogAdapter extends RecyclerView.Adapter<StylesDialogAdapter.StyleHolder> {
    private MainViewModel viewModel;
    private ArrayList<String> stylesList = new ArrayList<>();
    private Context context;
    //last item delete tap index
    private int lastDelete = -1;

    StylesDialogAdapter(MainViewModel viewModel, Fragment fragment, Context context) {
        this.viewModel = viewModel;
        viewModel.listOfStyleNames().observe(fragment, strings -> {
            stylesList.clear();
            if (strings.size() > 0) {
                stylesList.addAll(strings);
            }
            notifyDataSetChanged();
        });
        this.context = context;
    }

    @NonNull
    @Override
    public StyleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_style, parent, false);
        return new StyleHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StyleHolder holder, int position) {
        holder.binding.itemStyleText.setText(stylesList.get(position));
        holder.binding.itemStyleDelete.setOnClickListener(v -> {
            if (lastDelete == viewModel.database.styleDao().getStyleId(stylesList.get(position))) {
                int index = lastDelete;
                viewModel.database.styleDao().deleteStyleAt(index);
                viewModel.database.clothingItemDao().updateStyle(index);
            } else {
                Notifications.makeToast(context,  context.getString(R.string.confirm_delete));
                lastDelete = viewModel.database.styleDao().getStyleId(stylesList.get(position));
                (new Handler()).postDelayed(() -> lastDelete = -1, 2000);
            }
        });
        holder.binding.itemStyleLayout.setOnClickListener(view -> {
            //hide keyboard on click outside
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stylesList.size();
    }

    static class StyleHolder extends RecyclerView.ViewHolder {
        ItemStyleBinding binding;

        StyleHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStyleBinding.bind(itemView);

        }
    }
}
