package com.example.covidnews;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

public class ProvinceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);
        ColumnChartView colview = (ColumnChartView)findViewById(R.id.col1);
        int subcol = 3;
        int numcol = 23;
        List<Column> cols = new ArrayList<Column>();
        List<SubcolumnValue> subcols;
        for(int i=0;i<numcol;i++){
            subcols = new ArrayList<SubcolumnValue>();
            for(int j=0;j<subcol;j++){
                subcols.add(new SubcolumnValue(11));
            }
            Column col = new Column(subcols);
            col.setHasLabels(true);
            col.setHasLabelsOnlyForSelected(true);
            cols.add(col);
        }
        ColumnChartData data = new ColumnChartData(cols);
        Axis X,Y;
        X = new Axis(); Y = new Axis();
        X.setName("省份");
        Y.setName("疫情情况");
        data.setAxisXBottom(X);
        data.setAxisXTop(Y);
        colview.setColumnChartData(data);
    }
}