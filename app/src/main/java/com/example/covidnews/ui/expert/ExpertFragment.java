package com.example.covidnews.ui.expert;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.covidnews.R;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpertFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    RecyclerView myview;
    RowAdapter adapter;
    ArrayList<Expert_detail> experts;

    // TODO: Rename and change types of parameters
    public ExpertFragment() {
        // Required empty public constructor
        if(experts==null) {
            experts = new ArrayList<>();
        }
        adapter = new RowAdapter(R.layout.row_expert_layout, null);
    }

    // TODO: Rename and change types and number of parameters
    public static ExpertFragment newInstance() {
        ExpertFragment fragment = new ExpertFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_expert, container, false);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myview = root.findViewById(R.id.rview);
        myview.setLayoutManager(manager);
        myview.setAdapter(adapter);
        View view = getLayoutInflater().inflate(R.layout.loading_layout,null);
        adapter.setEmptyView(view);
        adapter.addChildClickViewIds(R.id.ntitle1);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                Bundle bundle = new Bundle();
                Expert_detail to_send = (Expert_detail) adapter.getData().get(position);
                bundle.putSerializable("item", to_send);
                System.out.println(to_send);
                Intent intent = new Intent(getActivity(), ExpertDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return root;
    }

    public ArrayList<Expert_detail> getlist(){
        return experts;
    }

    public void changed(){
        adapter.setList(experts);
        adapter.notifyDataSetChanged();
    }

}
