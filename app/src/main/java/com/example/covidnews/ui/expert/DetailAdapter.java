package com.example.covidnews.ui.expert;
import android.graphics.Color;
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
import java.util.Map;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

public class DetailAdapter extends BaseNodeAdapter {

    public DetailAdapter() {
        super();
        addFullSpanNodeProvider(new ExpRootNodeProvider());
        addNodeProvider(new ExpDesNodeProvider());
        addNodeProvider(new FrontProvider());
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> data, int position) {
        BaseNode node = data.get(position);
        if (node instanceof ExpRootNode) {
            return 0;
        }
        else if (node instanceof ExpDesNode) {
            return 1;
        }
        else if (node instanceof FrontNode) {
            return 2;
        }
        return -1;
    }
}

class ExpRootNode extends BaseExpandNode{
    private String Title;
    private List<BaseNode> children;

    public ExpRootNode(String Title,List<BaseNode> children){
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

class ExpDesNode extends BaseNode{
    private String Title;

    public ExpDesNode(String Title){
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

class FrontNode extends BaseNode{
    private Map<String,Float> Title;

    public FrontNode(Map<String,Float> title){
        this.Title = title;
    }

    public Map<String,Float> getTitle() {
        return Title;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}

class ExpRootNodeProvider extends BaseNodeProvider {

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
        ExpRootNode entity = (ExpRootNode) data;
        helper.setText(R.id.root_text, entity.getTitle());
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        getAdapter().expandOrCollapse(position);
    }
}

class ExpDesNodeProvider extends BaseNodeProvider {

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
        ExpDesNode entity = (ExpDesNode) data;
        helper.setText(R.id.des_text, entity.getTitle());
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        getAdapter().expandOrCollapse(position);
    }
}

class FrontProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.index_item_layout;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
        FrontNode entity = (FrontNode) data;
        ColumnChartView colview = (ColumnChartView) helper.getView(R.id.col1);
        int subcol = 1;
        List<Column> cols = new ArrayList<Column>();
        List<SubcolumnValue> subcols;
        ArrayList<String> names = new ArrayList<String>();
        float count = 0.f;
        ArrayList<Float> maps = new ArrayList<>();
        for(Map.Entry<String,Float> entry:entity.getTitle().entrySet()){
            subcols = new ArrayList<SubcolumnValue>();
            maps.add(count);
            count++;
            names.add(entry.getKey());
            subcols.add(new SubcolumnValue(entry.getValue(),Color.parseColor("#6495ED")));
            Column col = new Column(subcols);
            col.setHasLabels(true);
            col.setHasLabelsOnlyForSelected(true);
            cols.add(col);
        }
        ColumnChartData datasets = new ColumnChartData(cols);
        Axis X,Y;
        X = Axis.generateAxisFromCollection(maps,names);
        Y = new Axis();
        Y.setHasLines(true);
        Y.setName("");
        datasets.setAxisXBottom(X);
        datasets.setAxisYLeft(Y);
        colview.setColumnChartData(datasets);
        Viewport viewport = new Viewport(1,colview.getMaximumViewport().height()*1.25f,names.size()>4?4:names.size(),0);
        colview.setCurrentViewport(viewport);
        colview.moveTo(1,0);

    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        getAdapter().expandOrCollapse(position);
    }
}

