package com.example.covidnews.listviews;

import android.view.View;

import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.covidnews.R;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KindAdapter extends BaseNodeAdapter {

    public KindAdapter() {
        super();
        addFullSpanNodeProvider(new KindRootNodeProvider());
        addNodeProvider(new KindButNodeProvider());
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> data, int position) {
        BaseNode node = data.get(position);
        if (node instanceof KindRootNode) {
            return 0;
        }
        else if (node instanceof KindButNode) {
            return 1;
        }
        return -1;
    }
}

class KindRootNode extends BaseExpandNode {
    private String Title;
    private List<BaseNode> children;

    public KindRootNode(String Title,List<BaseNode> children){
        this.Title = Title;
        this.children = children;
        setExpanded(false);
    }

    public String getTitle() {
        return Title;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return children;
    }
}

class KindButNode extends BaseNode{
    private String Title;

    public KindButNode(String Title){
        this.Title = Title;
    }

    public String getTitle() {
        return Title;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}

class KindRootNodeProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.root_layout;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
        KindRootNode entity = (KindRootNode) data;
        helper.setText(R.id.root_text, entity.getTitle());
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        getAdapter().expandOrCollapse(position);
    }
}

class KindButNodeProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.button_item_layout;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
        KindButNode entity = (KindButNode) data;
        helper.setText(R.id.t_btn1, entity.getTitle());
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        getAdapter().expandOrCollapse(position);
    }
}
