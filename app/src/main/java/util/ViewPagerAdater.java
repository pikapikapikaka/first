package util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdater extends PagerAdapter{
    private List<View> views;
    private Context context;
    public ViewPagerAdater(List<View> views, Context context){
        this.views = views;
        this.context = context;
    }
    @Override
    public int getCount() {
        return this.views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

        return (view == o);//判断instantiateitem返回的对象是否与当前View代表的是同一个对象
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(this.views.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(this.views.get(position));
        return this.views.get(position);
    }
}
