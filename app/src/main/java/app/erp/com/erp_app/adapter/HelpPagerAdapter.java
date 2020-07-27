package app.erp.com.erp_app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.erp.com.erp_app.R;

public class HelpPagerAdapter extends PagerAdapter {

    private Context mContext = null;
    private String help_key = "";

    public HelpPagerAdapter() {
    }

    public HelpPagerAdapter(Context context, String key) {
        this.mContext = context;
        this.help_key = key;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;
        if(mContext != null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.help_page,container,false);
            ImageView imageView = (ImageView) view.findViewById(R.id.help_img);
            switch (help_key){
                case "insert" :

                    switch (position){
                        case 0 :
                            imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.help1));
                            break;
                        case 1:
                            imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.help2));
                            break;
                        case 2:
                            imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.help3));
                            break;
                        case 3:
                            imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.help4));
                            break;
                        case 4:
                            imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.help5));
                            break;
                        case 5:
                            imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.help6));
                            break;
                        case 6:
                            imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.help7));
                            break;
                    }
                    break;
                case "list" :

                    switch (position){
                        case 0 :
                            imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.edit_help1));
                            break;
                        case 1:
                            imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.edit_help2));
                            break;
                        case 2:
                            imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.edit_help3));
                            break;
                    }
                case "care" :

                    switch (position){
                        case 0 :
                            imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.care_insert_help1));
                            break;
                    }
                    break;
            }
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        int return_int = 0;
        switch (help_key){
            case "insert" :
                return_int = 7;
                break;
            case "list" :
                return_int = 3;
                break;
            case "care" :
                return_int = 1;
                break;
        }
        return return_int;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }
}
