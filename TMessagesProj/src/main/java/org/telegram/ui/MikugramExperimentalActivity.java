package org.telegram.ui;

import static org.telegram.messenger.LocaleController.getString;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.MikugramConfig;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class MikugramExperimentalActivity extends BaseFragment {

    private RecyclerListView listView;
    private ListAdapter listAdapter;
    private int rowCount;

    private int headerRow;
    private int showNetworkSpeedRow;
    private int downloadSpeedBoostRow;
    private int showRPCErrorRow;
    private int mapDriftingFixRow;
    private int infoRow;

    @Override
    public boolean onFragmentCreate() {
        rowCount = 0;
        headerRow = rowCount++;
        showNetworkSpeedRow = rowCount++;
        downloadSpeedBoostRow = rowCount++;
        showRPCErrorRow = rowCount++;
        mapDriftingFixRow = rowCount++;
        infoRow = rowCount++;
        return super.onFragmentCreate();
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(getString(R.string.MikugramExperimental));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) finishFragment();
            }
        });

        listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        fragmentView = frameLayout;

        listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(listAdapter);
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        listView.setOnItemClickListener((view, position) -> {
            if (position == showNetworkSpeedRow) {
                MikugramConfig.toggleShowNetworkSpeed();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isShowNetworkSpeed());
            } else if (position == downloadSpeedBoostRow) {
                MikugramConfig.toggleDownloadSpeedBoost();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isDownloadSpeedBoost());
            } else if (position == showRPCErrorRow) {
                MikugramConfig.toggleShowRPCError();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isShowRPCError());
            } else if (position == mapDriftingFixRow) {
                MikugramConfig.toggleMapDriftingFix();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isMapDriftingFix());
            }
        });

        return fragmentView;
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        public ListAdapter(Context context) { mContext = context; }

        @Override public boolean isEnabled(RecyclerView.ViewHolder holder) {
            return holder.getItemViewType() == 1;
        }
        @Override public int getItemCount() { return rowCount; }

        @NonNull @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 0: view = new HeaderCell(mContext); view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite)); break;
                case 1: view = new TextCheckCell(mContext); view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite)); break;
                default: view = new TextInfoPrivacyCell(mContext); break;
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case 0:
                    ((HeaderCell) holder.itemView).setText(getString(R.string.MikugramExperimental));
                    break;
                case 1: {
                    TextCheckCell cell = (TextCheckCell) holder.itemView;
                    if (position == showNetworkSpeedRow) cell.setTextAndCheck(getString(R.string.MikugramShowNetworkSpeed), MikugramConfig.isShowNetworkSpeed(), true);
                    else if (position == downloadSpeedBoostRow) cell.setTextAndCheck(getString(R.string.MikugramDownloadSpeedBoost), MikugramConfig.isDownloadSpeedBoost(), true);
                    else if (position == showRPCErrorRow) cell.setTextAndCheck(getString(R.string.MikugramShowRPCError), MikugramConfig.isShowRPCError(), true);
                    else if (position == mapDriftingFixRow) cell.setTextAndCheck(getString(R.string.MikugramMapDriftingFix), MikugramConfig.isMapDriftingFix(), false);
                    break;
                }
                case 2:
                    ((TextInfoPrivacyCell) holder.itemView).setText(getString(R.string.MikugramExperimentalInfo));
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == headerRow) return 0;
            if (position == infoRow) return 2;
            return 1;
        }
    }
}

