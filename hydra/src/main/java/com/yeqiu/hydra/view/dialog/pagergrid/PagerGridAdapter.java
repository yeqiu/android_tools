package com.yeqiu.hydra.view.dialog.pagergrid;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * @project：HailHydra
 * @author：小卷子
 * @date 2019/7/12
 * @describe：
 * @fix：
 */
public class PagerGridAdapter extends PagerAdapter {

    private List<GridView> gridViews;


    public PagerGridAdapter(List<GridView> gridViews) {
        this.gridViews = gridViews;
    }

    @Override
    public int getCount() {
        return gridViews == null ? 0 : gridViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       // super.destroyItem(container, position, object);

        container.removeView((View) object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        GridView gridView = gridViews.get(position);
        container.addView(gridView);


        return gridView;
    }
}
