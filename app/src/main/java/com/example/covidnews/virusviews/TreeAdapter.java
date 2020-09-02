package com.example.covidnews.virusviews;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.covidnews.R;
import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TreeAdapter extends BaseNodeAdapter {

    public TreeAdapter() {
        super();
        // 注册Provider，总共有如下三种方式
        addFullSpanNodeProvider(new RootNodeProvider());
        // 普通的item provider
        addNodeProvider(new DesNodeProvider());
        addNodeProvider(new ImgNodeProvider());
        addNodeProvider(new PropertyNodeProvider());
        addNodeProvider(new RelationNodeProvider());
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> data, int position) {
        BaseNode node = data.get(position);
        if (node instanceof RootNode) {
            return 0;
        } else if (node instanceof DesNode) {
            return 1;
        } else if (node instanceof ImgNode) {
            return 2;
        }
         else if (node instanceof PropertyNode) {
            return 3;
        }
         else if (node instanceof RelationNode) {
            return 4;
        }
            return -1;
    }
}

class RootNode extends BaseExpandNode{
    private String Title;
    private List<BaseNode> children;

    public RootNode(String Title,List<BaseNode> children){
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

class DesNode extends BaseNode{
    private String Title;

    public DesNode(String Title){
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

class ImgNode extends BaseNode{
    private String Title;
    private Drawable d;

    public ImgNode(String Title){
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

class PropertyNode extends BaseNode{
    private String pros;
    private String detail;
    PropertyNode(String pros,String detail){
        this.pros = pros;
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public String getPros() {
        return pros;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}

class RelationNode extends BaseNode{
    public String rel;
    public boolean forward;
    public String rival;
    RelationNode(String rel,boolean forward,String rival){
        this.rel = rel;
        this.forward = forward;
        this.rival = rival;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}

class RootNodeProvider extends BaseNodeProvider {

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
        RootNode entity = (RootNode) data;
        helper.setText(R.id.root_text, entity.getTitle());
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        getAdapter().expandOrCollapse(position);
    }
}

class DesNodeProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.des_layout;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
        DesNode entity = (DesNode) data;
        helper.setText(R.id.des_text, entity.getTitle());
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        getAdapter().expandOrCollapse(position);
    }
}

class ImgNodeProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.virus_img_layout;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
        ImgNode entity = (ImgNode) data;
        //helper.setText(R.id.des_text, entity.getTitle());
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        getAdapter().expandOrCollapse(position);
    }
}

class PropertyNodeProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 3;
    }

    @Override
    public int getLayoutId() {
        return R.layout.property_item_layout;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
        PropertyNode entity = (PropertyNode) data;
        helper.setText(R.id.ptitle,entity.getPros()).setText(R.id.pcontent,entity.getDetail());
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        getAdapter().expandOrCollapse(position);
    }
}

class RelationNodeProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 4;
    }

    @Override
    public int getLayoutId() {
        return R.layout.relation_item_layout;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
        RelationNode entity = (RelationNode) data;
        helper.setText(R.id.ptitle,entity.rel);
        if(entity.forward){
            helper.setImageResource(R.id.arrow,R.drawable.right);
        }
        helper.setText(R.id.pcontent,entity.rival);
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        getAdapter().expandOrCollapse(position);
    }
}
