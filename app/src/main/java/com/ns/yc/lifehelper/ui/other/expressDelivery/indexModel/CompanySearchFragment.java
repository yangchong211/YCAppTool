package com.ns.yc.lifehelper.ui.other.expressDelivery.indexModel;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/6/23
 * 描    述：搜索页面
 * 修订历史：
 * 备注：此搜索功能更为强大，搜索结果可以是多条，比如输入‘安’，那么结果便是以安开头的所有城市
 * ================================================
 */
public class CompanySearchFragment extends BaseFragment {


    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv_no_result)
    TextView tvNoResult;
    private List<ExpressDeliveryEntity> mData;
    private SearchAdapter mAdapter;
    private String mQueryText;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_me_city_search;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    public void bindData(List<ExpressDeliveryEntity> data) {
        this.mData = data;
        mAdapter = new SearchAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        if (mQueryText != null) {
            mAdapter.getFilter().filter(mQueryText);
        }
    }

    /**
     * 根据newText 进行查找, 显示
     */
    public void bindQueryText(String newText) {
        if (mData == null) {
            mQueryText = newText.toLowerCase();
        } else if (!TextUtils.isEmpty(newText)) {
            mAdapter.getFilter().filter(newText.toLowerCase());
        }
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.VH> implements Filterable {

        private List<ExpressDeliveryEntity> items = new ArrayList<>();

        public SearchAdapter() {
            items.clear();
            items.addAll(mData);
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            final VH holder = new VH(LayoutInflater.from(getActivity()).inflate(R.layout.item_search_city, parent, false));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Toast.makeText(getActivity(), items.get(position).getName(), Toast.LENGTH_SHORT).show();
                }
            });
            return holder;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.tvName.setText(items.get(position).getName());
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    ArrayList<ExpressDeliveryEntity> list = new ArrayList<>();
                    for (ExpressDeliveryEntity item : mData) {
                        if (item.getPinyin().startsWith(constraint.toString()) || item.getName().contains(constraint)) {
                            list.add(item);
                        }
                    }
                    FilterResults results = new FilterResults();
                    results.count = list.size();
                    results.values = list;
                    return results;
                }

                @Override
                @SuppressWarnings("unchecked")
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    ArrayList<ExpressDeliveryEntity> list = (ArrayList<ExpressDeliveryEntity>) results.values;
                    items.clear();
                    items.addAll(list);
                    if (results.count == 0) {
                        tvNoResult.setVisibility(View.VISIBLE);
                        tvResult.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        tvNoResult.setVisibility(View.INVISIBLE);
                        tvResult.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    notifyDataSetChanged();
                }
            };
        }

        class VH extends RecyclerView.ViewHolder {
            private TextView tvName;

            public VH(View itemView) {
                super(itemView);
                tvName = (TextView) itemView.findViewById(R.id.tv_name);
            }
        }
    }

}
